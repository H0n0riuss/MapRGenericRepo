package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;

import java.util.Optional;

/**
 * @author H0n0riuss
 */
public class Query {
    private final ReturnType returnType;
    private final Subject subject;
    private final QueryType queryType;
    private final TypeArgs typeArgs;
    private final OrderBy orderBy;

    public Query(String source) {
        this(source, null);
    }

    public Query(String source, Class<?> clazz) {
        Assert.notNull(source, "Source cant be null");

        this.returnType = new ReturnType(source);
        this.subject = new Subject(source);
        this.queryType = new QueryType(source, clazz);
        this.typeArgs = new TypeArgs(source);
        this.orderBy = new OrderBy(source);
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public Optional<Subject> getSubject() {
        return Optional.of(subject);
    }

    public Optional<QueryType> getQueryTypes() {
        return Optional.of(queryType);
    }

    public Optional<TypeArgs> getTypeArgs() {
        return Optional.of(typeArgs);
    }

    public Optional<OrderBy> getOrderBy() {
        return Optional.of(orderBy);
    }
}
