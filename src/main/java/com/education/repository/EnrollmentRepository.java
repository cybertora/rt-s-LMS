package com.education.repository;

import com.education.entity.Enrollment;
import com.education.entity.Mentor;
import com.education.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class EnrollmentRepository {

    private final SessionFactory sessionFactory;

    public EnrollmentRepository() {

        sessionFactory = new Configuration().configure().addAnnotatedClass(Enrollment.class)
                .addAnnotatedClass(User.class).addAnnotatedClass(Mentor.class)
                .buildSessionFactory();
    }

    public Enrollment save(Enrollment enrollment) {

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            session.saveOrUpdate(enrollment);
            session.getTransaction().commit();
            return enrollment;
        }
    }

    public List<Enrollment> findByUser(User user) {

        try (Session session = sessionFactory.openSession()) {

            return session.createQuery("FROM Enrollment WHERE user = :user", Enrollment.class)
                    .setParameter("user", user).list();
        }
    }
}