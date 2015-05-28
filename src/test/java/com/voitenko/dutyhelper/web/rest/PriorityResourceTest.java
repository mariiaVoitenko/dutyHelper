package com.voitenko.dutyhelper.web.rest;

import com.voitenko.dutyhelper.Application;
import com.voitenko.dutyhelper.domain.Priority;
import com.voitenko.dutyhelper.repository.PriorityRepository;

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
 * Test class for the PriorityResource REST controller.
 *
 * @see PriorityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PriorityResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private PriorityRepository priorityRepository;

    private MockMvc restPriorityMockMvc;

    private Priority priority;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PriorityResource priorityResource = new PriorityResource();
        ReflectionTestUtils.setField(priorityResource, "priorityRepository", priorityRepository);
        this.restPriorityMockMvc = MockMvcBuilders.standaloneSetup(priorityResource).build();
    }

    @Before
    public void initTest() {
        priority = new Priority();
        priority.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPriority() throws Exception {
        int databaseSizeBeforeCreate = priorityRepository.findAll().size();

        // Create the Priority
        restPriorityMockMvc.perform(post("/api/prioritys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(priority)))
                .andExpect(status().isCreated());

        // Validate the Priority in the database
        List<Priority> prioritys = priorityRepository.findAll();
        assertThat(prioritys).hasSize(databaseSizeBeforeCreate + 1);
        Priority testPriority = prioritys.get(prioritys.size() - 1);
        assertThat(testPriority.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllPrioritys() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        // Get all the prioritys
        restPriorityMockMvc.perform(get("/api/prioritys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(priority.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPriority() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        // Get the priority
        restPriorityMockMvc.perform(get("/api/prioritys/{id}", priority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(priority.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPriority() throws Exception {
        // Get the priority
        restPriorityMockMvc.perform(get("/api/prioritys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePriority() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

		int databaseSizeBeforeUpdate = priorityRepository.findAll().size();

        // Update the priority
        priority.setName(UPDATED_NAME);
        restPriorityMockMvc.perform(put("/api/prioritys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(priority)))
                .andExpect(status().isOk());

        // Validate the Priority in the database
        List<Priority> prioritys = priorityRepository.findAll();
        assertThat(prioritys).hasSize(databaseSizeBeforeUpdate);
        Priority testPriority = prioritys.get(prioritys.size() - 1);
        assertThat(testPriority.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deletePriority() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

		int databaseSizeBeforeDelete = priorityRepository.findAll().size();

        // Get the priority
        restPriorityMockMvc.perform(delete("/api/prioritys/{id}", priority.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Priority> prioritys = priorityRepository.findAll();
        assertThat(prioritys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
