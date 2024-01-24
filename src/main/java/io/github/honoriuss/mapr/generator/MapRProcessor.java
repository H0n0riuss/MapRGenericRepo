package io.github.honoriuss.mapr.generator;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import io.github.honoriuss.mapr.connections.OjaiConnector;
import io.github.honoriuss.mapr.query.annotations.Repository;
import org.ojai.store.Connection;
import org.springframework.stereotype.Component;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

/**
 * @author H0n0riuss
 */
@SupportedAnnotationTypes("io.github.honoriuss.mapr.generator.annotations.*")
@AutoService(MapRProcessor.class)
public class MapRProcessor extends AbstractProcessor {
    private String packageName;
    private ClassName entityClassName;
    private List<? extends TypeMirror> extendsInterfaces;
    private TypeElement interfaceElement;
    private TypeSpec.Builder classBuilder;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Repository.class)) {
            if (element.getKind() == ElementKind.INTERFACE) {
                initProcessorAttributes((TypeElement) element);
                generateImplementation();
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Only interfaces can be annotated with @Repository", element);
            }
        }
        return true;
    }

    private void initProcessorAttributes(TypeElement interfaceElement) {
        this.packageName = this.processingEnv.getElementUtils().getPackageOf(interfaceElement).getQualifiedName().toString();
        // Get the generic type parameter
        var declaredType = (DeclaredType) interfaceElement.getInterfaces().get(0);
        var entityTypeMirror = declaredType.getTypeArguments().get(0);
        this.entityClassName = ClassName.get((TypeElement) this.processingEnv.getTypeUtils().asElement(entityTypeMirror));
        this.interfaceElement = interfaceElement;
        this.classBuilder = TypeSpec.classBuilder(interfaceElement.getSimpleName() + "ImplB");
    }

    private void generateImplementation() {
        prepareImplementation();
        implementHead();
        implementAttributes();
        implementConstructor();
        implementMethods();
        implementExtendsMethods();
        writeImplementedClass();
    }

    private void prepareImplementation() {
        this.extendsInterfaces = this.interfaceElement.getInterfaces();
    }

    private void implementHead() {
        classBuilder
                .addAnnotation(Component.class)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(TypeName.get(interfaceElement.asType()));
        for (var interfaceToImplement : extendsInterfaces) {
            classBuilder.addSuperinterface(interfaceToImplement);
        }
    }

    private void implementAttributes() {
        Repository repositoryAnnotation = interfaceElement.getAnnotation(Repository.class);
        classBuilder
                .addField(FieldSpec
                        .builder(Connection.class, "connection", Modifier.PRIVATE, Modifier.FINAL)
                        .build())
                .addField(FieldSpec
                        .builder(String.class, "dbPath", Modifier.PRIVATE, Modifier.FINAL)
                        .initializer(String.format("\"%s\"", repositoryAnnotation.tablePath()))
                        .build());
    }

    private void implementConstructor() {
        classBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(OjaiConnector.class, "connection")
                .addStatement("this.connection = connection.getConnection()")
                .build());
    }

    private void implementMethods() {
        for (Element enclosedElement : this.interfaceElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD) {
                classBuilder.addMethod(MethodGenerator.generateMethod((ExecutableElement) enclosedElement, processingEnv, entityClassName));
            }
        }
    }
    private void implementExtendsMethods() {
        for (var t : this.extendsInterfaces) {
            if (t.getKind() == TypeKind.DECLARED) {
                Element element = ((DeclaredType) t).asElement(); //TODO hier weiter machen
                for (var e : element.getEnclosedElements()) {
                    classBuilder.addMethod(MethodGenerator.generateMethod((ExecutableElement) e, processingEnv, entityClassName));
                }
            }
        }
    }

    private void writeImplementedClass() {
        try {
            JavaFile javaFile = JavaFile.builder(this.packageName, this.classBuilder.build())
                    .build();
            javaFile.writeTo(this.processingEnv.getFiler());
        } catch (Exception ex) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
        }
    }
}
