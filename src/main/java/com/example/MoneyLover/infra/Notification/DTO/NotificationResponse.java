package com.example.MoneyLover.infra.Notification.DTO;

import com.example.MoneyLover.infra.Notification.Entiti.TypeNotification;
import com.example.MoneyLover.infra.User.Dto.UserResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    String id;

    String user;

    UserResponse creator;

    String wallet;

    String category;

    String message;

    LocalDateTime createdDate;

    boolean unread;

    String type;
}
