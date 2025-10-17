package com.education.decorators;

import com.education.core.Course;
import com.education.core.CourseDecorator;
import com.education.entity.Enrollment;
import com.education.entity.User;
import com.education.repository.EnrollmentRepository;
import com.education.service.NotificationService;

public class CertificateDecorator extends CourseDecorator {
    private final NotificationService notificationService = new NotificationService();
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepository();

    public CertificateDecorator(Course wrappedCourse) {
        super(wrappedCourse);
    }

    @Override
    public void deliverContent() {
        super.deliverContent();
        issueCertificate();
    }

    private void issueCertificate() {
        System.out.println("Issuing certificate upon completion.");
    }

    // Новый метод для проверки и выдачи сертификата
    public void checkAndIssueCertificate(User user, Enrollment enrollment) {
        if (enrollment.getProgress() >= 100) {
            notificationService.sendNotification(user, "Certificate issued for " + enrollment.getCourseName() + "!");
        }
    }
}