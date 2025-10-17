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


        StudentPortalFacade portal = new StudentPortalFacade();
        MentorRepository mentorRepository = new MentorRepository();
        EnrollmentRepository enrollmentRepository = new EnrollmentRepository();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Online Education Platform!");
        User currentUser = null;

        while (true) {
            if (currentUser == null) {
                System.out.println("\nOptions: 1 - Register, 2 - Login, 0 - Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    currentUser = portal.registerUser(username, password);
                    if (currentUser != null) {
                        System.out.println("Registered successfully.");
                    }
                } else if (choice == 2) {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    currentUser = portal.loginUser(username, password);
                    if (currentUser != null) {
                        System.out.println("Logged in successfully.");
                    } else {
                        System.out.println("Invalid credentials.");
                    }
                } else if (choice == 0) {
                    System.out.println("Exiting...");
                    break;
                }
            } else {
                System.out.println("\nOptions: 1 - Enroll in Course, 2 - Select Mentor, 3 - Start Learning, 4 - View Leaderboard, 5 - Add Mentor, 6 - Logout, 0 - Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    System.out.println("Available courses:");
                    List<String> courseNames = new ArrayList<>(portal.getAvailableCourses().keySet());
                    for (int i = 0; i < courseNames.size(); i++) {
                        System.out.println((i + 1) + " - " + courseNames.get(i));
                    }
                    System.out.print("Enter course number: ");
                    int courseNum = scanner.nextInt() - 1;
                    scanner.nextLine();
                    String courseName = courseNames.get(courseNum);

                    System.out.print("With certificate? (yes/no): ");
                    boolean withCertificate = scanner.nextLine().equalsIgnoreCase("yes");
                    System.out.print("With mentor? (yes/no): ");
                    boolean withMentor = scanner.nextLine().equalsIgnoreCase("yes");
                    System.out.print("With gamification? (yes/no): ");
                    boolean withGamification = scanner.nextLine().equalsIgnoreCase("yes");

                    portal.enrollInCourse(currentUser, courseName, withCertificate, withMentor, withGamification);
                } else if (choice == 2) {
                    List<Enrollment> enrollments = enrollmentRepository.findByUser(currentUser);
                    if (enrollments.isEmpty()) {
                        System.out.println("No enrollments.");
                        continue;
                    }
                    System.out.println("Your enrollments:");
                    for (int i = 0; i < enrollments.size(); i++) {
                        System.out.println((i + 1) + " - " + enrollments.get(i).getCourseName());
                    }
                    System.out.print("Select enrollment number: ");
                    int enrollNum = scanner.nextInt() - 1;
                    scanner.nextLine();
                    Enrollment enrollment = enrollments.get(enrollNum);

                    System.out.print("Enter mentor ID: ");
                    Long mentorId = scanner.nextLong();
                    scanner.nextLine();
                    portal.selectMentor(currentUser, mentorId, enrollment);
                } else if (choice == 3) {
                    List<Enrollment> enrollments = enrollmentRepository.findByUser(currentUser);
                    if (enrollments.isEmpty()) {
                        System.out.println("No enrollments.");
                        continue;
                    }
                    System.out.println("Your enrollments:");
                    for (int i = 0; i < enrollments.size(); i++) {
                        System.out.println((i + 1) + " - " + enrollments.get(i).getCourseName() + " (Progress: " + enrollments.get(i).getProgress() + "%)");
                    }
                    System.out.print("Select enrollment number to learn: ");
                    int enrollNum = scanner.nextInt() - 1;
                    scanner.nextLine();
                    Enrollment enrollment = enrollments.get(enrollNum);
                    portal.startLearning(currentUser, enrollment);
                } else if (choice == 4) {
                    System.out.println(portal.viewLeaderboard());
                } else if (choice == 5) {
                    System.out.print("Enter mentor name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter mentor expertise: ");
                    String expertise = scanner.nextLine();
                    Mentor mentor = new Mentor(name, expertise);
                    mentor = mentorRepository.save(mentor);
                    System.out.println("Mentor added with ID: " + mentor.getId());
                } else if (choice == 6) {
                    currentUser = null;
                    System.out.println("Logged out.");
                } else if (choice == 0) {
                    System.out.println("Exiting...");
                    break;
                }
            }
        }
        scanner.close();
    }
}