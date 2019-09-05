package shcm.shsupercm.data.data.generation;

import shcm.shsupercm.data.data.DataAnnotationRegistry;
import shcm.shsupercm.data.data.annotations.Data;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class DataAnnotationProcessor extends AbstractProcessor {
    public static final String GENERATED_CLASS_PACKAGE = "shcm.shsupercm.data.data.generation";
    public static final String GENERATED_CLASS_NAME = "DataTypeHandler";

    private Messager messager;

    private static long dataTypeNumber = -1;

    public DataAnnotationProcessor() {
    }

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
        messager = processingEnv.getMessager();
        for (Element dataElement : roundEnv.getElementsAnnotatedWith(Data.class)) {
            if (dataElement.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Only classes may be annotated with @Data!", dataElement);
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
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getCanonicalName(Element element) {
        StringBuilder name = new StringBuilder(element.getKind() == ElementKind.PACKAGE ? ((PackageElement) element).getQualifiedName().toString() : element.getSimpleName().toString());

        Element e = element;
        while((e = e.getEnclosingElement()) != null) {
            name.insert(0, (e.getKind() == ElementKind.PACKAGE ? ((PackageElement) e).getQualifiedName().toString() : e.getSimpleName().toString()) + ".");
        }

        return name.toString();
    }

    @SuppressWarnings({"unused","FieldCanBeLocal"})
    private static class FieldEntry {
        private final Element fieldElement;
        private final String fieldName, dataName, getter, setter, fieldType;
        private final boolean isPublic;

        FieldEntry(Element fieldElement) {
            this.fieldElement = fieldElement;

            fieldType = fieldElement.asType().toString();

            fieldName = fieldElement.getSimpleName().toString();
            Data.Name nameAnnotation = fieldElement.getAnnotation(Data.Name.class);
            if(nameAnnotation == null)
                dataName = fieldName;
            else
                dataName = nameAnnotation.value();

            isPublic = fieldElement.getModifiers().contains(Modifier.PUBLIC);

            Data.Access fieldAccess = fieldElement.getAnnotation(Data.Access.class);
            if(fieldAccess == null) {
                if(!isPublic) {
                    String etField = "et" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    getter = 'g' + etField;
                    setter = 's' + etField;
                } else {
                    getter = null;
                    setter = null;
                }
            } else {
                getter = fieldAccess.getter();
                setter = fieldAccess.setter();
            }
        }
        void write(Writer writer) throws IOException {
            writer.append("        dataBlock.set(\"").append(dataName).append("\", data.");
            if(getter == null)
                writer.append(fieldName);
            else
                writer.append(getter).append("()");
            writer.append(");\n");
        }
        void read(Writer writer) throws IOException {
            writer.append("        if (dataBlock.exists(\"").append(dataName).append("\")) data.");
            if(setter == null)
                writer.append(fieldName).append(" = (").append(fieldType).append(") dataBlock.get(\"").append(dataName).append("\")");
            else
                writer.append(setter).append("((").append(fieldType).append(") dataBlock.get(\"").append(dataName).append("\"))");
            writer.append(";\n");
        }
    }
}
