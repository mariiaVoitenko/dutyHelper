package com.voitenko.dutyhelper.repository;

import com.voitenko.dutyhelper.domain.Priority;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Priority entity.
 */
public interface PriorityRepository extends JpaRepository<Priority,Long> {

}
