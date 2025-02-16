package com.qorvia.blogfeedbackservice.service;

import com.qorvia.blogfeedbackservice.dto.EventReportDTO;
import com.qorvia.blogfeedbackservice.dto.request.GetAllEventReportRequest;
import com.qorvia.blogfeedbackservice.dto.response.PaginationResponse;
import com.qorvia.blogfeedbackservice.model.AdminApprovalStatus;
import com.qorvia.blogfeedbackservice.model.EventReport;
import com.qorvia.blogfeedbackservice.model.ReportStatus;
import com.qorvia.blogfeedbackservice.repository.EventReportRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventReportServiceImpl implements EventReportService {

    private final EventReportRepository eventReportRepository;

    @Override
    public ResponseEntity<?> addEventReport(String eventId, EventReportDTO eventReportDTO, Long userId, String userEmail) {
        log.info("Received request to add event report for eventId: {} by userId: {}", eventId, userId);

        if (!StringUtils.hasText(eventId) || eventReportDTO == null || !StringUtils.hasText(eventReportDTO.getSubject()) || !StringUtils.hasText(eventReportDTO.getContent())) {
            log.warn("Invalid event report data provided. Missing required fields.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required fields: eventId, subject, or content.");
        }

        try {
            EventReport eventReport = new EventReport();
            eventReport.setEventId(eventId);
            eventReport.setOrganizerId(eventReportDTO.getOrganizerId());
            eventReport.setUserId(userId);
            eventReport.setUserEmail(userEmail);
            eventReport.setSubject(eventReportDTO.getSubject());
            eventReport.setContent(eventReportDTO.getContent());
            eventReport.setReportStatus(ReportStatus.PENDING);
            eventReport.setAdminApprovalStatus(AdminApprovalStatus.PENDING);
            eventReport.setCreatedAt(LocalDateTime.now());

            eventReportRepository.save(eventReport);

            log.info("Event report successfully added for eventId: {}", eventId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Event report created successfully.");

        } catch (Exception e) {
            log.error("Error while adding event report for eventId: {}", eventId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create event report.");
        }
    }

    @Override
    public ResponseEntity<?> getAllEventReports(int page, int size) {
        log.info("Received request to fetch all event reports with page: {}, size: {}", page, size);

        if (page < 0 || size <= 0) {
            log.warn("Invalid pagination parameters provided. Page: {}, Size: {}", page, size);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid pagination parameters.");
        }

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<EventReport> eventReportPage = eventReportRepository.findAll(pageable);

            List<EventReportDTO> eventReportDTOs = eventReportPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            PaginationResponse<EventReportDTO> response = PaginationResponse.<EventReportDTO>builder()
                    .data(eventReportDTOs)
                    .totalPages(eventReportPage.getTotalPages())
                    .totalElements(eventReportPage.getTotalElements())
                    .currentPage(eventReportPage.getNumber())
                    .pageSize(eventReportPage.getSize())
                    .build();

            log.info("Fetched event reports successfully. Total elements: {}, Current page: {}",
                    eventReportPage.getTotalElements(), eventReportPage.getNumber());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error while fetching event reports.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch event reports.");
        }
    }
    @Override
    public ResponseEntity<?> deleteReport(Long reportId) {
        try {
            if (!eventReportRepository.existsById(reportId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event report not found.");
            }

            eventReportRepository.deleteById(reportId);
            return ResponseEntity.ok("Event report deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete event report.");
        }
    }

    public ResponseEntity<?> adminApprove(Long reportId) {
        try {
            EventReport report = eventReportRepository.findById(reportId)
                    .orElseThrow(() -> new RuntimeException("Event report not found"));
            report.setAdminApprovalStatus(AdminApprovalStatus.APPROVED);
            eventReportRepository.save(report);
            return ResponseEntity.ok("Event report approved successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to approve event report.");
        }
    }

    @Override
    public ResponseEntity<?> getAllReports(Long organizerId, int page, int size) {
        log.info("Received request to fetch all event reports for organizerId: {} with page: {}, size: {}", organizerId, page, size);

        if (page < 0 || size <= 0) {
            log.warn("Invalid pagination parameters provided. Page: {}, Size: {}", page, size);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid pagination parameters.");
        }

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<EventReport> eventReportPage = eventReportRepository.findAll(
                    (root, query, criteriaBuilder) -> {
                        List<Predicate> predicates = new ArrayList<>();

                        if (organizerId != null) {
                            predicates.add(criteriaBuilder.equal(root.get("organizerId"), organizerId));
                        }

                        predicates.add(criteriaBuilder.equal(root.get("adminApprovalStatus"), AdminApprovalStatus.APPROVED));

                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    },
                    pageable
            );

            List<EventReportDTO> eventReportDTOs = eventReportPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            PaginationResponse<EventReportDTO> response = PaginationResponse.<EventReportDTO>builder()
                    .data(eventReportDTOs)
                    .totalPages(eventReportPage.getTotalPages())
                    .totalElements(eventReportPage.getTotalElements())
                    .currentPage(eventReportPage.getNumber())
                    .pageSize(eventReportPage.getSize())
                    .build();

            log.info("Fetched event reports successfully for organizerId: {}. Total elements: {}, Current page: {}",
                    organizerId, eventReportPage.getTotalElements(), eventReportPage.getNumber());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error while fetching event reports for organizerId: {}", organizerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch event reports.");
        }
    }

    @Override
    public ResponseEntity<?> updateStatus(Long reportId, Long organizerId, String status) {
        log.info("Received request to update status for reportId: {}, organizerId: {}, status: {}", reportId, organizerId, status);

        if (reportId == null || organizerId == null || !StringUtils.hasText(status)) {
            log.warn("Invalid input parameters provided. Missing required fields.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required fields: reportId, organizerId, or status.");
        }

        try {
            EventReport eventReport = eventReportRepository.findById(reportId)
                    .orElseThrow(() -> new RuntimeException("Event report not found"));

            if (!eventReport.getOrganizerId().equals(organizerId)) {
                log.warn("OrganizerId mismatch. OrganizerId provided: {}, OrganizerId in report: {}", organizerId, eventReport.getOrganizerId());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this report.");
            }

            ReportStatus reportStatus;
            try {
                reportStatus = ReportStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status provided: {}", status);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status. Allowed values: " + getAllowedStatusValues());
            }

            eventReport.setReportStatus(reportStatus);
            eventReportRepository.save(eventReport);

            log.info("Status updated successfully for reportId: {}. New status: {}", reportId, reportStatus);
            return ResponseEntity.ok("Event report status updated successfully.");
        } catch (RuntimeException e) {
            log.error("Error while updating status for reportId: {}", reportId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while updating status for reportId: {}", reportId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update event report status.");
        }
    }

    private String getAllowedStatusValues() {
        return String.join(", ", ReportStatus.getAllowedValues());
    }

    private EventReportDTO convertToDTO(EventReport eventReport) {
        return EventReportDTO.builder()
                .id(eventReport.getId())
                .eventId(eventReport.getEventId())
                .organizerId(eventReport.getOrganizerId())
                .userId(eventReport.getUserId())
                .userEmail(eventReport.getUserEmail())
                .subject(eventReport.getSubject())
                .content(eventReport.getContent())
                .reportStatus(eventReport.getReportStatus())
                .adminApprovalStatus(eventReport.getAdminApprovalStatus())
                .createdAt(eventReport.getCreatedAt())
                .build();
    }

}
