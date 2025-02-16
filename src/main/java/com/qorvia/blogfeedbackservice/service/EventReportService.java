package com.qorvia.blogfeedbackservice.service;

import com.qorvia.blogfeedbackservice.dto.EventReportDTO;
import com.qorvia.blogfeedbackservice.dto.request.GetAllEventReportRequest;
import org.springframework.http.ResponseEntity;

public interface EventReportService {
    ResponseEntity<?> addEventReport(String eventId, EventReportDTO eventReportDTO, Long userId, String userEmail);

    ResponseEntity<?> getAllEventReports(int page, int size);

    ResponseEntity<?> deleteReport(Long reportId);

    ResponseEntity<?> adminApprove(Long reportId);

    ResponseEntity<?> getAllReports(Long organizerId, int page, int size);

    ResponseEntity<?> updateStatus(Long reportId, Long organizerId, String status);
}
