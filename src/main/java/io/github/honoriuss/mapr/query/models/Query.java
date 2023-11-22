package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;

import java.util.Optional;

/**
 * @author H0n0riuss
 */
public class Query {
    private final ReturnType returnType;
    private final Subject subject;
    private final QueryTypes queryTypes;
    private final TypeArgs typeArgs;
    private final OrderBy orderBy;

    public Query(String source) {
        Assert.notNull(source, "Source cant be null");

        this.returnType = new ReturnType(source);
        this.subject = new Subject(source);
        this.queryTypes = new QueryTypes(source);
        this.typeArgs = new TypeArgs(source);
        this.orderBy = new OrderBy(source);
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public Optional<Subject> getSubject() {
        return Optional.of(subject);
    }

    public Optional<QueryTypes> getQueryTypes() {
        return Optional.of(queryTypes);
    }

    public Optional<TypeArgs> getTypeArgs() {
        return Optional.of(typeArgs);
    }

    public Optional<OrderBy> getOrderBy() {
        return Optional.of(orderBy);
    }
}
