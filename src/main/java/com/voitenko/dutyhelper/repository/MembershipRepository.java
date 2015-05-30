package com.voitenko.dutyhelper.repository;

import com.voitenko.dutyhelper.domain.Membership;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Membership entity.
 */
public interface MembershipRepository extends JpaRepository<Membership,Long> {

    @Query("select membership from Membership membership where membership.user.login = ?#{principal.username}")
    List<Membership> findAllForCurrentUser();

}
