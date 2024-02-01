package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.query.enums.EQueryType;
import io.github.honoriuss.mapr.utils.Assert;
import io.github.honoriuss.mapr.utils.StringUtils;

import javax.lang.model.element.TypeElement;
import java.util.*;
import java.util.regex.Pattern;

import static io.github.honoriuss.mapr.query.parser.Part.Type.fromProperty;

/**
 * @author H0n0riuss
 * <p>
 * Use queryPartModelList()
 */
@Deprecated
public class QueryPart {
    private final Pattern BY = Pattern.compile("By");
    private final List<String> queryPartStringList;
    private final Class<?> clazz;
    private final List<Object> eQueryPartList;
    private final List<QueryTypeModel> queryPartModelList;
    private final List<String> columnList;
    private final List<String> argList;

    public QueryPart(TypeElement typeElement, Class<?> clazz) {
        this(typeElement.getSimpleName().toString() + "(" + typeElement.getTypeParameters() + ")", clazz);
    }

    public QueryPart(String source, Class<?> clazz) {
        Assert.notNull(source, "Source cant be null");
        this.clazz = clazz;

        if (!hasQueryPart(source)) {
            this.queryPartStringList = null;
            this.eQueryPartList = null;
            this.queryPartModelList = null;
            this.columnList = null;
            this.argList = null;
            return;
        }

        var methodName = extractMethodNameAfterBy(source);
        this.argList = createAttributeList(methodName);
        this.columnList = new ArrayList<>();
        createQueryPartsList(methodName);
        this.queryPartStringList = extractQueryParts(methodName);
        this.eQueryPartList = createEQueryList();
        this.queryPartModelList = createQueryPartModel();
    }

    private List<String> createAttributeList(String methodName) {
        methodName = methodName.split("\\(")[1].split("\\)")[0];
        var resList = new ArrayList<String>();
        var split = methodName.split(",");
        for (var s : split) {
            s = s.trim();
            resList.add(s.split(" ")[1].trim());
        }
        return resList;
    }

    public Optional<List<String>> getQueryTypeStringList() {
        if (queryPartStringList == null) {
            return Optional.empty();
        }
        return Optional.of(queryPartStringList);
    }

    public Optional<List<Object>> getEQueryTypeList() {
        if (eQueryPartList == null) {
            return Optional.empty();
        }
        return Optional.of(eQueryPartList);
    }

    public Optional<List<QueryTypeModel>> getQueryTypeModelList() {
        if (this.queryPartModelList == null) {
            return Optional.empty();
        }
        return Optional.of(queryPartModelList);
    }

    public Optional<List<String>> getColumnList() {
        if (this.columnList == null) {
            return Optional.empty();
        }
        return Optional.of(columnList);
    }

    public Optional<List<String>> getArgList() {
        if (this.argList == null) {
            return Optional.empty();
        }
        return Optional.of(argList);
    }

    private void createQueryPartsList(String source) {
        var classAttributes = new ArrayList<String>();
        addOptionalClassAttributes(classAttributes);
        var keywords = extractQueryParts(source);
        for (var keyword : keywords) {
            if (classAttributes.contains(keyword)) {
                var lowerKeyword = Character.toLowerCase(keyword.charAt(0)) + keyword.substring(1);
                this.columnList.add(lowerKeyword);
            }
        }
    }

    private List<Object> createEQueryList() {
        var size = this.queryPartStringList.size();
        var result = new ArrayList<>(size);
        var keywords = new ArrayList<>(EQueryType.ALL_KEYWORDS);
        var pattern = Pattern.compile(String.join("|", keywords));

        for (int i = 0; i < size; ++i) {
            var queryPart = this.queryPartStringList.get(i);

            if (pattern.matcher(queryPart).find()) {
                var eQueryPart = fromProperty(queryPart);
                result.add(eQueryPart); //TODO Klasse erstellen, die den QueryPart hat und die attribute (String) dazu
                for (int j = 1; j <= eQueryPart.getNumberOfArguments(); ++j) { //TODO validation
                    result.add(this.queryPartStringList.get(i - j));
                }
            }
        }
        return result;
    }

    private List<QueryTypeModel> createQueryPartModel() {
        var size = this.queryPartStringList.size();
        var result = new ArrayList<QueryTypeModel>(size);
        var keywords = new ArrayList<>(EQueryType.ALL_KEYWORDS);
        var pattern = Pattern.compile(String.join("|", keywords));
        var argIndex = 0;
        var columnIndex = 0;

        for (int i = 0; i < size; ++i) {
            var queryPart = this.queryPartStringList.get(i);

            if (pattern.matcher(queryPart).find()) {
                var eQueryPart = EQueryType.fromProperty(queryPart);
                if (columnIndex >= this.columnList.size()) {
                    --columnIndex;
                }
                var columnName = this.columnList.get(columnIndex++);
                var queryPartModel = new QueryTypeModel(eQueryPart, columnName);
                result.add(queryPartModel);
                for (int j = 0; j < eQueryPart.getNumberOfArguments(); ++argIndex, ++j) {
                    queryPartModel.addQueryAttribute(this.argList.get(j + argIndex));
                }
            }
        }
        return result;
    }

    private String extractMethodNameAfterBy(String source) {
        return source.split("By", 2)[1];
    }

    private List<String> extractQueryParts(String source) {
        var resultList = new ArrayList<String>();
        source = source.split("\\(")[0];
        Assert.hasText(source, "there should be a QueryPart");
        var keywords = new ArrayList<>(EQueryType.ALL_KEYWORDS);

        addOptionalClassAttributes(keywords);

        do {
            var hasKeyword = false;
            for (var keyword : keywords) {
                if (!source.startsWith(keyword)) {
                    continue;
                }
                source = source.substring(keyword.length());
                resultList.add(keyword);
                hasKeyword = true;
                break;
            }
            if (!hasKeyword) {
                throw new IllegalArgumentException("No keyword or class attribute in source");
            }
        } while (!source.isEmpty());

        return resultList;
    }

    private void addOptionalClassAttributes(Collection<String> collection) {
        if (this.getClazz().isEmpty()) {
            return;
        }
        var attributes = StringUtils.getAttributesFromClass(this.clazz);
        for (var attribute : attributes) {
            var modifiedAttribute = Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
            collection.add(modifiedAttribute);
        }
    }

    private boolean hasQueryPart(String source) {
        return BY.matcher(source).find();
    }

    private Optional<Class<?>> getClazz() {
        return this.clazz == null ? Optional.empty() : Optional.of(this.clazz);
    }
}
