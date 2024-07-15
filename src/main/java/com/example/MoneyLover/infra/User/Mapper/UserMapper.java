package com.example.MoneyLover.infra.User.Mapper;

import com.example.MoneyLover.infra.User.Dto.SignInDto;
import com.example.MoneyLover.infra.User.Dto.UserResponse;
import com.example.MoneyLover.infra.User.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(source = "email", target = "email")
    UserResponse toUserResponse(User user);
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password",ignore = true)
    User registerUser(SignInDto signInDto);
}
