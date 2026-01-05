package com.example.demo08.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo08.Model.AuditLog;
import com.example.demo08.Repository.AuditLogRepository;

@Service
@Transactional
public class AuditService {
    
    private final AuditLogRepository auditLogRepository;
    
    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    
    public AuditLog logAction(String username, AuditLog.ActionType actionType, Long entityId, String entityType, String details) {
        AuditLog log = new AuditLog(username, actionType, entityId, entityType, details);
        return auditLogRepository.save(log);
    }
    
    public AuditLog logAction(String username, AuditLog.ActionType actionType, Long entityId, String entityType, String details, String ipAddress, String userAgent) {
        AuditLog log = new AuditLog(username, actionType, entityId, entityType, details);
        log.setIpAddress(ipAddress);
        log.setUserAgent(userAgent);
        return auditLogRepository.save(log);
    }
    
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByUsername(String username) {
        return auditLogRepository.findByUsername(username);
    }
    
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByActionType(AuditLog.ActionType actionType) {
        return auditLogRepository.findByActionType(actionType);
    }
    
    @Transactional(readOnly = true)
    public Page<AuditLog> getLogsByUsername(String username, Pageable pageable) {
        return auditLogRepository.findByUsernameOrderByTimestampDesc(username, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<AuditLog> getAllLogs(Pageable pageable) {
        return auditLogRepository.findByOrderByTimestampDesc(pageable);
    }
}