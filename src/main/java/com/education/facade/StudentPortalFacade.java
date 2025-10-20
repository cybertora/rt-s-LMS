package com.education.facade;

import com.education.core.Course;
import com.education.entity.Enrollment;
import com.education.entity.User;
import com.education.service.EducationService;

import java.util.Map;
import java.util.Scanner;

public class StudentPortalFacade {
    private final EducationService educationService;

    public StudentPortalFacade() {
        this.educationService = new EducationService();
    }

    public User registerUser(String username, String password) {
        return educationService.registerUser(username, password);
    }

    public User loginUser(String username, String password) {
        return educationService.loginUser(username, password);
    }

    public void enrollUser(User user, String courseName, boolean withCertificate, boolean withMentor, boolean withGamification) {
        educationService.enrollUser(user, courseName, withCertificate, withMentor, withGamification);
    }

    public void selectMentor(User user, Long mentorId, Enrollment enrollment) {
        educationService.selectMentor(user, mentorId, enrollment);
    }

    public void startLearning(User user, Enrollment enrollment, Scanner scanner) {
        educationService.startLearning(user, enrollment, scanner);
    }

    public String getLeaderboard() {
        return educationService.getLeaderboard();
    }

    public Map<String, Course> getAvailableCourses() {
        return educationService.getAvailableCourses();
    }
}