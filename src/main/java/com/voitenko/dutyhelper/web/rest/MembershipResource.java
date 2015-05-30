package com.voitenko.dutyhelper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.voitenko.dutyhelper.domain.Membership;
import com.voitenko.dutyhelper.repository.MembershipRepository;
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
 * REST controller for managing Membership.
 */
@RestController
@RequestMapping("/api")
public class MembershipResource {

    private final Logger log = LoggerFactory.getLogger(MembershipResource.class);

    @Inject
    private MembershipRepository membershipRepository;

    /**
     * POST  /memberships -> Create a new membership.
     */
    @RequestMapping(value = "/memberships",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Membership membership) throws URISyntaxException {
        log.debug("REST request to save Membership : {}", membership);
        if (membership.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new membership cannot already have an ID").build();
        }
        membershipRepository.save(membership);
        return ResponseEntity.created(new URI("/api/memberships/" + membership.getId())).build();
    }

    /**
     * PUT  /memberships -> Updates an existing membership.
     */
    @RequestMapping(value = "/memberships",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Membership membership) throws URISyntaxException {
        log.debug("REST request to update Membership : {}", membership);
        if (membership.getId() == null) {
            return create(membership);
        }
        membershipRepository.save(membership);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /memberships -> get all the memberships.
     */
    @RequestMapping(value = "/memberships",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Membership>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Membership> page = membershipRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/memberships", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /memberships/:id -> get the "id" membership.
     */
    @RequestMapping(value = "/memberships/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Membership> get(@PathVariable Long id) {
        log.debug("REST request to get Membership : {}", id);
        return Optional.ofNullable(membershipRepository.findOne(id))
            .map(membership -> new ResponseEntity<>(
                membership,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /memberships/:id -> delete the "id" membership.
     */
    @RequestMapping(value = "/memberships/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Membership : {}", id);
        membershipRepository.delete(id);
    }
}
