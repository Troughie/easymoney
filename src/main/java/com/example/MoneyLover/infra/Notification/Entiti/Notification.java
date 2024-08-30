package com.example.MoneyLover.infra.Notification.Entiti;

import com.example.MoneyLover.infra.User.Entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Notification")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToMany()
    @JoinTable(
            name = "user_notification",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<User> users;

    String user;

    @ManyToOne
    User creator;

    String wallet;
    String category;
    String message;

    LocalDateTime createdDate;

    String type;
    boolean unread;
}
