package com.skndan.repo;

import com.skndan.entity.Paged;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;

import java.util.List;
import java.util.Map;

public abstract class BaseRepo<T, ID> implements PanacheRepositoryBase<T, ID> {

    public Paged<T> findAllPaged(int page, int size) {
        PanacheQuery<T> query = findAll();
        long total = query.count();
        List<T> results = query.page(Page.of(page, size)).list();
        return new Paged<>(results, total, page, size);
    }

    // Positional parameters
    public Paged<T> findPaged(String query, int page, int size, Object... params) {
        PanacheQuery<T> q = find(query, params);
        long total = q.count();
        List<T> results = q.page(Page.of(page, size)).list();
        return new Paged<>(results, total, page, size);
    }

    // Named parameters
    public Paged<T> findPaged(String query, Map<String, Object> params, int page, int size) {
        PanacheQuery<T> q = find(query, params);
        long total = q.count();
        List<T> results = q.page(Page.of(page, size)).list();
        return new Paged<>(results, total, page, size);
    }
}