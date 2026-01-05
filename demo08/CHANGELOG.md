# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.0] - 2026-01-05

### Added
- Initial project setup with Spring Boot 3.3.5
- User registration and authentication system
- Role-based access control (RBAC) with ADMIN, USER roles
- Student approval workflow system
- Scholarship application management system
- Basic CRUD operations for users and applications
- Thymeleaf templates for web interface
- Security configuration with Spring Security
- Database integration with JPA/Hibernate
- Validation annotations for model entities
- Global exception handling
- Unit and integration tests
- Maven wrapper for build consistency

### Changed
- Enhanced model validation with JSR-303 annotations
- Improved error handling with comprehensive exception handlers
- Updated README with comprehensive project documentation

### Fixed
- Fixed validation issues in model classes
- Improved security configuration for better protection
- Enhanced form validation and error messaging

### Security
- Added proper input validation for all user inputs
- Implemented CSRF protection for forms
- Enhanced security configuration with proper headers