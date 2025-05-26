package com.milestonemania.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "milestones", indexes = {
    @Index(name = "idx_milestone_actual_date", columnList = "actualDate")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String title;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @NotNull
    @Column(nullable = false)
    private LocalDate actualDate;

    @Version
    private Long version;

    public Milestone(String title, String description, LocalDate actualDate) {
        this.title = title;
        this.description = description;
        this.actualDate = actualDate;
    }

    public Milestone(String title, LocalDate actualDate) {
        this.title = title;
        this.actualDate = actualDate;
    }
}

