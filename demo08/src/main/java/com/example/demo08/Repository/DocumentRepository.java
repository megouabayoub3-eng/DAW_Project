package com.example.demo08.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo08.Model.Document;
import com.example.demo08.Model.ScholarshipApplication;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByScholarshipApplication(ScholarshipApplication application);
    
    List<Document> findByScholarshipApplication_Id(Long applicationId);
}