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
    private static final String generatedPackage = "shcm.shsupercm.data.data.generation";

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
        //if(!roundEnv.processingOver()) return false;
        messager = processingEnv.getMessager();
        for (Element dataElement : roundEnv.getElementsAnnotatedWith(Data.class)) {
            if (dataElement.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Only classes may be annotated with @Data!", dataElement);
                return true;
            }
            try {


                Data dataAnnotation = dataElement.getAnnotation(Data.class);

                String generatedClassName = "DataTypeHandler" + ++dataTypeNumber;
                String dataTypeCanonicalName = getCanonicalName(dataElement);

                JavaFileObject dataTypeHandlerClass = processingEnv.getFiler().createSourceFile(generatedPackage + "." + generatedClassName, dataElement);
                Writer writer = dataTypeHandlerClass.openWriter();

                for (Element fieldElement : dataElement.getEnclosedElements()) {
                    if(fieldElement.getAnnotation(Data.Ignore.class) == null) {
                        String fieldName = fieldElement.getSimpleName().toString();
                        String name = fieldName;
                        Data.Name nameAnnotation = fieldElement.getAnnotation(Data.Name.class);
                        if(nameAnnotation != null)
                            name = nameAnnotation.value();

                    }
                }
                Element enclosing = dataElement;
                while (enclosing.getKind() != ElementKind.PACKAGE) {
                    enclosing = enclosing.getEnclosingElement();
                }
                PackageElement packageElement = (PackageElement) enclosing;

                writer.append("package " + generatedPackage + ";\n");
                writer.append("public class " + generatedClassName + " extends " + DataAnnotationRegistry.DataTypeHandler.class.getCanonicalName() + "<" + dataTypeCanonicalName + "> {\n");
                writer.append("    @Override public Class<" + dataTypeCanonicalName + "> getType() { return " + dataTypeCanonicalName + ".class; }\n");
                writer.append("    @Override public byte[] dataTypeUID() { return new byte[]" + Arrays.toString(dataAnnotation.value()).replace('[','{').replace(']','}') + "; }\n");
                writer.append("}");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        }
        return true;
    }

    private String getCanonicalName(Element element) {
        if(element == null)
            return "";
        return getCanonicalName(element.getEnclosingElement()) + (element.getKind() == ElementKind.PACKAGE ? (((PackageElement) element).getQualifiedName()) : "." + element.getSimpleName());
    }
}
