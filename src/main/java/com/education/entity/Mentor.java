package com.education.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mentors")
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String expertise;

    public Mentor() {}
    public Mentor(String name, String expertise) {

        this.name = name;
        this.expertise = expertise;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getExpertise() {
        return expertise;
    }
    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
}