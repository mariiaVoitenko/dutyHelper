package com.voitenko.dutyhelper.web.rest;

import com.voitenko.dutyhelper.Application;
import com.voitenko.dutyhelper.domain.Membership;
import com.voitenko.dutyhelper.repository.MembershipRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MembershipResource REST controller.
 *
 * @see MembershipResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MembershipResourceTest {


    @Inject
    private MembershipRepository membershipRepository;

    private MockMvc restMembershipMockMvc;

    private Membership membership;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MembershipResource membershipResource = new MembershipResource();
        ReflectionTestUtils.setField(membershipResource, "membershipRepository", membershipRepository);
        this.restMembershipMockMvc = MockMvcBuilders.standaloneSetup(membershipResource).build();
    }

    @Before
    public void initTest() {
        membership = new Membership();
    }

    @Test
    @Transactional
    public void createMembership() throws Exception {
        int databaseSizeBeforeCreate = membershipRepository.findAll().size();

        // Create the Membership
        restMembershipMockMvc.perform(post("/api/memberships")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(membership)))
                .andExpect(status().isCreated());

        // Validate the Membership in the database
        List<Membership> memberships = membershipRepository.findAll();
        assertThat(memberships).hasSize(databaseSizeBeforeCreate + 1);
        Membership testMembership = memberships.get(memberships.size() - 1);
    }

    @Test
    @Transactional
    public void getAllMemberships() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get all the memberships
        restMembershipMockMvc.perform(get("/api/memberships"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(membership.getId().intValue())));
    }

    @Test
    @Transactional
    public void getMembership() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

        // Get the membership
        restMembershipMockMvc.perform(get("/api/memberships/{id}", membership.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(membership.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMembership() throws Exception {
        // Get the membership
        restMembershipMockMvc.perform(get("/api/memberships/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMembership() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

		int databaseSizeBeforeUpdate = membershipRepository.findAll().size();

        // Update the membership
        restMembershipMockMvc.perform(put("/api/memberships")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(membership)))
                .andExpect(status().isOk());

        // Validate the Membership in the database
        List<Membership> memberships = membershipRepository.findAll();
        assertThat(memberships).hasSize(databaseSizeBeforeUpdate);
        Membership testMembership = memberships.get(memberships.size() - 1);
    }

    @Test
    @Transactional
    public void deleteMembership() throws Exception {
        // Initialize the database
        membershipRepository.saveAndFlush(membership);

		int databaseSizeBeforeDelete = membershipRepository.findAll().size();

        // Get the membership
        restMembershipMockMvc.perform(delete("/api/memberships/{id}", membership.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Membership> memberships = membershipRepository.findAll();
        assertThat(memberships).hasSize(databaseSizeBeforeDelete - 1);
    }
}
