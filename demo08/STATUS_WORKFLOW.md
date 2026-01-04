# Student Status Workflow

## Overview

The Scholarship Management System uses a three-tier status system for student registration and approval.

## Status States

### 1. **PENDING** ⏳

- **Initial State**: New students start with PENDING status
- **Display**: Yellow badge "⏳ Pending"
- **Actions Available**:
  - Teachers can approve → ACCEPTED
  - Teachers can reject → REJECTED

### 2. **ACCEPTED** ✓

- **Triggered By**: Teacher approval via dashboard
- **Display**: Green badge "✓ Accepted"
- **Access**: Student can access:
  - Student Dashboard
  - Apply for Scholarships
  - View Scholarship Applications
- **Flow**: After teacher approves student
  - Status updates to ACCEPTED
  - Approval date recorded
  - Teacher name recorded in `approvedByTeacher`

### 3. **REJECTED** ✗

- **Triggered By**: Teacher rejection via dashboard
- **Display**: Red badge "✗ Rejected"
- **Access**: Restricted to rejection page
- **Flow**: After teacher rejects student
  - Status updates to REJECTED
  - Rejection reason recorded
  - Rejection date recorded

## User Flow

### Student Login Flow

```
1. Student logs in
2. System checks status via /check-status endpoint
3. Based on status:
   - ACCEPTED → Redirect to /student/dashboard
   - PENDING → Redirect to /pending page
   - REJECTED → Redirect to /rejected page
```

### Teacher Dashboard

```
1. Teacher logs in
2. Dashboard shows:
   - Quick actions (Scholarship Review)
   - Registered Students list with status badges
3. Teacher can:
   - Click "Approve" → Status becomes ACCEPTED
   - Click "Reject" → Opens modal for rejection reason
```

### Scholarship Management

- **Students**: Can only apply for scholarships if ACCEPTED
- **Teachers**: Can review all pending scholarship applications regardless of student status

## Code Implementation

### Model Changes

- **Enum**: `Student.ApprovalStatus` with three values:
  - PENDING
  - ACCEPTED
  - REJECTED

### Service Layer

- **StudentService.approveStudent()**: Updates status to ACCEPTED
- **StudentService.getStudentStatus()**: Returns current student status
- **StudentService.rejectStudent()**: Updates status to REJECTED

### Controller Routing

- **HomeController.checkStudentStatus()**: Routes based on approval status
- **TeacherController.approveStudent()**: Approves and updates status
- **StudentController**: Accessible only for ACCEPTED students

### Frontend Templates

- **teacher.html**: Shows status badges for all students
- **pending.html**: Displayed when student status is PENDING
- **rejected.html**: Displayed when student status is REJECTED
- **student.html**: Accessible only when student status is ACCEPTED

## Database Schema

```sql
CREATE TABLE students (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED') DEFAULT 'PENDING',
    registration_date TIMESTAMP,
    approval_date TIMESTAMP,
    approved_by_teacher VARCHAR(255),
    rejection_reason TEXT
);
```

## Status Transition Diagram

```
┌──────────┐
│ PENDING  │ (Initial State)
└────┬─────┘
     │
     ├─── Teacher Approves ───→ ┌──────────┐
     │                          │ ACCEPTED │ (Student can apply for scholarships)
     │                          └──────────┘
     │
     └─── Teacher Rejects ────→ ┌──────────┐
                                │ REJECTED │ (Access restricted)
                                └──────────┘
```

## Example Status Badges

- ✓ Accepted (Green)
- ⏳ Pending (Yellow)
- ✗ Rejected (Red)
