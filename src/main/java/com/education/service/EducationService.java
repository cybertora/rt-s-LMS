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
import java.util.Scanner;
import java.util.stream.StreamSupport;

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
        User user = new User.UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();
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
            System.out.println("Course " + courseName + " not found.");
            return null;
        }
        System.out.println("Enrolling with Certificate: " + withCertificate + ", Mentor: " + withMentor + ", Gamification: " + withGamification);
        Enrollment enrollment = new Enrollment(user, courseName, 0);
        enrollment.setWithCertificate(withCertificate);
        enrollment.setWithMentor(withMentor);
        enrollment.setWithGamification(withGamification);
        enrollmentRepository.save(enrollment);
        notificationService.sendNotification(user, "Enrolled in " + courseName + " successfully.");
        return course; // Возвращаем базовый курс, декораторы будут применены позже
    }

    public void selectMentor(User user, Long mentorId, Enrollment enrollment) {
        Mentor mentor = mentorRepository.findById(mentorId);
        if (mentor != null) {
            enrollment.setMentor(mentor);
            enrollmentRepository.save(enrollment);
            notificationService.sendNotification(user, "Mentor " + mentor.getName() + " assigned to your course.");
        } else {
            System.out.println("Mentor with ID " + mentorId + " not found.");
        }
    }

    public boolean simulateLearning(User user, Enrollment enrollment, Scanner scanner) {
        System.out.println("Simulating learning for " + enrollment.getCourseName() + "...");
        System.out.print("Question: What is 1 + 1? ");
        int userAnswer = scanner.nextInt();
        if (userAnswer == 2) {
            System.out.println("Correct!");
            enrollment.setProgress(enrollment.getProgress() + 20); // Увеличиваем прогресс на 20%
        } else {
            System.out.println("Wrong! The correct answer is 2.");
        }
        System.out.println("Learning session completed. Progress: " + enrollment.getProgress() + "%.");
        return userAnswer == 2;
    }

    public void startLearning(User user, Enrollment enrollment, Scanner scanner) {
        // Пересоздаём decoratedCourse на основе сохранённых настроек
        Course baseCourse = availableCourses.get(enrollment.getCourseName());
        if (baseCourse == null) {
            System.out.println("Error: Base course not found for " + enrollment.getCourseName());
            return;
        }
        Course course = baseCourse;
        if (enrollment.isWithMentor()) {
            course = new MentorSupportDecorator(course);
        }
        if (enrollment.isWithCertificate()) {
            course = new CertificateDecorator(course);
        }
        if (enrollment.isWithGamification()) {
            course = new GamificationDecorator(course);
        }
        enrollment.setDecoratedCourse(course); // Устанавливаем пересозданный курс
        if (course == null) {
            System.out.println("Error: No decorated course created for " + enrollment.getCourseName() + ". Check enrollment setup.");
            return;
        }
        int currentProgress = enrollment.getProgress();
        System.out.println("Starting learning for " + enrollment.getCourseName() + ", current progress: " + currentProgress +
                ", is GamificationDecorator: " + (course instanceof GamificationDecorator) +
                ", is CertificateDecorator: " + (course instanceof CertificateDecorator));
        if (currentProgress < 100) {
            if (simulateLearning(user, enrollment, scanner)) {
                if (enrollment.getProgress() >= 100) {
                    enrollment.setProgress(100);
                    notificationService.sendNotification(user, "Congratulations, " + user.getUsername() + "! You have completed " +
                            enrollment.getCourseName() + " and received a certificate!");
                    if (course instanceof CertificateDecorator) {
                        ((CertificateDecorator) course).issueCertificate(user, enrollment.getCourseName());
                    }
                }
                if (course instanceof GamificationDecorator && ((GamificationDecorator) course).shouldUpdateProgress()) {
                    user.setPoints(user.getPoints() + (enrollment.getProgress() >= 100 ? 100 : 20));
                    userRepository.save(user);
                    System.out.println("Gamification applied, new points: " + user.getPoints());
                }
                enrollmentRepository.save(enrollment);
            }
        } else {
            notificationService.sendNotification(user, "Course " + enrollment.getCourseName() + " already completed.");
            if (course instanceof CertificateDecorator) {
                ((CertificateDecorator) course).issueCertificate(user, enrollment.getCourseName());
                System.out.println("Congratulations, " + user.getUsername() + "! You have already received a certificate for " +
                        enrollment.getCourseName() + ".");
            }
        }
    }

    public String getLeaderboard() {
        StringBuilder leaderboard = new StringBuilder("Leaderboard:\n");
        Iterable<User> users = userRepository.findAll();
        if (users != null) {
            StreamSupport.stream(users.spliterator(), false)
                    .sorted((u1, u2) -> Integer.compare(u2.getPoints(), u1.getPoints()))
                    .limit(3)
                    .forEach(user -> leaderboard.append(user.getUsername()).append(": ").append(user.getPoints()).append(" points\n"));
        } else {
            leaderboard.append("No users found.");
        }
        return leaderboard.toString();
    }
}