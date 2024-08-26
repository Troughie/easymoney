package com.example.MoneyLover.infra.Notification.Service;

import com.example.MoneyLover.infra.Notification.DTO.NotificationResponse;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.Entity.ApiResponse;

import java.util.List;

public interface NotificationService {
    ApiResponse<?> getNotification(User user,String status);
    void sendNotificationTransaction(List<User> users, String user, String wallet, String category);
    void sendNotificationFriend(User user,String creator);
    ApiResponse<?> makeAllAsRead(List<NotificationResponse> notifications);
}
