package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author H0n0riuss
 */
@Deprecated
public class OrderBy {
    private static final Pattern ORDER_BY = Pattern.compile("(OrderBy|Order)");
    private static final Pattern DIRECTION = Pattern.compile("(ASC|DESC|Asc|Desc)");
    private final List<String> attributesToOrderBy = new ArrayList<>();
    private boolean isAsc = false; //TODO prüfen was der Standard ist

    public OrderBy(TypeElement typeElement) {
        this(typeElement.getSimpleName().toString());
    }

    public OrderBy(String source) {
        Assert.notNull(source, "Source can`t be null.");
        if (!ORDER_BY.matcher(source).find()) {
            return;
        }
        extractOrderBy(source);
    }

    public Optional<List<String>> getAttributesToOrderBy() {
        if (this.attributesToOrderBy.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(attributesToOrderBy);
    }

    public boolean isAsc() {
        return isAsc;
    }

    private void extractOrderBy(String source) {
        var orderBy = source
                .split("(OrderBy|Order)")[1]
                .split("\\(")[0]; //TODO das mit den anderen Keywörtern machen --> so kann es nur am Ende stehen!
        orderBy = findDirection(orderBy);

        var split = orderBy.split("(?=\\p{Lu})");

        this.attributesToOrderBy.addAll(Arrays.asList(split));
    }

    private String findDirection(String orderBy) {
        if (!DIRECTION.matcher(orderBy).find()) {
            return orderBy;
        }

        var asc = "(ASC|Asc)";
        var desc = "(DESC|Desc)";
        if (Pattern.compile(asc).matcher(orderBy).find()) {
            orderBy = orderBy.split(asc)[0];
            this.isAsc = true;
        } else if (Pattern.compile(desc).matcher(orderBy).find()) {
            orderBy = orderBy.split(desc)[0];
        }

        return orderBy;
    }
}
