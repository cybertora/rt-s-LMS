package com.education.repository;

import com.education.entity.Enrollment;
import com.education.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class EnrollmentRepository {
    private final SessionFactory sessionFactory;

    public EnrollmentRepository() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public Enrollment findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Enrollment.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(Enrollment enrollment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(enrollment);
            session.getTransaction().commit();
        }
    }

    public List<Enrollment> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Enrollment", Enrollment.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}