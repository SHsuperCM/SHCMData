package shcm.shsupercm.data.data.generation;

import shcm.shsupercm.data.data.DataAnnotationRegistry.DataTypeHandler;
import shcm.shsupercm.data.data.annotations.Data;
import shcm.shsupercm.data.data.IData;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Generates(upon compilation) {@link DataTypeHandler} for {@link IData} classes annotated with @{@link Data}.
 */
public class DataAnnotationProcessor extends AbstractProcessor {
    /**
     * The package in which to generate the handlers.
     */
    public static final String GENERATED_CLASS_PACKAGE = "shcm.shsupercm.data.data.generation";
    /**
     * The class name(that'll have a number added to it).
     */
    public static final String GENERATED_CLASS_NAME = "DataTypeHandler";

    /**
     * The latest data type handler created by the processor.
     */
    private static long dataTypeNumber = -1;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet<>();
        set.add(Data.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element dataElement : roundEnv.getElementsAnnotatedWith(Data.class)) {
            if (dataElement.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Only classes may be annotated with @Data!", dataElement);
                return true;
            }
            try {
                Data dataAnnotation = dataElement.getAnnotation(Data.class);

                String generatedClassName = GENERATED_CLASS_NAME + ++dataTypeNumber;
                String dataTypeCanonicalName = getCanonicalName(dataElement);

                List<FieldEntry> dataFields = new ArrayList<>();

                for (Element fieldElement : dataElement.getEnclosedElements())
                    if (fieldElement.getKind() == ElementKind.FIELD && !fieldElement.getModifiers().contains(Modifier.FINAL) && fieldElement.getAnnotation(Data.Ignore.class) == null)
                        dataFields.add(new FieldEntry(fieldElement));

                JavaFileObject dataTypeHandlerClass = processingEnv.getFiler().createSourceFile(GENERATED_CLASS_PACKAGE + "." + generatedClassName, dataElement);
                Writer writer = dataTypeHandlerClass.openWriter();

                writer.append("package " + GENERATED_CLASS_PACKAGE + ";\n");
                writer.append("import shcm.shsupercm.data.framework.DataBlock;\n");
                writer.append("import shcm.shsupercm.data.data.IData;\n");
                writer.append("import shcm.shsupercm.data.data.DataAnnotationRegistry.DataTypeHandler;\n");
                writer.append("public class ").append(generatedClassName).append(" extends DataTypeHandler<").append(dataTypeCanonicalName).append("> {\n");
                writer.append("    public Class<").append(dataTypeCanonicalName).append("> getType() { return ").append(dataTypeCanonicalName).append(".class; }\n");
                writer.append("    public ").append(dataTypeCanonicalName).append(" newT() { return new ").append(dataTypeCanonicalName).append("(); }\n");
                writer.append("    public byte[] dataTypeUID() { return new byte[]").append(Arrays.toString(dataAnnotation.value()).replace('[', '{').replace(']', '}')).append("; }\n");
                writer.append("    public DataBlock write(DataBlock dataBlock, ").append(dataTypeCanonicalName).append(" data) {\n");
                for (FieldEntry dataField : dataFields)
                    dataField.write(writer);
                writer.append("        return dataBlock;\n");
                writer.append("    }\n");
                writer.append("    public IData read(DataBlock dataBlock) {\n");
                writer.append("        ").append(dataTypeCanonicalName).append(" data = newT();\n");
                for (FieldEntry dataField : dataFields)
                    dataField.read(writer);
                writer.append("        return data;\n");
                writer.append("    }\n");
                writer.append("}");
                writer.close();
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * Resolves the full canonical name of an element.
     * @param element the element to find the canonical name for.
     * @return the canonical name of the element.
     */
    private String getCanonicalName(Element element) {
        StringBuilder name = new StringBuilder(element.getKind() == ElementKind.PACKAGE ? ((PackageElement) element).getQualifiedName().toString() : element.getSimpleName().toString());

        Element e = element;
        while((e = e.getEnclosingElement()) != null) {
            name.insert(0, (e.getKind() == ElementKind.PACKAGE ? ((PackageElement) e).getQualifiedName().toString() : e.getSimpleName().toString()) + ".");
        }

        return name.toString();
    }

    /**
     * Representation of a field that should be handled automatically.
     */
    @SuppressWarnings({"unused","FieldCanBeLocal"})
    private static class FieldEntry {
        /**
         * Actual element of the field.
         */
        private final Element fieldElement;
        /**
         * The name of the field.
         */
        private final String fieldName;
        /**
         * The name of the data value key that'll be associated with the field.<br>
         * This will be the field name if the field has not specified anything else through @{@link Data.Name}.
         */
        private final String dataName;
        /**
         * The name of the getter method for the field(will default to {@code get}Field).<br>
         * If the field is public and this is null, the handler will use direct field access.
         */
        private final String getter;
        /**
         * The name of the setter method for the field(will default to {@code set}Field).<br>
         * If the field is public and this is null, the handler will use direct field access.
         */
        private final String setter;
        /**
         * Canonical name of the class type of the field.
         */
        private final String fieldType;
        /**
         * If non null, defines the field type as enum and defines storage solution.
         */
        private final Data.Enum.Method enumMethod;
        /**
         * Is the field public.
         */
        private final boolean isPublic;

        /**
         * Processes the field into a field entry.
         */
        FieldEntry(Element fieldElement) {
            this.fieldElement = fieldElement;

            this.fieldType = fieldElement.asType().toString();

            this.fieldName = fieldElement.getSimpleName().toString();
            Data.Name nameAnnotation = fieldElement.getAnnotation(Data.Name.class);
            if(nameAnnotation == null)
                this.dataName = this.fieldName;
            else
                this.dataName = nameAnnotation.value();

            this.isPublic = fieldElement.getModifiers().contains(Modifier.PUBLIC);

            Data.Enum enumMethod = fieldElement.getAnnotation(Data.Enum.class);
            if(enumMethod == null)
                this.enumMethod = null;
            else
                this.enumMethod = enumMethod.value();

            Data.Access fieldAccess = fieldElement.getAnnotation(Data.Access.class);
            if(fieldAccess == null) {
                if(!this.isPublic) {
                    String etField = "et" + this.fieldName.substring(0, 1).toUpperCase() + this.fieldName.substring(1);
                    this.getter = 'g' + etField;
                    this.setter = 's' + etField;
                } else {
                    this.getter = null;
                    this.setter = null;
                }
            } else {
                this.getter = fieldAccess.getter();
                this.setter = fieldAccess.setter();
            }
        }

        /**
         * Writes the line of the field into the "write" method.
         */
        void write(Writer writer) throws IOException {
            writer.append("        ");
            if(enumMethod != null) {
                writer.append("if (");
                writeValue(writer);
                writer.append(" != null) ");
            }
            writer.append("dataBlock.set(\"").append(dataName).append("\", ");
            writeValue(writer);
            if(enumMethod == Data.Enum.Method.NAME)
                writer.append(".name()");
            if(enumMethod == Data.Enum.Method.ORDINAL)
                writer.append(".ordinal()");
            writer.append(");\n");
        }

        /**
         * Appends the {@link IData} field value reference.
         */
        void writeValue(Writer writer) throws IOException {
            writer.append("data.");
            if(this.getter == null)
                writer.append(this.fieldName);
            else
                writer.append(this.getter).append("()");
        }

        /**
         * Writes the line of the field into the "read" method.
         */
        void read(Writer writer) throws IOException {
            writer.append("        if (dataBlock.exists(\"").append(dataName).append("\")) data.");

            if(this.setter == null) {
                writer.append(this.fieldName).append(" = ");
                readValue(writer);
            } else {
                writer.append(this.setter).append("(");
                readValue(writer);
                writer.append(")");
            }
            writer.append(";\n");
        }

        /**
         * Appends the data block value reference.
         */
        private void readValue(Writer writer) throws IOException {
            if(enumMethod == null) {
                writer.append("(").append(this.fieldType).append(") dataBlock.get(\"").append(this.dataName).append("\")");
            } else if(enumMethod == Data.Enum.Method.NAME) {
                writer.append(this.fieldType).append(".valueOf((java.lang.String)").append("dataBlock.get(\"").append(this.dataName).append("\"))");
            } else if(enumMethod == Data.Enum.Method.ORDINAL) {
                writer.append(this.fieldType).append(".values()[((int)").append("dataBlock.get(\"").append(this.dataName).append("\"))]");
            }
        }
    }
}
