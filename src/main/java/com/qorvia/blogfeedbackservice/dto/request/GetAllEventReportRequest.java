package com.qorvia.blogfeedbackservice.dto.request;

import com.qorvia.blogfeedbackservice.model.AdminApprovalStatus;
import com.qorvia.blogfeedbackservice.model.ReportStatus;
import lombok.Data;

@Data
public class GetAllEventReportRequest {
    private AdminApprovalStatus adminApprovalStatus;
    private ReportStatus reportStatus;

}
