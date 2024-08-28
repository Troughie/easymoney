package com.example.MoneyLover.infra.Notification.ServiceImpl;

import com.example.MoneyLover.infra.Notification.DTO.NotificationResponse;
import com.example.MoneyLover.infra.Notification.Entiti.Notification;
import com.example.MoneyLover.infra.Notification.Entiti.TypeNotification;
import com.example.MoneyLover.infra.Notification.Mapper.NotificationMapper;
import com.example.MoneyLover.infra.Notification.Repository.NotificationRepo;
import com.example.MoneyLover.infra.Notification.Service.NotificationService;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.Entity.ApiResponse;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class INotification implements NotificationService {

        private final ResponseException _res;
        private final NotificationRepo _notificationRepo;
    public ApiResponse<?> getNotification(User user,String status) {
        try {
            if("unread".equalsIgnoreCase(status)) {
                return _res.createSuccessResponse(200, NotificationMapper.INSTANCE.toNotificationResponse(_notificationRepo.findAllByUsersContainingAndUnreadEquals(user,true)));
            }
            return _res.createSuccessResponse(200, NotificationMapper.INSTANCE.toNotificationResponse(_notificationRepo.findAllByUsersContaining(user)));
        }catch (Exception e){
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> markAsRead(String id) {
        try {
            Notification notification = _notificationRepo.findById(id).orElse(null);
            assert notification != null;
            notification.setUnread(false);
            _notificationRepo.save(notification);
            return _res.createSuccessResponse("success",200);
        }catch (Exception e)
        {
            return _res.createErrorResponse(e.getMessage(),500);
        }
    }

    public ApiResponse<?> makeAllAsRead(List<NotificationResponse> notifications) {
        for(NotificationResponse notification : notifications) {
            Notification notification1 = _notificationRepo.findById(notification.getId()).orElse(null);
            assert notification1 != null;
            notification1.setUnread(false);
            _notificationRepo.save(notification1);
        }
        return _res.createSuccessResponse("success",200);
    }

    @Async
    public void sendNotificationTransaction(List<User> users,String user, String wallet, String category) {
        Notification notification = new Notification();
        notification.setCategory(category);
        notification.setWallet(wallet);
        notification.setUsers(new ArrayList<>());

        for(User u : users) {
            notification.getUsers().add(u);
        }
        notification.setUser(user);
        notification.setUnread(true);
        notification.setType(TypeNotification.transaction.name());
        notification.setCreatedDate(LocalDateTime.now());
        _notificationRepo.save(notification);
    }

    @Async
    public void sendNotificationFriend(User user,String creator,String message){
        Notification notification = new Notification();
        notification.setUsers(new ArrayList<>());
        notification.getUsers().add(user);
        if(message!=null &&  !message.isEmpty()){
            notification.setMessage(message);
        }
        notification.setUser(creator);
        notification.setUnread(true);
        notification.setType(TypeNotification.friend.name());
        notification.setCreatedDate(LocalDateTime.now());
        _notificationRepo.save(notification);
    }

    @Async
    public void sendNotificationBudget(List<User> users,String user,String message , String category,TypeNotification type,String wallet) {
        Notification notification = new Notification();
        notification.setUsers(new ArrayList<>());

        for(User u : users) {
            notification.getUsers().add(u);
        }

        if(message!=null &&  !message.isEmpty()){
            notification.setMessage(message);
        }

        notification.setUser(user);
        notification.setCategory(category);
        notification.setUnread(true);
        notification.setType(type.name());
        notification.setWallet(wallet);
        notification.setCreatedDate(LocalDateTime.now());
        _notificationRepo.save(notification);
    }
}
