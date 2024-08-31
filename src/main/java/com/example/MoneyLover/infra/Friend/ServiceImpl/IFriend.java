package com.example.MoneyLover.infra.Friend.ServiceImpl;

import com.example.MoneyLover.infra.Friend.Entity.Friend;
import com.example.MoneyLover.infra.Friend.Entity.StatusFriend;
import com.example.MoneyLover.infra.Friend.Mapper.FriendMapper;
import com.example.MoneyLover.infra.Friend.Repository.FriendRepo;
import com.example.MoneyLover.infra.Friend.Service.FriendService;
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
            Friend existFriend1 = friendRepo.findFriendExist(user,friendAdd,StatusFriend.pending.name());
            Friend existFriend = friendRepo.findFriendExist(friendAdd,user,StatusFriend.pending.name());
            Friend existFriendAccept = friendRepo.findFriendExist(user,friendAdd,StatusFriend.accepted.name());
            Friend existFriendAccept1 = friendRepo.findFriendExist(friendAdd,user,StatusFriend.accepted.name());
            if(existFriend!=null ||existFriend1!=null){
                return _res.createErrorResponse("Already add friend", 400);
            }

            if(existFriendAccept!=null||existFriendAccept1!=null){
                return _res.createErrorResponse("Already in friend list", 400);
            }

            Friend friend = new Friend();
            friend.setUser(user);
            friend.setFriend(friendAdd);
            friend.setStatus(StatusFriend.pending.name());
            friend.setCreatedAt(LocalDateTime.now());
            friendRepo.save(friend);
            notificationService.sendNotificationFriend(friendAdd,user,null);
            return _res.createSuccessResponse("Add friend successfully",200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> getAllFriendOrPending(User user,String type){
        try {
            StatusFriend status;
            List<Friend> friends = new ArrayList<>();
            if (StatusFriend.pending.name().equalsIgnoreCase(type)) {
                status = StatusFriend.pending;
                friends= friendRepo.findAllUserSend(user, status.name());
            } else if (StatusFriend.accepted.name().equalsIgnoreCase(type)) {
                status = StatusFriend.accepted;
                friends= friendRepo.findAllUser(user, status.name());
            }else if(StatusFriend.block.name().equalsIgnoreCase(type)){
                status = StatusFriend.block;
                friends= friendRepo.findAllUser(user, status.name());
            }else{
                friends=friendRepo.findAllUserReceive(user, StatusFriend.pending.name());
            }
            return _res.createSuccessResponse("successfully",200, FriendMapper.INSTANCE.toUserResponseAll(friends));
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> acceptFriend(User user,String id){
        try {
            Friend friend = friendRepo.findTopById(id);
            if(friend==null){
                return _res.createErrorResponse("Some thing wrong!! try later", 400);
            }
            User userRequest = userRepo.findTopById(friend.getUser().getId());
            friend.setStatus(StatusFriend.accepted.name());
            friendRepo.save(friend);

            Friend friend1 = new Friend();
            friend1.setUser(user);
            friend1.setFriend(userRequest);
            friend1.setStatus(StatusFriend.accepted.name());
            friend1.setCreatedAt(LocalDateTime.now());
            friendRepo.save(friend1);
            notificationService.sendNotificationFriend(userRequest,user,"accepted your request");
            return _res.createSuccessResponse("Accept friend successfully",200);
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
            User userFriend = friend.getFriend();
            User user = friend.getUser();
            Friend friendDelete2 = friendRepo.findTopByUserAndFriend(user,userFriend);
            friendRepo.delete(friendDelete2);
            friendRepo.delete(friend);
            return _res.createSuccessResponse("Remove friend successfully",200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> cancelFriendSend(String id){
        return _res.createSuccessResponse("Cancel friend send successfully",200);
    }
}
