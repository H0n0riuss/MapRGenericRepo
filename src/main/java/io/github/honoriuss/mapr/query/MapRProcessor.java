package io.github.honoriuss.mapr.query;

import com.google.auto.service.AutoService;
import io.github.honoriuss.mapr.query.annotations.Repository;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * @author H0n0riuss
 */
@SupportedAnnotationTypes("io.github.honoriuss.mapr.annotations.*")
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

        // Generiere Implementierungen für jede Methode im Interface
        for (Element enclosedElement : interfaceElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD) {
                generateMethods(generatedCode, (ExecutableElement) enclosedElement);
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
        generatedCode.append("import org.springframework.stereotype.Component;\n");
        generatedCode.append("@Component\n");
        generatedCode.append("public class ")
                .append(generatedClassName)
                .append(" implements ")
                .append(interfaceElement.getSimpleName())
                .append(" {\n\n");
    }

    private void generateMethods(StringBuilder generatedCode, ExecutableElement enclosedElement) {
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
        generatedCode.append("        // Deine Implementierung hier\n");
        generatedCode.append("    }\n\n");
    }
}
