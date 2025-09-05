package ru.doc.sport_trainings.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "exercise_notes")
public class ExerciseNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="users_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="exercise_types_id",nullable=false)
    private ExerciseType type;

    @Column(nullable = false, name = "dates")
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Column(nullable=false)
    private Double times;
}