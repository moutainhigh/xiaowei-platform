package com.xiaowei.core.query.yuanxuan.jpa.specification;

import javax.persistence.criteria.*;

public class NotEqualSpecification<T> extends AbstractSpecification<T> {
    private final String property;
    private final Object[] values;

    public NotEqualSpecification(String property, Object... values) {
        this.property = property;
        this.values = values;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From from = getRoot(property, root);
        String field = getProperty(property);
        if (values == null) {
            return cb.isNotNull(getFieldPath(from,field));
        }
        if (values.length == 1) {
            return getPredicate(from, cb, values[0], field);
        }
        Predicate[] predicates = new Predicate[values.length];
        for (int i = 0; i < values.length; i++) {
            predicates[i] = getPredicate(root, cb, values[i], field);
        }
        return cb.or(predicates);
    }

    private Predicate getPredicate(From root, CriteriaBuilder cb, Object value, String field) {
        return value == null ? cb.isNotNull(getFieldPath(root,field)) : cb.notEqual(getFieldPath(root,field), value);
    }
}

