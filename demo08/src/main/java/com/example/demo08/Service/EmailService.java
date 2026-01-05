package com.example.demo08.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Async
    public void sendApplicationStatusUpdate(String to, String status, String reason) {
        // In a real application, this would send actual emails
        // For now, we'll just log the email for demonstration
        String subject = "Scholarship Application Status Update";
        String body = String.format(
            "Dear Applicant,\n\n" +
            "Your scholarship application status has been updated to: %s\n\n" +
            "Additional information: %s\n\n" +
            "Best regards,\n" +
            "Scholarship Management System",
            status, 
            reason != null ? reason : "No additional information provided."
        );
        
        logger.info("Email would be sent to: {} with subject: {} and body: {}", to, subject, body);
        // Simulate email sending delay
        try {
            Thread.sleep(100); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Email sending interrupted", e);
        }
    }
    
    @Async
    public void sendRegistrationStatusUpdate(String to, String status, String reason) {
        String subject = "Registration Status Update";
        String body = String.format(
            "Dear User,\n\n" +
            "Your registration status has been updated to: %s\n\n" +
            "Additional information: %s\n\n" +
            "Best regards,\n" +
            "Scholarship Management System",
            status, 
            reason != null ? reason : "No additional information provided."
        );
        
        logger.info("Email would be sent to: {} with subject: {} and body: {}", to, subject, body);
    }
}