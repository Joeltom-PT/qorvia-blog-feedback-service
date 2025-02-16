package com.qorvia.blogfeedbackservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 400)
    private String content;

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String userName;
    private String userEmail;
    private Long userId;
    private String eventId;
    private Boolean isVerified;
}