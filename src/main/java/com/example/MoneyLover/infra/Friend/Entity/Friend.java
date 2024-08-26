package com.example.MoneyLover.infra.Friend.Entity;

import com.example.MoneyLover.infra.User.Entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Friends")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    LocalDateTime createdAt;

    // Status could be used to determine if the friendship is pending, accepted, etc.
    private String status;

}
