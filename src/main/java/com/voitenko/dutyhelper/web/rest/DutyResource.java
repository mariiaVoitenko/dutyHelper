package com.voitenko.dutyhelper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.voitenko.dutyhelper.domain.Duty;
import com.voitenko.dutyhelper.repository.DutyRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DutyResource {

    private final Logger log = LoggerFactory.getLogger(DutyResource.class);

    @Inject
    private DutyRepository dutyRepository;

    @RequestMapping(value = "/dutys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Duty duty) throws URISyntaxException {
        log.debug("REST request to save Duty : {}", duty);
        if (duty.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new duty cannot already have an ID").build();
        }
        dutyRepository.save(duty);
        return ResponseEntity.created(new URI("/api/dutys/" + duty.getId())).build();
    }

    @RequestMapping(value = "/dutys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Duty duty) throws URISyntaxException {
        log.debug("REST request to update Duty : {}", duty);
        if (duty.getId() == null) {
            return create(duty);
        }
        dutyRepository.save(duty);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/dutys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Duty>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Duty> page = dutyRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dutys", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/dutys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Duty> get(@PathVariable Long id) {
        log.debug("REST request to get Duty : {}", id);
        return Optional.ofNullable(dutyRepository.findOne(id))
            .map(duty -> new ResponseEntity<>(
                duty,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/dutys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Duty : {}", id);
        dutyRepository.delete(id);
    }
}
