package com.example.MoneyLover.infra.Notification.Repository;

import com.example.MoneyLover.infra.Notification.Entiti.Notification;
import com.example.MoneyLover.infra.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification,String> {

    List<Notification> findAllByUsersContaining(User user);
    List<Notification> findAllByUsersContainingAndUnreadEquals(User user,boolean unread);



}
