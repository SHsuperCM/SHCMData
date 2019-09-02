package shcm.shsupercm.data.data.generation;

import shcm.shsupercm.data.data.DataAnnotationRegistry;
import shcm.shsupercm.data.data.annotations.Data;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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


                /*for (Element fieldElement : dataElement.getEnclosedElements()) {
                    if(fieldElement.getAnnotation(Data.Ignore.class) == null) {
                        String fieldName = fieldElement.getSimpleName().toString();
                        String name = fieldName;
                        Data.Name nameAnnotation = fieldElement.getAnnotation(Data.Name.class);
                        if(nameAnnotation != null)
                            name = nameAnnotation.value();

                    }
                }*/

                JavaFileObject dataTypeHandlerClass = processingEnv.getFiler().createSourceFile(GENERATED_CLASS_PACKAGE + "." + generatedClassName, dataElement);
                Writer writer = dataTypeHandlerClass.openWriter();

                writer.append("package " + GENERATED_CLASS_PACKAGE + ";\n");
                writer.append("public class " + generatedClassName + " extends " + DataAnnotationRegistry.DataTypeHandler.class.getCanonicalName() + "<" + dataTypeCanonicalName + "> {\n");
                writer.append("    public Class<" + dataTypeCanonicalName + "> getType() { return " + dataTypeCanonicalName + ".class; }\n");
                writer.append("    public " + dataTypeCanonicalName + " newT() { return new " + dataTypeCanonicalName + "(); }\n");
                writer.append("    public byte[] dataTypeUID() { return new byte[]" + Arrays.toString(dataAnnotation.value()).replace('[','{').replace(']','}') + "; }\n");
                writer.append("    public shcm.shsupercm.data.framework.DataBlock write(shcm.shsupercm.data.framework.DataBlock dataBlock) {\n");

                writer.append("        return dataBlock;\n");
                writer.append("    }\n");
                writer.append("    public shcm.shsupercm.data.data.IData read(shcm.shsupercm.data.framework.DataBlock dataBlock) {\n");
                writer.append("        " + dataTypeCanonicalName + " data = new " + dataTypeCanonicalName + "();\n");

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
}
