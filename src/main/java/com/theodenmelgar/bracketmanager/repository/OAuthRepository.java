package com.theodenmelgar.bracketmanager.repository;

import com.theodenmelgar.bracketmanager.enums.OAuthProviderEnum;
import com.theodenmelgar.bracketmanager.model.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthRepository extends JpaRepository<OAuthUser, Long> {
    Optional<OAuthUser> findByProviderAndOAuthId(OAuthProviderEnum provider, String oAuthId);
}
