package com.education;

import com.education.entity.Enrollment;
import com.education.entity.Mentor;
import com.education.entity.User;
import com.education.facade.StudentPortalFacade;
import com.education.repository.EnrollmentRepository;
import com.education.repository.MentorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.setProperty("org.hibernate.logging.level", "OFF");
        System.setProperty("java.util.logging.config.file", "src/main/resources/logging.properties");

        StudentPortalFacade portal = new StudentPortalFacade();
        MentorRepository mentorRepository = new MentorRepository();
        EnrollmentRepository enrollmentRepository = new EnrollmentRepository();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Online Education Platform!");
        System.out.print("Are you a second-year student? (yes/no): ");
        String isSecondYear = scanner.nextLine().trim().toLowerCase();
        if (!isSecondYear.equals("yes")) {
            System.out.println("Sorry, this platform is only for second-year students. Access denied.");
            return;
        }

        User currentUser = null;
        while (true) {
            System.out.print("\nOptions: ");
            if (currentUser == null) {
                System.out.print("1 - Register  2 - Login  ");
            }
            System.out.print("3 - Enroll in a course  4 - Select a mentor  5 - Start learning  6 - View leaderboard  7 - Create Mentor  0 - Exit");
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Очистка буфера

            switch (choice) {
                case 1:
                    if (currentUser == null) {
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        currentUser = portal.registerUser(username, password);
                    } else {
                        System.out.println("You are already logged in.");
                    }
                    break;
                case 2:
                    if (currentUser == null) {
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        currentUser = portal.loginUser(username, password);
                    } else {
                        System.out.println("You are already logged in.");
                    }
                    break;
                case 3:
                    if (currentUser != null) {
                        System.out.println("Available courses: " + portal.getAvailableCourses().keySet());
                        System.out.print("Enter course name: ");
                        String courseName = scanner.nextLine();
                        System.out.print("With certificate? (yes/no): ");
                        boolean withCertificate = scanner.nextLine().trim().toLowerCase().equals("yes");
                        System.out.print("With mentor? (yes/no): ");
                        boolean withMentor = scanner.nextLine().trim().toLowerCase().equals("yes");
                        System.out.print("With gamification? (yes/no): ");
                        boolean withGamification = scanner.nextLine().trim().toLowerCase().equals("yes");
                        portal.enrollUser(currentUser, courseName, withCertificate, withMentor, withGamification);
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;
                case 4:
                    if (currentUser != null) {
                        System.out.print("Enter enrollment ID: ");
                        Long enrollmentId = scanner.nextLong();
                        scanner.nextLine(); // Очистка буфера
                        Enrollment enrollment = enrollmentRepository.findById(enrollmentId);
                        if (enrollment != null) {
                            System.out.print("Enter mentor ID: ");
                            Long mentorId = scanner.nextLong();
                            scanner.nextLine(); // Очистка буфера
                            portal.selectMentor(currentUser, mentorId, enrollment);
                        } else {
                            System.out.println("Enrollment not found.");
                        }
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;
                case 5:
                    if (currentUser != null) {
                        List<Enrollment> enrollments = enrollmentRepository.findAll();
                        if (enrollments != null && !enrollments.isEmpty()) {
                            System.out.println("Available enrollments:");
                            for (int i = 0; i < enrollments.size(); i++) {
                                Enrollment e = enrollments.get(i);
                                if (e.getUser().getId().equals(currentUser.getId())) {
                                    System.out.println(i + " - " + e.getCourseName() + " (Progress: " + e.getProgress() + "%)");
                                }
                            }
                            System.out.print("Select enrollment number: ");
                            int enrollmentIndex = scanner.nextInt();
                            scanner.nextLine(); // Очистка буфера
                            if (enrollmentIndex >= 0 && enrollmentIndex < enrollments.size()) {
                                Enrollment selectedEnrollment = enrollments.get(enrollmentIndex);
                                if (selectedEnrollment.getUser().getId().equals(currentUser.getId())) {
                                    portal.startLearning(currentUser, selectedEnrollment, scanner);
                                } else {
                                    System.out.println("This enrollment does not belong to you.");
                                }
                            } else {
                                System.out.println("Invalid enrollment number.");
                            }
                        } else {
                            System.out.println("No enrollments found.");
                        }
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;
                case 6:
                    if (currentUser != null) {
                        System.out.println(portal.getLeaderboard());
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;
                case 7:
                    System.out.print("Enter mentor name: ");
                    String mentorName = scanner.nextLine();
                    System.out.print("Enter mentor expertise: ");
                    String mentorExpertise = scanner.nextLine();
                    Mentor mentor = new Mentor.MentorBuilder()
                            .setName(mentorName)
                            .setExpertise(mentorExpertise)
                            .build();
                    Mentor savedMentor = mentorRepository.save(mentor);
                    if (savedMentor != null) {
                        System.out.println("Mentor " + mentorName + " with ID " + savedMentor.getId() + " and expertise " + mentorExpertise + " created successfully.");
                    } else {
                        System.out.println("Failed to create mentor.");
                    }
                    break;
                case 0:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}