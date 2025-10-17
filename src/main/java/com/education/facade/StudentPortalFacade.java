package com.education.facade;

import com.education.core.Course;
import com.education.entity.Enrollment;
import com.education.entity.User;
import com.education.service.EducationService;

import java.util.Map;

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

    public Course enrollInCourse(User user, String courseName, boolean withCertificate, boolean withMentor, boolean withGamification) {
        return educationService.enrollUser(user, courseName, withCertificate, withMentor, withGamification);
    }

    public void selectMentor(User user, Long mentorId, Enrollment enrollment) {
        educationService.selectMentor(user, mentorId, enrollment);
    }

    public void startLearning(User user, Enrollment enrollment) {
        educationService.startLearning(user, enrollment);
    }

    public String viewLeaderboard() {
        return educationService.getLeaderboard();
    }

    public Map<String, Course> getAvailableCourses() {
        return educationService.getAvailableCourses();
    }
}