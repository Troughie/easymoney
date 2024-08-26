package com.example.MoneyLover.infra.User.Repository;

import com.example.MoneyLover.infra.User.Entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);

    User findTopByEmail(String email);

    User findTopById(String id);

    @Query("SELECT u FROM User u WHERE u.email = :code OR u.id = :code")
    User findUserByEmailOrId(@Param("code") String code);

    @Query("select u from User u where u.username like %?1% or u.email like %?1%")
    List<User> findAllContain(String id);
}
