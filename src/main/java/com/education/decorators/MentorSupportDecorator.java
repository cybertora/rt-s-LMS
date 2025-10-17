package com.education.decorators;

import com.education.core.Course;
import com.education.core.CourseDecorator;

public class MentorSupportDecorator extends CourseDecorator {
    public MentorSupportDecorator(Course wrappedCourse) {
        super(wrappedCourse);
    }

    @Override
    public void deliverContent() {
        super.deliverContent();
        provideMentorSupport();
    }

    private void provideMentorSupport() {
        System.out.println("Providing personal mentor support.");
    }
}