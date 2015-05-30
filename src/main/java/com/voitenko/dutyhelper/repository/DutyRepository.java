package com.voitenko.dutyhelper.repository;

import com.voitenko.dutyhelper.domain.Duty;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Duty entity.
 */
public interface DutyRepository extends JpaRepository<Duty,Long> {

}
