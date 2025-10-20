package com.education.repository;

import com.education.entity.Mentor;
import com.education.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class MentorRepository {
    private final SessionFactory sessionFactory;

    public MentorRepository() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public Mentor save(Mentor mentor) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(mentor);
            session.getTransaction().commit();
            return mentor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Mentor findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Mentor.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean existsById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Mentor.class, id) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}