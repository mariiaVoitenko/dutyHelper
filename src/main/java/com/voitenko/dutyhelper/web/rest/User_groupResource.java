package com.voitenko.dutyhelper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.voitenko.dutyhelper.domain.User_group;
import com.voitenko.dutyhelper.repository.User_groupRepository;
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

/**
 * REST controller for managing User_group.
 */
@RestController
@RequestMapping("/api")
public class User_groupResource {

    private final Logger log = LoggerFactory.getLogger(User_groupResource.class);

    @Inject
    private User_groupRepository user_groupRepository;

    @RequestMapping(value = "/user_groups",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody User_group user_group) throws URISyntaxException {
        log.debug("REST request to save User_group : {}", user_group);
        if (user_group.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new user_group cannot already have an ID").build();
        }
        user_groupRepository.save(user_group);
        return ResponseEntity.created(new URI("/api/user_groups/" + user_group.getId())).build();
    }

    @RequestMapping(value = "/user_groups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody User_group user_group) throws URISyntaxException {
        log.debug("REST request to update User_group : {}", user_group);
        if (user_group.getId() == null) {
            return create(user_group);
        }
        user_groupRepository.save(user_group);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/user_groups",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<User_group>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<User_group> page = user_groupRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user_groups", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/user_groups/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<User_group> get(@PathVariable Long id) {
        log.debug("REST request to get User_group : {}", id);
        return Optional.ofNullable(user_groupRepository.findOne(id))
            .map(user_group -> new ResponseEntity<>(
                user_group,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/user_groups/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete User_group : {}", id);
        user_groupRepository.delete(id);
    }
}
