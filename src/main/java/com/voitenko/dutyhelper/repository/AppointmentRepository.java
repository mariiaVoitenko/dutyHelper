package com.voitenko.dutyhelper.repository;

import com.voitenko.dutyhelper.domain.Appointment;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Appointment entity.
 */
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    @Query("select appointment from Appointment appointment where appointment.user.login = ?#{principal.username}")
    List<Appointment> findAllForCurrentUser();

}
