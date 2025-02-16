package com.qorvia.blogfeedbackservice.repository;

import com.qorvia.blogfeedbackservice.model.EventReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventReportRepository extends JpaRepository<EventReport, Long>, JpaSpecificationExecutor<EventReport> {
}
