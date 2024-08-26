package com.example.MoneyLover.infra.Friend.ServiceImpl;

import com.example.MoneyLover.infra.Friend.Entity.Friend;
import com.example.MoneyLover.infra.Friend.Entity.StatusFriend;
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
            User user1 = userRepo.findTopById(id);
            if (user1 == null) {
                return _res.createErrorResponse("User not found", 404);
            }
            Friend friend = new Friend();
            friend.setUser(user);
            friend.setFriend(user1);
            friend.setStatus(StatusFriend.pending.name());
            friendRepo.save(friend);
            notificationService.sendNotificationFriend(user1,user.getUsername());
            return _res.createSuccessResponse("Add friend successfully",200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> getAllFriendOrPending(User user,String type){
        try {
            List<Friend> friends=new ArrayList<>();
            if (StatusFriend.pending.name().equalsIgnoreCase(type)) {
                friends=friendRepo.findAllUserOrSend(user, StatusFriend.pending.name());
            }else if(StatusFriend.accepted.name().equalsIgnoreCase(type)){
                friends=friendRepo.findAllUserOrSend(user, StatusFriend.accepted.name());
            }else{
                friends=friendRepo.findAllUserOrSend(user, StatusFriend.block.name());
            }
            return _res.createSuccessResponse("successfully",200,friends);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> acceptFriend(User user,String id){
        try {
            User user1 = userRepo.findTopById(id);
            if (user1 == null) {
                return _res.createErrorResponse("User not found", 404);
            }
            Friend friend = friendRepo.findByUserAndFriend(user, user1);
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
            return _res.createSuccessResponse("successfully",200,friends);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }
}
