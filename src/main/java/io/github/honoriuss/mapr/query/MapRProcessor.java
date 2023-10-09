package io.github.honoriuss.mapr.query;

import com.google.auto.service.AutoService;
import io.github.honoriuss.mapr.query.annotations.Repository;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

/**
 * @author H0n0riuss
 */
@SupportedAnnotationTypes("io.github.honoriuss.mapr.query.annotations.*")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(MapRProcessor.class)
public class MapRProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Repository.class)) {
            if (element.getKind() == ElementKind.INTERFACE) {
                generateImplementation((TypeElement) element);
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Only interfaces can be annotated with @Repository", element);
            }
        }
        return true;
    }

    private void generateImplementation(TypeElement interfaceElement) {
        String generatedClassName = interfaceElement.getSimpleName() + "Impl";
        String packageName = processingEnv.getElementUtils().getPackageOf(interfaceElement).getQualifiedName().toString();

        StringBuilder generatedCode = new StringBuilder();
        implementHead(interfaceElement, generatedClassName, packageName, generatedCode);
        implementAttributes(interfaceElement, generatedCode);

        // Generiere Implementierungen für jede Methode im Interface
        for (Element enclosedElement : interfaceElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD) {
                generateMethod(generatedCode, (ExecutableElement) enclosedElement);
            }
        }

        generatedCode.append("}\n");

        writeImplementedClass(interfaceElement, generatedClassName, packageName, generatedCode);
    }

    private void writeImplementedClass(TypeElement interfaceElement, String generatedClassName, String packageName, StringBuilder generatedCode) {
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(packageName + "." + generatedClassName, interfaceElement);
            try (Writer writer = sourceFile.openWriter()) {
                writer.write(generatedCode.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void implementHead(TypeElement interfaceElement, String generatedClassName, String packageName, StringBuilder generatedCode) {
        generatedCode.append("package ")
                .append(packageName)
                .append(";\n\n");
        generatedCode.append("import org.springframework.stereotype.Component;\n"); //TODO add imports for class
        generatedCode.append("@Component\n");
        generatedCode.append("public class ")
                .append(generatedClassName)
                .append(" implements ")
                .append(interfaceElement.getSimpleName())
                .append(" {\n\n");
    }

    private void implementAttributes(TypeElement interfaceElement, StringBuilder generatedCode) {
        List<? extends VariableElement> parameters = ((ExecutableElement) interfaceElement).getParameters();
        for (VariableElement parameter : parameters) {
            String parameterType = parameter.asType().toString();
            String parameterName = parameter.getSimpleName().toString();
            generatedCode.append("    final ").append(parameterType).append(parameterName).append(";\n");
            //TODO hier weiter machen
        }
    }

    private void generateMethod(StringBuilder generatedCode, ExecutableElement enclosedElement) {
        generatedCode.append("    @Override\n");
        generatedCode.append("    public ")
                .append(enclosedElement.getReturnType())
                .append(" ")
                .append(enclosedElement.getSimpleName())
                .append("(");

        boolean firstParam = true;
        for (Element paramElement : enclosedElement.getParameters()) {
            if (!firstParam) {
                generatedCode.append(", ");
            }
            generatedCode.append(paramElement.asType()).append(" ").append(paramElement.getSimpleName());
            firstParam = false;
        }

        generatedCode.append(") {\n");
        generatedCode.append("        System.out.println(\"Method ").append(enclosedElement.getSimpleName())
                .append(" is called.\");\n");
        if (!enclosedElement.getReturnType().toString().equals("void")) {
            generatedCode.append(createReturnMethod(enclosedElement));
        }
        generatedCode.append("    }\n\n");
    }

    private String createReturnMethod(ExecutableElement enclosedElement) {
        var appendCode =
                "        try (DocumentStore store = connection.getStore(dbPath)) {\n" +
                "            return store.findById(_id).toJavaBean(clazz);\n" +
                "        }\n";
        return appendCode;
    }
}
