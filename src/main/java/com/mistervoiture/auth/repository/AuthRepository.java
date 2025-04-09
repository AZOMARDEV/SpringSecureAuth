package com.mistervoiture.auth.repository;

import com.mistervoiture.auth.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, String> {
    Optional<Auth> findAuthByUsername(String username);

    @Query("select count(*) from Auth auth where auth.secretNickname = :nickname")
    int isSecretNicknameUnique(@Param("nickname") String nickname);

    @Query("select count(*) from Auth auth where auth.username = :username")
    int isSecretUsernameUnique(@Param("username") String username);

    @Modifying
    @Query("UPDATE Auth auth SET auth.pwd = :password WHERE auth._id = :_id")
    void updateSecurityPass(@Param("_id") String _id, @Param("password") String password);

}
