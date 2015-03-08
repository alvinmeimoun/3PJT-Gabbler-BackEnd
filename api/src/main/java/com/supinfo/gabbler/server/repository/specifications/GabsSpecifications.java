package com.supinfo.gabbler.server.repository.specifications;

import com.supinfo.gabbler.server.entity.Gabs;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class GabsSpecifications {

    public static Specification<Gabs> globalTimeline(final Set<Long> userIDs){
        return (root, query, builder) -> root.get("userId").in(userIDs);
    }

}
