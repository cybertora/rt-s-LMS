package com.education.core;

public abstract class CourseDecorator implements Course {
    protected Course wrappedCourse;

    public CourseDecorator(Course wrappedCourse) {
        this.wrappedCourse = wrappedCourse;
    }

    @Override
    public void deliverContent() {
        wrappedCourse.deliverContent();
    }
}