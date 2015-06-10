package com.supinfo.gabbler.server.repository;

import com.supinfo.gabbler.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor {

    User findByNickname(String nickname);
    List<User> findByEmailOrNickname(String email, String nickname);
}
