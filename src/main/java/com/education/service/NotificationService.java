package com.education.service;

import com.education.entity.User;

public class NotificationService {
    public void sendNotification(User user, String message) {
        System.out.println("Notification for " + user.getUsername() + ": " + message);
    }
}