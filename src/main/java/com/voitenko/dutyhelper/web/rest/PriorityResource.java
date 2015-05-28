package com.voitenko.dutyhelper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.voitenko.dutyhelper.domain.Priority;
import com.voitenko.dutyhelper.repository.PriorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Priority.
 */
@RestController
@RequestMapping("/api")
public class PriorityResource {

    private final Logger log = LoggerFactory.getLogger(PriorityResource.class);

    @Inject
    private PriorityRepository priorityRepository;

    /**
     * POST  /prioritys -> Create a new priority.
     */
    @RequestMapping(value = "/prioritys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Priority priority) throws URISyntaxException {
        log.debug("REST request to save Priority : {}", priority);
        if (priority.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new priority cannot already have an ID").build();
        }
        priorityRepository.save(priority);
        return ResponseEntity.created(new URI("/api/prioritys/" + priority.getId())).build();
    }

    /**
     * PUT  /prioritys -> Updates an existing priority.
     */
    @RequestMapping(value = "/prioritys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Priority priority) throws URISyntaxException {
        log.debug("REST request to update Priority : {}", priority);
        if (priority.getId() == null) {
            return create(priority);
        }
        priorityRepository.save(priority);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /prioritys -> get all the prioritys.
     */
    @RequestMapping(value = "/prioritys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Priority> getAll() {
        log.debug("REST request to get all Prioritys");
        return priorityRepository.findAll();
    }

    /**
     * GET  /prioritys/:id -> get the "id" priority.
     */
    @RequestMapping(value = "/prioritys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Priority> get(@PathVariable Long id) {
        log.debug("REST request to get Priority : {}", id);
        return Optional.ofNullable(priorityRepository.findOne(id))
            .map(priority -> new ResponseEntity<>(
                priority,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prioritys/:id -> delete the "id" priority.
     */
    @RequestMapping(value = "/prioritys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Priority : {}", id);
        priorityRepository.delete(id);
    }
}
