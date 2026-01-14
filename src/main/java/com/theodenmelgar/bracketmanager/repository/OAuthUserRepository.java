package com.theodenmelgar.bracketmanager.repository;

import com.theodenmelgar.bracketmanager.enums.LoginMethodEnum;
import com.theodenmelgar.bracketmanager.model.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthUserRepository extends JpaRepository<OAuthUser, Long> {
    Optional<OAuthUser> findByProviderAndOAuthProviderId(LoginMethodEnum provider, String oAuthId);
}
