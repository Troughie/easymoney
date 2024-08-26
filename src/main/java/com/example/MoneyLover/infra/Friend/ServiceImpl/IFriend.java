package com.example.MoneyLover.infra.Friend.ServiceImpl;

import com.example.MoneyLover.infra.Friend.Entity.Friend;
import com.example.MoneyLover.infra.Friend.Entity.StatusFriend;
import com.example.MoneyLover.infra.Friend.Mapper.FriendMapper;
import com.example.MoneyLover.infra.Friend.Repository.FriendRepo;
import com.example.MoneyLover.infra.Friend.Service.FriendService;
import com.example.MoneyLover.infra.Notification.Repository.NotificationRepo;
import com.example.MoneyLover.infra.Notification.Service.NotificationService;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.infra.User.Repository.UserRepository;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IFriend implements FriendService {
    private final ResponseException _res;
    private final FriendRepo friendRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    public ApiResponse<?> addFriend(User user, String id)
    {
        try {
            User friendAdd = userRepo.findTopById(id);
            if (friendAdd == null) {
                return _res.createErrorResponse("User not found", 404);
            }
            Friend existFriend = friendRepo.findFriendExist(user,StatusFriend.pending.name());
            if(existFriend!=null){
                return _res.createErrorResponse("Already add friend", 400);
            }

            Friend friend = new Friend();
            friend.setUser(user);
            friend.setFriend(friendAdd);
            friend.setStatus(StatusFriend.pending.name());
            friend.setCreatedAt(LocalDateTime.now());
            friendRepo.save(friend);
            notificationService.sendNotificationFriend(friendAdd,user.getUsername());
            return _res.createSuccessResponse("Add friend successfully",200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> getAllFriendOrPending(User user,String type){
        try {
            StatusFriend status;
            if (StatusFriend.pending.name().equalsIgnoreCase(type)) {
                status = StatusFriend.pending;
            } else if (StatusFriend.accepted.name().equalsIgnoreCase(type)) {
                status = StatusFriend.accepted;
            } else {
                status = StatusFriend.block;
            }

            // Fetch friends based on status
            List<Friend> friends = friendRepo.findAllUserOrSend(user, status.name());

            return _res.createSuccessResponse("successfully",200, FriendMapper.INSTANCE.toUserResponseAll(friends));
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> acceptFriend(String id){
        try {
            Friend friend = friendRepo.findTopById(id);
            if(friend==null){
                return _res.createErrorResponse("Some thing wrong!! try later", 400);
            }
            friend.setStatus(StatusFriend.accepted.name());
            friendRepo.save(friend);
            return _res.createSuccessResponse("Accept friend successfully",200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> getAllFriendReceive(User user){
        try {
            List<Friend> friends=friendRepo.findAllUserReceive(user, StatusFriend.pending.name());
            return _res.createSuccessResponse("successfully",200,FriendMapper.INSTANCE.toUserResponseReceive(friends));
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> removeFriend(String id){
        try {
            Friend friend = friendRepo.findTopById(id);
            if(friend==null){
                return _res.createErrorResponse("Some thing wrong!! try later", 400);
            }
            friendRepo.delete(friend);
            return _res.createSuccessResponse("Remove friend successfully",200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }
}
