package com.education.decorators;

import com.education.core.Course;
import com.education.core.CourseDecorator;

public class GamificationDecorator extends CourseDecorator {
    public GamificationDecorator(Course wrappedCourse) {
        super(wrappedCourse);
    }

    @Override
    public void deliverContent() {
        super.deliverContent();
        System.out.println("Adding points and leaderboard gamification.");
    }

    public boolean shouldUpdateProgress() {
        return true; // Указывает, что геймификация активна
    }
}