package com.mistervoiture.auth.repository;

import com.mistervoiture.auth.entity.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationRepository extends JpaRepository<Validation, String> {

    Optional<Validation> findAuthByUsername(String username);

    @Modifying
    @Query("UPDATE Validation valid SET valid.code = :code WHERE valid.username = :username")
    void updateCodeValidation(@Param("username") String username, @Param("code") String code);

    @Modifying
    @Query("UPDATE Validation valid SET valid.activation = true WHERE valid.username = :username")
    void updateStatusValidation(@Param("username") String username);
}
