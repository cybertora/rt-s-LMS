package com.education.courses;

import com.education.core.Course;

public class RussianLanguage implements Course {

    @Override
    public void deliverContent() {

        System.out.println("Delivering content for Russian Language course.");
    }
}