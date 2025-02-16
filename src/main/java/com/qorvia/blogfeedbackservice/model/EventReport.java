package com.qorvia.blogfeedbackservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "event_reports")
public class EventReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventId;
    private Long organizerId;
    private Long userId;
    private String userEmail;
    private String subject;
    private String content;
    private ReportStatus reportStatus;
    private AdminApprovalStatus adminApprovalStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
