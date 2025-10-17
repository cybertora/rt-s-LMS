package com.education.courses;

import com.education.core.Course;

public class PhysicalEducation implements Course{

    @Override
    public void deliverContent() {
        System.out.println("Delivering content for PE course.");
    }
}
