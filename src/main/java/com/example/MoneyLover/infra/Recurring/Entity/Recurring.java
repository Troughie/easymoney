package com.example.MoneyLover.infra.Recurring.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Recurring")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Recurring {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String frequency;

    int every;

    int for_time;

    LocalDate from_date;

    LocalDate to_date;

    boolean forever;

    LocalDate due_date;

    int send_count;

    int date_of_week;

    int occurrence_in_month;

    boolean done;
}

