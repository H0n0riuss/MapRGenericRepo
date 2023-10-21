package io.github.honoriuss.mapr.query.models;

import com.squareup.javapoet.ClassName;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * @author H0n0riuss
 */
public class MetaInformation {
    public String generatedClassName;
    public String packageName;
    public ClassName entityClassName;

    public MetaInformation(TypeElement interfaceElement, ProcessingEnvironment processingEnv) {
        generatedClassName = interfaceElement.getSimpleName() + "Impl";
        packageName = processingEnv.getElementUtils().getPackageOf(interfaceElement).getQualifiedName().toString();
        // Get the generic type parameter
        DeclaredType declaredType = (DeclaredType) interfaceElement.getInterfaces().get(0);
        TypeMirror entityTypeMirror = declaredType.getTypeArguments().get(0);
        entityClassName = ClassName.get((TypeElement) processingEnv.getTypeUtils().asElement(entityTypeMirror));
    }
}
