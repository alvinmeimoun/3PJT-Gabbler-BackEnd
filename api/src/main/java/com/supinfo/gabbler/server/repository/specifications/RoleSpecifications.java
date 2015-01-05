package com.supinfo.gabbler.server.repository.specifications;

import com.supinfo.gabbler.server.entity.Role;
import org.springframework.data.jpa.domain.Specification;

public class RoleSpecifications {

    public static Specification<Role> roleEqualName(final String name){
        return (root, query, builder) -> builder.equal(root.<String>get("name"), name);
    }
}
