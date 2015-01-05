package com.supinfo.gabbler.server.repository;

import com.supinfo.gabbler.server.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TokenRepository extends JpaRepository<Token, String>, JpaSpecificationExecutor {
}
