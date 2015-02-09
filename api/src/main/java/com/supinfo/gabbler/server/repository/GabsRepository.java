package com.supinfo.gabbler.server.repository;

import com.supinfo.gabbler.server.entity.Gabs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface GabsRepository extends JpaRepository<Gabs, Long>, JpaSpecificationExecutor {
    public List<Gabs> findByUserId(Long userId);
}
