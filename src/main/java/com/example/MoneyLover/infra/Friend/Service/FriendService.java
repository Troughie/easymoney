package com.example.MoneyLover.infra.Friend.Service;

import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface FriendService {
    ApiResponse<?> addFriend(User user, String id);

    ApiResponse<?> getAllFriendOrPending(User user,String type);
    ApiResponse<?> getAllFriendReceive(User user);
    ApiResponse<?> acceptFriend(User user,String id);
}
