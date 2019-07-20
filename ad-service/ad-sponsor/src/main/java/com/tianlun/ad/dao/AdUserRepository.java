package com.tianlun.ad.dao;

import com.tianlun.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdUserRepository extends JpaRepository<AdUser, Long> {

    /**
     * <h2>Find user by username</h2>
     */
    AdUser findByUsername(String username);

}
