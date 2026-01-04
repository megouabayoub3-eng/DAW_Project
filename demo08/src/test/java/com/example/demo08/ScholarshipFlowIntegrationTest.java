package com.example.demo08;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo08.Model.ScholarshipApplication;
import com.example.demo08.Repository.ScholarshipApplicationRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ScholarshipFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScholarshipApplicationRepository scholarshipRepository;

    @BeforeEach
    public void cleanup() {
        // Ensure repository starts clean each test; DataInitializer still seeds demo
        // data so we won't delete everything
    }

    @Test
    @WithMockUser(username = "student", roles = { "STUDENT" })
    public void studentCanApplyAndSeePendingApplication() throws Exception {
        int before = scholarshipRepository.findByStudent_Username("student").size();

        this.mockMvc.perform(post("/student/scholarship/apply")
                .param("amount", "1234.5")
                .param("reason", "Integration test funding")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/student/scholarship/my-applications**"));

        List<ScholarshipApplication> apps = scholarshipRepository.findByStudent_Username("student");
        assertThat(apps.size()).isGreaterThan(before);

        ScholarshipApplication app = apps.stream().filter(a -> "Integration test funding".equals(a.getReason()))
                .findFirst().orElse(null);
        assertThat(app).isNotNull();
        assertThat(app.getStatus()).isEqualTo(ScholarshipApplication.Status.PENDING);
    }

    @Test
    @WithMockUser(username = "teacher", roles = { "TEACHER" })
    public void teacherCanApproveApplication() throws Exception {
        List<ScholarshipApplication> apps = scholarshipRepository.findByStatus(ScholarshipApplication.Status.PENDING);
        assertThat(apps).isNotEmpty();
        ScholarshipApplication app = apps.get(0);

        this.mockMvc.perform(post("/teacher/scholarship/approve/" + app.getId())
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/teacher/scholarships**"));

        ScholarshipApplication updated = scholarshipRepository.findById(app.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(ScholarshipApplication.Status.APPROVED);
        assertThat(updated.getReviewedBy()).isEqualTo("teacher");
    }
}
