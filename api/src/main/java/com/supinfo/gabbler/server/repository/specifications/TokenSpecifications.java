package com.supinfo.gabbler.server.repository.specifications;

import com.supinfo.gabbler.server.entity.Token;
import org.springframework.data.jpa.domain.Specification;

public class TokenSpecifications {

    public static Specification<Token> tokenEqualUsername(final String username){
        return (root, query, builder) -> builder.equal(root.<String>get("username"), username);
    }
}
