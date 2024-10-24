package com.vuviet.ThuongMai.repository;

import com.vuviet.ThuongMai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User getByEmail(String email);

    boolean existsByEmail(String email);

    User findByRefreshTokenAndEmail(String Token, String email);
}
