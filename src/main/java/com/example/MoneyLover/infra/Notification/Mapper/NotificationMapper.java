package com.example.MoneyLover.infra.Notification.Mapper;

import com.example.MoneyLover.infra.Notification.DTO.NotificationResponse;
import com.example.MoneyLover.infra.Notification.Entiti.Notification;
import com.example.MoneyLover.infra.User.Mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "type",target = "type")
    @Mapping(source = "message",target = "message")
    List<NotificationResponse> toNotificationResponse(List<Notification> notification);

    default List<NotificationResponse> toNotificationResponse2(List<Notification> notifications) {
        List<NotificationResponse> notificationResponses = toNotificationResponse(notifications);

        // Iterate through both notificationResponses and notifications in parallel
        for (int i = 0; i < notifications.size(); i++) {
            Notification notification = notifications.get(i);
            NotificationResponse notificationResponse = notificationResponses.get(i);

            // Set the creator in the notificationResponse
            notificationResponse.setCreator(UserMapper.INSTANCE.toUserResponse(notification.getCreator()));
        }
        return notificationResponses;
    }
}
