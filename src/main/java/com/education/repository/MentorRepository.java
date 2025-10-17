package com.education.repository;

import com.education.entity.Enrollment;
import com.education.entity.Mentor;
import com.education.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MentorRepository {

    private final SessionFactory sessionFactory;

    public MentorRepository() {

        sessionFactory = new Configuration().configure().addAnnotatedClass(Mentor.class)
                .addAnnotatedClass(User.class).addAnnotatedClass(Enrollment.class)
                .buildSessionFactory();
    }

    public Mentor save(Mentor mentor) {

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            session.saveOrUpdate(mentor);
            session.getTransaction().commit();
            return mentor;
        }
    }

    public Mentor findById(Long id) {

        try (Session session = sessionFactory.openSession()) {

            return session.get(Mentor.class, id);
        }
    }
}