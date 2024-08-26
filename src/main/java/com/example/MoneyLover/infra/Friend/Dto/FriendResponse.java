package com.example.MoneyLover.infra.Friend.Dto;

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
public class FriendResponse {
    String id;

    LocalDateTime createdAt;

    UserResponse user;
}
