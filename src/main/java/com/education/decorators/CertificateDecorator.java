package com.education.decorators;

import com.education.core.Course;
import com.education.core.CourseDecorator;
import com.education.entity.User;
import com.education.service.NotificationService;

public class CertificateDecorator extends CourseDecorator {
    private final NotificationService notificationService = new NotificationService();

    public CertificateDecorator(Course wrappedCourse) {
        super(wrappedCourse);
    }

    @Override
    public void deliverContent() {
        super.deliverContent();
        System.out.println("Issuing certificate upon completion.");
    }

    public boolean shouldIssueCertificate(int progress) {
        return progress >= 100;
    }

    public void issueCertificate(User user, String courseName) {
        if (shouldIssueCertificate(100)) { // Прогресс фиксируем как 100 при завершении
            notificationService.sendNotification(user, "Congratulations, " + user.getUsername() + "! Certificate issued for " + courseName + "!");
        }
    }
}