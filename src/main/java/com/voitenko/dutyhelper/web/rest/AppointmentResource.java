package com.voitenko.dutyhelper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.voitenko.dutyhelper.domain.Appointment;
import com.voitenko.dutyhelper.repository.AppointmentRepository;
import com.voitenko.dutyhelper.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Appointment.
 */
@RestController
@RequestMapping("/api")
public class AppointmentResource {

    private final Logger log = LoggerFactory.getLogger(AppointmentResource.class);

    @Inject
    private AppointmentRepository appointmentRepository;

    /**
     * POST  /appointments -> Create a new appointment.
     */
    @RequestMapping(value = "/appointments",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Appointment appointment) throws URISyntaxException {
        log.debug("REST request to save Appointment : {}", appointment);
        if (appointment.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new appointment cannot already have an ID").build();
        }
        appointmentRepository.save(appointment);
        return ResponseEntity.created(new URI("/api/appointments/" + appointment.getId())).build();
    }

    /**
     * PUT  /appointments -> Updates an existing appointment.
     */
    @RequestMapping(value = "/appointments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Appointment appointment) throws URISyntaxException {
        log.debug("REST request to update Appointment : {}", appointment);
        if (appointment.getId() == null) {
            return create(appointment);
        }
        appointmentRepository.save(appointment);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /appointments -> get all the appointments.
     */
    @RequestMapping(value = "/appointments",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Appointment>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Appointment> page = appointmentRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appointments", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /appointments/:id -> get the "id" appointment.
     */
    @RequestMapping(value = "/appointments/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Appointment> get(@PathVariable Long id) {
        log.debug("REST request to get Appointment : {}", id);
        return Optional.ofNullable(appointmentRepository.findOne(id))
            .map(appointment -> new ResponseEntity<>(
                appointment,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /appointments/:id -> delete the "id" appointment.
     */
    @RequestMapping(value = "/appointments/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Appointment : {}", id);
        appointmentRepository.delete(id);
    }
}
