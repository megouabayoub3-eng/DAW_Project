package com.example.demo08.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo08.Model.ScholarshipApplication;
import com.example.demo08.Model.ScholarshipApplication.Status;

@Repository
public interface ScholarshipApplicationRepository extends JpaRepository<ScholarshipApplication, Long> {
    List<ScholarshipApplication> findByStatus(Status status);

    List<ScholarshipApplication> findByStudent_Username(String username);
}
