package com.education.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import com.education.core.Course;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    private String courseName;
    private int progress;
    @ManyToOne
    private Mentor mentor;
    @Transient
    private Course decoratedCourse;
    private boolean withCertificate;
    private boolean withMentor;
    private boolean withGamification;

    public Enrollment() {}
    public Enrollment(User user, String courseName, int progress) {
        this.user = user;
        this.courseName = courseName;
        this.progress = progress;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }
    public Mentor getMentor() { return mentor; }
    public void setMentor(Mentor mentor) { this.mentor = mentor; }
    public Course getDecoratedCourse() { return decoratedCourse; }
    public void setDecoratedCourse(Course decoratedCourse) { this.decoratedCourse = decoratedCourse; }
    public boolean isWithCertificate() { return withCertificate; }
    public void setWithCertificate(boolean withCertificate) { this.withCertificate = withCertificate; }
    public boolean isWithMentor() { return withMentor; }
    public void setWithMentor(boolean withMentor) { this.withMentor = withMentor; }
    public boolean isWithGamification() { return withGamification; }
    public void setWithGamification(boolean withGamification) { this.withGamification = withGamification; }
}