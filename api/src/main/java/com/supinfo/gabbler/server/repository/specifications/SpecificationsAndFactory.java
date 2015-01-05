package com.supinfo.gabbler.server.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * @author Alvin Meimoun
 */
 public class SpecificationsAndFactory<T> {

     List<Specification<T>> specifications = new ArrayList<>();

    public SpecificationsAndFactory add(Specification<T> spec) {
        specifications.add(spec);
        return this;
    }

    public SpecificationsAndFactory<T> addAll(List<Specification<T>> specList) {
        specifications.addAll(specList);
        return this;
    }

    @SuppressWarnings("unchecked")
    public Specifications<T> build() {

        if (CollectionUtils.isEmpty(specifications))
            return null;

        Specifications<T> globalSpecificationApplicant = where(specifications.get(0));
        for (int i = 1; i < specifications.size(); i++) {
            globalSpecificationApplicant = globalSpecificationApplicant.and(specifications.get(i));
        }
        return globalSpecificationApplicant;
    }
 }