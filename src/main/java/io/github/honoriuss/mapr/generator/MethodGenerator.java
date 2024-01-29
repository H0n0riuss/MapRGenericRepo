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
    public static MethodSpec generateMethod(ExecutableElement enclosedElement, ProcessingEnvironment processingEnvironment, ClassName entityClassName) {
        var methodName = enclosedElement.getSimpleName().toString();
        var parameterSpecs = ProcessorUtils.getParameterSpecs(enclosedElement, processingEnvironment, entityClassName);

        var argumentStringList = new ArrayList<String>();
        for (var argument : enclosedElement.getParameters()) {
            argumentStringList.add(argument.getSimpleName().toString());
        }

        var returnClass = enclosedElement.getReturnType();
        var returnClassType = ProcessorUtils.getClassType(returnClass);
        if (returnClassType.toString().equals("T")) {
            returnClassType = entityClassName;
        }

        var hasReturnType = enclosedElement.getReturnType().toString().equals(void.class.toString());
        var hasListReturnType = ProcessorUtils.isListType(returnClass.getClass());

        var queryString = getStoreQuery(methodName, argumentStringList, entityClassName, hasReturnType, hasListReturnType);

        return MethodSpec.methodBuilder(
                        methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(returnClassType)
                .addParameters(parameterSpecs)
                .beginControlFlow("try ($T store = connection.getStore(dbPath)) ", DocumentStore.class)
                .addStatement(queryString)
                .endControlFlow()
                .build(); //TODO den Teil wahrscheinlich erst nach der Schleife machen, damit alles andere drinnen richtig erstellt wird
    }

    private static MethodSpec generateReturnMethod(ExecutableElement enclosedElement, ProcessingEnvironment processingEnvironment, ClassName entityClassName) {
        if (enclosedElement.getReturnType().toString().equals("T")) { //TODO auslagern in den QueryCreator
            if (ProcessorUtils.isListType(enclosedElement.getReturnType().getClass())) {
                return generateGenericListTypeReturnMethod(enclosedElement, processingEnvironment, entityClassName);
            }
            return generateGenericListReturnMethod(enclosedElement, processingEnvironment, entityClassName);
        }
        if (ProcessorUtils.isListType(enclosedElement.getReturnType().getClass())) {
            return generateListReturnMethod(enclosedElement, processingEnvironment, entityClassName);
        }


        var methodName = enclosedElement.getSimpleName().toString(); //TODO cut after by and table name if there is something like findByText
        var parameterSpecs = ProcessorUtils.getParameterSpecs(enclosedElement, processingEnvironment, entityClassName);

        var argumentStringList = new ArrayList<String>();
        for (var argument : enclosedElement.getParameters()) {
            argumentStringList.add(argument.getSimpleName().toString());
        }

        var queryString = "";//getStoreQuery(methodName, argumentStringList, entityClassName, true);
        return MethodSpec.methodBuilder(
                        methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameters(parameterSpecs)
                .beginControlFlow("try ($T store = connection.getStore(dbPath)) ", DocumentStore.class)
                .addStatement(queryString)
                //.addCode(AQueryCreator.createQueryStatement(methodName, argumentStringList))
                .endControlFlow()
                .build();
    }

    private static MethodSpec generateListReturnMethod(ExecutableElement enclosedElement, ProcessingEnvironment processingEnvironment, ClassName entityClassName) {
        return null;
    }

    private static MethodSpec generateGenericListReturnMethod(ExecutableElement enclosedElement, ProcessingEnvironment processingEnvironment, ClassName entityClassName) {
        return null;
    }

    private static MethodSpec generateGenericListTypeReturnMethod(ExecutableElement enclosedElement, ProcessingEnvironment processingEnvironment, ClassName entityClassName) {
        return null;
    }

    private static String createQueryConditionString(List<EQueryPart> eQueryPartList, List<EConditionPart> conditionPartList,
                                                     boolean hasReturnType, boolean hasListReturnType) {
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

    private static String getStoreQuery(String methodName, ArrayList<String> argumentStringList, ClassName entityClassName, boolean hasReturnType, boolean hasListReturnType) {
        var split = methodName.split("By", 2);
        var crud = ACrudDecider.getCrudType(methodName);
        if (split.length >= 2) {
            methodName = split[1];
        } else {
            methodName = "";
        }

        var queryConditionModel = AQueryConditionExtractor.extractQueryCondition(methodName, argumentStringList, entityClassName.getClass());

        var queryString = createQueryConditionString(queryConditionModel.eQueryPartList, queryConditionModel.eConditionPartList, hasReturnType, hasListReturnType);

        var findByIdArg = extractArgumentById(argumentStringList);

        var resString = "";

        switch (crud) {
            case CREATE -> resString = ACRUDQueryCreator.getCreateString(argumentStringList, hasReturnType);
            case READ -> resString = ACRUDQueryCreator.getReadString(entityClassName.simpleName(), queryString, hasListReturnType, findByIdArg);
            case UPDATE -> resString = ACRUDQueryCreator.getUpdateString(argumentStringList);
            case DELETE -> resString = ACRUDQueryCreator.getDeleteString(argumentStringList);
        }

        return resString;
    }

    private static String extractArgumentById(ArrayList<String> argumentStringList) {
        if (argumentStringList.size() == 1 && argumentStringList.get(0).toLowerCase().contains("id")) {
            return argumentStringList.get(0);
        }
        return "";
    }

}
