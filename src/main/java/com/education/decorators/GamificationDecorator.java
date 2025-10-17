package com.education.decorators;

import com.education.core.Course;
import com.education.core.CourseDecorator;
import com.education.entity.Enrollment;
import com.education.entity.User;
import com.education.repository.UserRepository;

public class GamificationDecorator extends CourseDecorator {
    private final UserRepository userRepository = new UserRepository();

    public GamificationDecorator(Course wrappedCourse) {
        super(wrappedCourse);
    }

    @Override
    public void deliverContent() {
        super.deliverContent();
        addGamification();
    }

    private void addGamification() {
        System.out.println("Adding points and leaderboard gamification.");
    }

    // Новый метод для обработки прогресса с геймификацией
    public void updateProgress(User user, Enrollment enrollment) {
        int currentProgress = enrollment.getProgress();
        if (currentProgress < 100) {
            enrollment.setProgress(currentProgress + 20);
            if (enrollment.getProgress() >= 100) {
                enrollment.setProgress(100);
                user.setPoints(user.getPoints() + 100);
            } else {
                user.setPoints(user.getPoints() + 20);
            }
            userRepository.save(user);// Предполагается, что enrollmentRepository доступен
        }
    }
}