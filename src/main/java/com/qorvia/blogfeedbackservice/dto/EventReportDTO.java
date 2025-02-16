package com.qorvia.blogfeedbackservice.dto;

import com.qorvia.blogfeedbackservice.model.AdminApprovalStatus;
import com.qorvia.blogfeedbackservice.model.ReportStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventReportDTO {
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
}
