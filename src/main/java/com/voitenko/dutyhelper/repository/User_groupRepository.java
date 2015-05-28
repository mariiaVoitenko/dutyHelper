package com.voitenko.dutyhelper.repository;

import com.voitenko.dutyhelper.domain.User_group;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the User_group entity.
 */
public interface User_groupRepository extends JpaRepository<User_group,Long> {

}
