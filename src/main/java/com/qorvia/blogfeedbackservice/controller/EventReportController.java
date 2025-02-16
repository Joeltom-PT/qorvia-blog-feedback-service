package com.qorvia.blogfeedbackservice.controller;

import com.qorvia.blogfeedbackservice.dto.EventReportDTO;
import com.qorvia.blogfeedbackservice.dto.request.GetAllEventReportRequest;
import com.qorvia.blogfeedbackservice.security.RequireRole;
import com.qorvia.blogfeedbackservice.security.Roles;
import com.qorvia.blogfeedbackservice.service.EventReportService;
import com.qorvia.blogfeedbackservice.service.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@Slf4j
public class EventReportController {

    private final JwtService jwtService;
    private final EventReportService eventReportService;

    @PostMapping("/add-event-report/{eventId}")
    public ResponseEntity<?> addEventReport(@PathVariable("eventId") String eventId, @RequestBody EventReportDTO eventReportDTO, HttpServletRequest servletRequest){
        String token = jwtService.getJWTFromRequest(servletRequest);
        Long userId = jwtService.getUserIdFromToken(token);
        String userEmail = jwtService.getEmailFromToken(token);
        return eventReportService.addEventReport(eventId, eventReportDTO, userId, userEmail);
    }

    @GetMapping("/get-all-event-reports")
    @RequireRole(role = Roles.ADMIN)
    public ResponseEntity<?> getAllEventReports(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size){
        return eventReportService.getAllEventReports(page, size);
    }


    @DeleteMapping("/delete/{id}")
    @RequireRole(role = Roles.ADMIN)
    public ResponseEntity<?> deleteReport(@PathVariable("id") Long reportId){
        return eventReportService.deleteReport(reportId);
    }

    @PutMapping("/approve/{id}")
    @RequireRole(role = Roles.ADMIN)
    public ResponseEntity<?> adminApprove(@PathVariable("id") Long reportId) {
        try {
            return eventReportService.adminApprove(reportId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to approve event report.");
        }
    }


    @GetMapping("/get-all")
    @RequireRole(role = Roles.ORGANIZER)
    public ResponseEntity<?> getAllReports(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                            HttpServletRequest servletRequest){
        log.info("Taking ");
        Long organizerId = jwtService.getUserIdFormRequest(servletRequest);
        return eventReportService.getAllReports(organizerId, page, size);
    }

    @PutMapping("/change-status")
    @RequireRole(role = Roles.ORGANIZER)
    public ResponseEntity<?> changeStatus(@RequestParam("reportId") Long reportId,
                                          @RequestParam("status") String status,
                                          HttpServletRequest servletRequest){
        Long organizerId = jwtService.getUserIdFormRequest(servletRequest);
        return eventReportService.updateStatus(reportId, organizerId, status);
    }

}


