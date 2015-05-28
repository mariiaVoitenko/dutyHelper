package com.voitenko.dutyhelper.web.rest;

import com.voitenko.dutyhelper.Application;
import com.voitenko.dutyhelper.domain.User_group;
import com.voitenko.dutyhelper.repository.User_groupRepository;

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
 * Test class for the User_groupResource REST controller.
 *
 * @see User_groupResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class User_groupResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private User_groupRepository user_groupRepository;

    private MockMvc restUser_groupMockMvc;

    private User_group user_group;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        User_groupResource user_groupResource = new User_groupResource();
        ReflectionTestUtils.setField(user_groupResource, "user_groupRepository", user_groupRepository);
        this.restUser_groupMockMvc = MockMvcBuilders.standaloneSetup(user_groupResource).build();
    }

    @Before
    public void initTest() {
        user_group = new User_group();
        user_group.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createUser_group() throws Exception {
        int databaseSizeBeforeCreate = user_groupRepository.findAll().size();

        // Create the User_group
        restUser_groupMockMvc.perform(post("/api/user_groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(user_group)))
                .andExpect(status().isCreated());

        // Validate the User_group in the database
        List<User_group> user_groups = user_groupRepository.findAll();
        assertThat(user_groups).hasSize(databaseSizeBeforeCreate + 1);
        User_group testUser_group = user_groups.get(user_groups.size() - 1);
        assertThat(testUser_group.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllUser_groups() throws Exception {
        // Initialize the database
        user_groupRepository.saveAndFlush(user_group);

        // Get all the user_groups
        restUser_groupMockMvc.perform(get("/api/user_groups"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(user_group.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getUser_group() throws Exception {
        // Initialize the database
        user_groupRepository.saveAndFlush(user_group);

        // Get the user_group
        restUser_groupMockMvc.perform(get("/api/user_groups/{id}", user_group.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(user_group.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUser_group() throws Exception {
        // Get the user_group
        restUser_groupMockMvc.perform(get("/api/user_groups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUser_group() throws Exception {
        // Initialize the database
        user_groupRepository.saveAndFlush(user_group);

		int databaseSizeBeforeUpdate = user_groupRepository.findAll().size();

        // Update the user_group
        user_group.setName(UPDATED_NAME);
        restUser_groupMockMvc.perform(put("/api/user_groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(user_group)))
                .andExpect(status().isOk());

        // Validate the User_group in the database
        List<User_group> user_groups = user_groupRepository.findAll();
        assertThat(user_groups).hasSize(databaseSizeBeforeUpdate);
        User_group testUser_group = user_groups.get(user_groups.size() - 1);
        assertThat(testUser_group.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteUser_group() throws Exception {
        // Initialize the database
        user_groupRepository.saveAndFlush(user_group);

		int databaseSizeBeforeDelete = user_groupRepository.findAll().size();

        // Get the user_group
        restUser_groupMockMvc.perform(delete("/api/user_groups/{id}", user_group.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<User_group> user_groups = user_groupRepository.findAll();
        assertThat(user_groups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
