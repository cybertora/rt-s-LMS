package com.education.service;

import com.education.core.Course;
import com.education.courses.DesignAndAnalysisOfAlgorithms;
import com.education.courses.OperatingSystems;
import com.education.courses.PhysicalEducation;
import com.education.courses.RussianLanguage;
import com.education.courses.SoftwareDesignPatterns;
import com.education.courses.WebTecnologies1;
import com.education.decorators.CertificateDecorator;
import com.education.decorators.GamificationDecorator;
import com.education.decorators.MentorSupportDecorator;
import com.education.entity.Enrollment;
import com.education.entity.Mentor;
import com.education.entity.User;
import com.education.repository.EnrollmentRepository;
import com.education.repository.MentorRepository;
import com.education.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class EducationService {
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final NotificationService notificationService;
    private final Map<String, Course> availableCourses;

    public EducationService() {
        this.userRepository = new UserRepository();
        this.mentorRepository = new MentorRepository();
        this.enrollmentRepository = new EnrollmentRepository();
        this.notificationService = new NotificationService();
        this.availableCourses = new HashMap<>();
        initializeCourses();
    }

    private void initializeCourses() {
        availableCourses.put("DesignAndAnalysisOfAlgorithms", new DesignAndAnalysisOfAlgorithms());
        availableCourses.put("OperatingSystems", new OperatingSystems());
        availableCourses.put("PhysicalEducation", new PhysicalEducation());
        availableCourses.put("RussianLanguage", new RussianLanguage());
        availableCourses.put("SoftwareDesignPatterns", new SoftwareDesignPatterns());
        availableCourses.put("WebTechnologies1", new WebTecnologies1());
    }

    public Map<String, Course> getAvailableCourses() {
        return availableCourses;
    }

    public User registerUser(String username, String password) {
        User user = new User(username, password);
        user = userRepository.save(user);
        notificationService.sendNotification(user, "Welcome, " + username + "! You are registered.");
        return user;
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            notificationService.sendNotification(user, "Login successful for " + username);
            return user;
        }
        System.out.println("Login failed for username: " + username);
        return null;
    }

    public Course enrollUser(User user, String courseName, boolean withCertificate, boolean withMentor, boolean withGamification) {
        Course course = availableCourses.get(courseName);
        if (course == null) {
            System.out.println("Course " + courseName + " not found."); // Заменил logger.error
            return null;
        }
        if (withCertificate) {
            course = new CertificateDecorator(course);
        }
        if (withMentor) {
            course = new MentorSupportDecorator(course);
        }
        if (withGamification) {
            course = new GamificationDecorator(course);
        }
        Enrollment enrollment = new Enrollment(user, courseName, 0);
        enrollmentRepository.save(enrollment);
        notificationService.sendNotification(user, "Enrolled in " + courseName + " successfully.");
        return course;
    }

    public void selectMentor(User user, Long mentorId, Enrollment enrollment) {
        Mentor mentor = mentorRepository.findById(mentorId);
        if (mentor != null) {
            enrollment.setMentor(mentor);
            enrollmentRepository.save(enrollment);
            notificationService.sendNotification(user, "Mentor " + mentor.getName() + " assigned to your course.");
        } else {
            System.out.println("Mentor with ID " + mentorId + " not found."); // Заменил logger.warn
        }
    }

    public void startLearning(User user, Enrollment enrollment) {
        Course course = availableCourses.get(enrollment.getCourseName());
        if (course instanceof GamificationDecorator) {
            ((GamificationDecorator) course).updateProgress(user, enrollment);
        } else {
            int currentProgress = enrollment.getProgress();
            if (currentProgress < 100) {
                enrollment.setProgress(currentProgress + 20);
                if (enrollment.getProgress() >= 100) {
                    enrollment.setProgress(100);
                    notificationService.sendNotification(user, "Course " + enrollment.getCourseName() + " completed!");
                } else {
                    notificationService.sendNotification(user, "Learning session for " + enrollment.getCourseName() +
                            ". Progress: " + enrollment.getProgress() + "%");
                }
                enrollmentRepository.save(enrollment);
            } else {
                notificationService.sendNotification(user, "Course " + enrollment.getCourseName() + " already completed.");
            }
        }
        if (course instanceof CertificateDecorator) {
            ((CertificateDecorator) course).checkAndIssueCertificate(user, enrollment);
        }
    }

    public String getLeaderboard() {
        StringBuilder leaderboard = new StringBuilder("Leaderboard:\n");
        userRepository.findAll().stream()
                .sorted((u1, u2) -> Integer.compare(u2.getPoints(), u1.getPoints()))
                .limit(3)
                .forEach(user -> leaderboard.append(user.getUsername()).append(": ").append(user.getPoints()).append(" points\n"));
        return leaderboard.toString();
    }
}