package com.voitenko.dutyhelper.web.rest;

import com.voitenko.dutyhelper.Application;
import com.voitenko.dutyhelper.domain.Duty;
import com.voitenko.dutyhelper.repository.DutyRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DutyResource REST controller.
 *
 * @see DutyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DutyResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final DateTime DEFAULT_START_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_START_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.print(DEFAULT_START_DATE);

    private static final DateTime DEFAULT_END_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_END_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.print(DEFAULT_END_DATE);

    private static final Boolean DEFAULT_CAN_CHANGE = false;
    private static final Boolean UPDATED_CAN_CHANGE = true;

    private static final Boolean DEFAULT_IS_DONE = false;
    private static final Boolean UPDATED_IS_DONE = true;

    @Inject
    private DutyRepository dutyRepository;

    private MockMvc restDutyMockMvc;

    private Duty duty;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DutyResource dutyResource = new DutyResource();
        ReflectionTestUtils.setField(dutyResource, "dutyRepository", dutyRepository);
        this.restDutyMockMvc = MockMvcBuilders.standaloneSetup(dutyResource).build();
    }

    @Before
    public void initTest() {
        duty = new Duty();
        duty.setName(DEFAULT_NAME);
        duty.setDescription(DEFAULT_DESCRIPTION);
        duty.setStart_date(DEFAULT_START_DATE);
        duty.setEnd_date(DEFAULT_END_DATE);
        duty.setCan_change(DEFAULT_CAN_CHANGE);
        duty.setIs_done(DEFAULT_IS_DONE);
    }

    @Test
    @Transactional
    public void createDuty() throws Exception {
        int databaseSizeBeforeCreate = dutyRepository.findAll().size();

        // Create the Duty
        restDutyMockMvc.perform(post("/api/dutys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(duty)))
                .andExpect(status().isCreated());

        // Validate the Duty in the database
        List<Duty> dutys = dutyRepository.findAll();
        assertThat(dutys).hasSize(databaseSizeBeforeCreate + 1);
        Duty testDuty = dutys.get(dutys.size() - 1);
        assertThat(testDuty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDuty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDuty.getStart_date().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDuty.getEnd_date().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_END_DATE);
        assertThat(testDuty.getCan_change()).isEqualTo(DEFAULT_CAN_CHANGE);
        assertThat(testDuty.getIs_done()).isEqualTo(DEFAULT_IS_DONE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(dutyRepository.findAll()).hasSize(0);
        // set the field null
        duty.setName(null);

        // Create the Duty, which fails.
        restDutyMockMvc.perform(post("/api/dutys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(duty)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Duty> dutys = dutyRepository.findAll();
        assertThat(dutys).hasSize(0);
    }

    @Test
    @Transactional
    public void checkStart_dateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(dutyRepository.findAll()).hasSize(0);
        // set the field null
        duty.setStart_date(null);

        // Create the Duty, which fails.
        restDutyMockMvc.perform(post("/api/dutys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(duty)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Duty> dutys = dutyRepository.findAll();
        assertThat(dutys).hasSize(0);
    }

    @Test
    @Transactional
    public void checkEnd_dateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(dutyRepository.findAll()).hasSize(0);
        // set the field null
        duty.setEnd_date(null);

        // Create the Duty, which fails.
        restDutyMockMvc.perform(post("/api/dutys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(duty)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Duty> dutys = dutyRepository.findAll();
        assertThat(dutys).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllDutys() throws Exception {
        // Initialize the database
        dutyRepository.saveAndFlush(duty);

        // Get all the dutys
        restDutyMockMvc.perform(get("/api/dutys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(duty.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].start_date").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].end_date").value(hasItem(DEFAULT_END_DATE_STR)))
                .andExpect(jsonPath("$.[*].can_change").value(hasItem(DEFAULT_CAN_CHANGE.booleanValue())))
                .andExpect(jsonPath("$.[*].is_done").value(hasItem(DEFAULT_IS_DONE.booleanValue())));
    }

    @Test
    @Transactional
    public void getDuty() throws Exception {
        // Initialize the database
        dutyRepository.saveAndFlush(duty);

        // Get the duty
        restDutyMockMvc.perform(get("/api/dutys/{id}", duty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(duty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.start_date").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.end_date").value(DEFAULT_END_DATE_STR))
            .andExpect(jsonPath("$.can_change").value(DEFAULT_CAN_CHANGE.booleanValue()))
            .andExpect(jsonPath("$.is_done").value(DEFAULT_IS_DONE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDuty() throws Exception {
        // Get the duty
        restDutyMockMvc.perform(get("/api/dutys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDuty() throws Exception {
        // Initialize the database
        dutyRepository.saveAndFlush(duty);

		int databaseSizeBeforeUpdate = dutyRepository.findAll().size();

        // Update the duty
        duty.setName(UPDATED_NAME);
        duty.setDescription(UPDATED_DESCRIPTION);
        duty.setStart_date(UPDATED_START_DATE);
        duty.setEnd_date(UPDATED_END_DATE);
        duty.setCan_change(UPDATED_CAN_CHANGE);
        duty.setIs_done(UPDATED_IS_DONE);
        restDutyMockMvc.perform(put("/api/dutys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(duty)))
                .andExpect(status().isOk());

        // Validate the Duty in the database
        List<Duty> dutys = dutyRepository.findAll();
        assertThat(dutys).hasSize(databaseSizeBeforeUpdate);
        Duty testDuty = dutys.get(dutys.size() - 1);
        assertThat(testDuty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDuty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDuty.getStart_date().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_START_DATE);
        assertThat(testDuty.getEnd_date().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_END_DATE);
        assertThat(testDuty.getCan_change()).isEqualTo(UPDATED_CAN_CHANGE);
        assertThat(testDuty.getIs_done()).isEqualTo(UPDATED_IS_DONE);
    }

    @Test
    @Transactional
    public void deleteDuty() throws Exception {
        // Initialize the database
        dutyRepository.saveAndFlush(duty);

		int databaseSizeBeforeDelete = dutyRepository.findAll().size();

        // Get the duty
        restDutyMockMvc.perform(delete("/api/dutys/{id}", duty.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Duty> dutys = dutyRepository.findAll();
        assertThat(dutys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
