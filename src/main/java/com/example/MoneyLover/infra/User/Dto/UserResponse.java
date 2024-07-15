package com.example.MoneyLover.infra.User.Dto;

import com.example.MoneyLover.shares.Annotation.Column;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    String user_id;
    String username;
    String email;
    boolean is_enable;
}
