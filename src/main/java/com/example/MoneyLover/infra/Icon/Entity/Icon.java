package com.example.MoneyLover.infra.Icon.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "icon")
public class Icon {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    @Column(unique = true)
    String path;
}
