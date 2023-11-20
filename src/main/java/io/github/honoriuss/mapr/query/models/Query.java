package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;

import java.util.Optional;

/**
 * @author H0n0riuss
 */
public class Query {
    private final ReturnType returnType;
    private final Optional<Subject> subject;
    private final Optional<QueryTypes> queryTypes;
    private final Optional<TypeArgs> typeArgs;
    private final Optional<OrderBy> orderBy;

    public Query(String source) {
        Assert.notNull(source, "Source cant be null");

        this.returnType = new ReturnType(source);
        this.subject = Optional.of(new Subject(source));
        this.queryTypes = Optional.of(new QueryTypes(source));
        this.typeArgs = Optional.of(new TypeArgs(source));
        this.orderBy = Optional.of(new OrderBy(source));
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public Optional<Subject> getSubject() {
        return subject;
    }

    public Optional<QueryTypes> getQueryTypes() {
        return queryTypes;
    }

    public Optional<TypeArgs> getTypeArgs() {
        return typeArgs;
    }

    public Optional<OrderBy> getOrderBy() {
        return orderBy;
    }
}
