package io.github.honoriuss.mapr.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import org.ojai.store.DocumentStore;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

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
        var methodName = enclosedElement.getSimpleName().toString(); //TODO cut after by and table name if there is something like findByText
        var parameterSpecs = ProcessorUtils.getParameterSpecs(enclosedElement, processingEnvironment, entityClassName);

        var argumentStringList = new ArrayList<String>();
        for (var argument : enclosedElement.getParameters()) {
            argumentStringList.add(argument.getSimpleName().toString());
        }
        var queryConditionModel = AQueryConditionExtractor.extractQueryCondition(methodName, argumentStringList, entityClassName.getClass());

        var queryString = createQueryConditionString(queryConditionModel.eQueryPartList,
                queryConditionModel.eConditionPartList);

        return MethodSpec.methodBuilder(
                        methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameters(parameterSpecs)
                .beginControlFlow("try ($T store = connection.getStore(dbPath)) ", DocumentStore.class)
                .addStatement(queryString)
                .addStatement(String.format("store.%s(%s)", //TODO decide before use this because of query
                        CrudDecider.getCrudTranslation(methodName),
                        parameterSpecs.get(0).name))
                .addCode(AQueryCreator.createQueryStatement(methodName, argumentStringList))
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

    private static String createQueryConditionString(List<EQueryPart> eQueryPartList,
                                                     List<EConditionPart> conditionPartList) {
        if (eQueryPartList.isEmpty()) {
            return "";
        }
        var res = "";
        var hasCondition = !conditionPartList.isEmpty();

        if (hasCondition) {
            res += String.format("org.ojai.store.QueryCondition condition = connection.newCondition()%s",
                    AQueryConditionCreator.createConditionCodeBlock(conditionPartList));
            res += ".build();\n";
        }

        res += String.format("org.ojai.store.Query query = connection.newQuery()%s",
                AQueryConditionCreator.createQueryCodeBlock(eQueryPartList));
        if (hasCondition) {
            res += ".where(condition)";
        }
        return res + ".build();";
    }
}
