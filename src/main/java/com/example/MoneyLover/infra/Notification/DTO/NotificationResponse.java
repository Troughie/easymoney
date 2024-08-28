package com.example.MoneyLover.infra.Notification.DTO;

import com.example.MoneyLover.infra.Notification.Entiti.TypeNotification;
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

    String userId;

    String wallet;

    String category;

    String message;

    LocalDateTime createdDate;

    boolean unread;

    String type;
}
