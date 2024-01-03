package io.github.honoriuss.mapr.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import org.ojai.store.DocumentStore;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

/**
 * @author H0n0riuss
 */
public abstract class MethodGenerator {
    public static MethodSpec generateMethod(ExecutableElement enclosedElement,
                                            ProcessingEnvironment processingEnvironment,
                                            ClassName entityClassName) {
        if (enclosedElement.getReturnType().toString().equals(void.class.toString())) {
            return generateVoidMethod(enclosedElement, processingEnvironment, entityClassName);
        } else {
            return generateReturnMethod(enclosedElement, processingEnvironment, entityClassName);
        }
    }

    private static MethodSpec generateVoidMethod(ExecutableElement enclosedElement,
                                                 ProcessingEnvironment processingEnvironment,
                                                 ClassName entityClassName) {
        var methodName = enclosedElement.getSimpleName().toString();
        var parameterSpecs = ProcessorUtils.getParameterSpecs(enclosedElement, processingEnvironment, entityClassName);

        return MethodSpec.methodBuilder(
                        methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameters(parameterSpecs)
                .beginControlFlow("try ($T store = connection.getStore(dbPath)) ", DocumentStore.class)
                .addStatement(String.format("store.%s(%s)",
                        CrudDecider.getCrudTranslation(methodName),
                        parameterSpecs.get(0).name))
                .endControlFlow()
                .build(); //TODO den Teil wahrscheinlich erst nach der Schleife machen, damit alles andere drinnen richtig erstellt wird
    }

    private static MethodSpec generateReturnMethod(ExecutableElement enclosedElement, ProcessingEnvironment processingEnvironment, ClassName entityClassName) {
        if (enclosedElement.getReturnType().toString().equals("T")) { //TODO auslagern in den QueryCreator
            if (ProcessorUtils.isListType(enclosedElement.getReturnType().getClass())) {
                return generateListTypeReturnMethod();
            }
            //return generateReturnMethod(enclosedElement, ProcessorUtils.getParameterSpecs(enclosedElement, processingEnvironment, entityClassName)); //TODO den Teil wahrscheinlich erst nach der Schleife machen, damit alles andere drinnen richtig erstellt wird
        }
        return null;
    }

    private static MethodSpec generateReturnMethod() {
        return null;
    }

    private static MethodSpec generateListTypeReturnMethod() {
        return null;
    }
}
