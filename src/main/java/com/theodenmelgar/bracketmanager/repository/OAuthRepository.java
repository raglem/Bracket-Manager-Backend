package com.theodenmelgar.bracketmanager.repository;

import com.theodenmelgar.bracketmanager.model.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthRepository extends JpaRepository<OAuthUser, String> {
}
