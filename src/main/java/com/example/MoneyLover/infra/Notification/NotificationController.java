package com.example.MoneyLover.infra.Notification;

import com.example.MoneyLover.infra.Notification.DTO.NotificationResponse;
import com.example.MoneyLover.infra.Notification.Service.NotificationService;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.Entity.PaginationParams;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NotificationController {
    @Autowired
    private ResponseException _res;

    @Autowired
    private NotificationService notificationService;
    @GetMapping("/notifications")
    public ResponseEntity<?> getNotification(@AuthenticationPrincipal User user, @RequestParam(required = false) String status, @ModelAttribute PaginationParams paginationParams) {
        var result = notificationService.getNotification(user,status);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("/notification/mark-all-as-read")
    public ResponseEntity<?> makeAllAsRead(@RequestBody List<NotificationResponse> notifications) {
        var result = notificationService.makeAllAsRead(notifications);
        return _res.responseEntity(result,result.getCode());
    }

    @PostMapping("/notification/mark-as-read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable String id) {
        var result = notificationService.markAsRead(id);
        return _res.responseEntity(result,result.getCode());
    }
}
