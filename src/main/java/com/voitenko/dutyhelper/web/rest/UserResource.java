package com.voitenko.dutyhelper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.voitenko.dutyhelper.domain.Membership;
import com.voitenko.dutyhelper.domain.User;
import com.voitenko.dutyhelper.repository.MembershipRepository;
import com.voitenko.dutyhelper.repository.UserRepository;
import com.voitenko.dutyhelper.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private MembershipRepository membershipRepository;

    @RequestMapping(value = "/users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> getAll() {
        log.debug("REST request to get all Users");
        return userRepository.findAll();
    }

    @RequestMapping(value = "/users/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<User> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userRepository.findOneByLogin(login)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/users/id/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.debug("REST request to get User : {}", id);
        return new ResponseEntity<>(userRepository.findOne(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/users/email",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<User> getUserByEmail(@RequestParam("email") String email) {
        log.debug("REST request to get User : {}", email);
        return userRepository.findOneByEmail(email)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/users/validate/{login}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    String getPassword(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        ResponseEntity<User> u = userRepository.findOneByLogin(login)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        return u.getBody().getPassword();
    }
}
