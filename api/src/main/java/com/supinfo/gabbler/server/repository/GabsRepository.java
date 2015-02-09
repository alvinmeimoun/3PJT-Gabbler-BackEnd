package com.supinfo.gabbler.server.repository;

import com.supinfo.gabbler.server.entity.Gabs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GabsRepository extends JpaRepository<Gabs, Integer>, JpaSpecificationExecutor {
}
