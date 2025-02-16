package com.qorvia.blogfeedbackservice.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ReportStatus {
    PENDING,
    IN_REVIEW,
    RESOLVED,
    REJECTED;

    public static List<String> getAllowedValues() {
        return Arrays.stream(values()).map(Enum::name).collect(Collectors.toList());
    }

}
