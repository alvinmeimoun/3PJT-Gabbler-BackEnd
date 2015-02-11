package com.supinfo.gabbler.server.repository.specifications;

import com.supinfo.gabbler.server.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> userEqualEmail(final String email){
        return (root, query, builder) -> builder.equal(builder.lower(root.<String>get("email")), email.toLowerCase());
    }

    public static Specification<User> userLikeDisplayNameOrNickname(final String req){
        return (root, query, builder) -> builder.or(builder.like(builder.lower(root.get("displayName")), "%"+req.toLowerCase()+"%"), builder.like(builder.lower(root.get("nickname")), "%"+req.toLowerCase()+"%"));
    }
}
