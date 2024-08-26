package com.example.MoneyLover.infra.Wallet.Dto;

import com.example.MoneyLover.infra.User.Dto.UserResponse;
import com.example.MoneyLover.infra.Wallet.Entity.Permission;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerResponse {
    UserResponse user;
    Permission permission;
    long totalAmount;
    long totalTransaction;
}
