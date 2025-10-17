package com.education.repository;

import com.education.entity.Enrollment;
import com.education.entity.Mentor;
import com.education.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepository() {

        sessionFactory = new Configuration().configure().addAnnotatedClass(User.class)
                .addAnnotatedClass(Mentor.class).addAnnotatedClass(Enrollment.class)
                .buildSessionFactory();
    }

    public User save(User user) {

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            session.saveOrUpdate(user);
            session.getTransaction().commit();
            return user;
        }
    }

    public User findByUsername(String username) {

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username).uniqueResult();
        }
    }

    public List<User> findAll() {

        try (Session session = sessionFactory.openSession()) {

            return session.createQuery("FROM User", User.class).list();
        }
    }
}