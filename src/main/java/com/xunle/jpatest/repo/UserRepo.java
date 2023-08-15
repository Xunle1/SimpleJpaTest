package com.xunle.jpatest.repo;

import com.xunle.jpatest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User getByUsername(String username);

    User findByUsername(String username);

    @Query(value = "select id, username, password from user where username = :username",nativeQuery = true)
    User nativeSqlSelectByUsername(String username);
}
