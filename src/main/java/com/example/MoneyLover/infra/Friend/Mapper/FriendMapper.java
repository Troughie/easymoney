package com.example.MoneyLover.infra.Friend.Mapper;

import com.example.MoneyLover.infra.Friend.Dto.FriendResponse;
import com.example.MoneyLover.infra.Friend.Entity.Friend;
import com.example.MoneyLover.infra.User.Dto.UserResponse;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.User.Mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface FriendMapper {
    FriendMapper INSTANCE = Mappers.getMapper(FriendMapper.class);

    default List<FriendResponse> toUserResponseAll(List<Friend> friend) {
        List<FriendResponse> friendResponses = new ArrayList<>();
        for (Friend f : friend) {
            UserResponse user =UserMapper.INSTANCE.toUserResponse(f.getFriend());
            FriendResponse fr = new FriendResponse();
            fr.setId(f.getId());
            fr.setUser(user);
            fr.setCreatedAt(f.getCreatedAt());
            friendResponses.add(fr);
        }
        return friendResponses;
    }

    default List<FriendResponse> toUserResponseReceive(List<Friend> friend) {
        List<FriendResponse> friendResponses = new ArrayList<>();
        for (Friend f : friend) {
            UserResponse user =UserMapper.INSTANCE.toUserResponse(f.getUser());
            FriendResponse fr = new FriendResponse();
            fr.setUser(user);
            fr.setCreatedAt(f.getCreatedAt());
            friendResponses.add(fr);
        }
        return friendResponses;
    }

}
