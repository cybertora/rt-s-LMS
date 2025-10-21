package com.education.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "mentors")
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String expertise;
    @Version
    private Long version;

    private Mentor() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getExpertise() { return expertise; }
    public Long getVersion() { return version; }


    public static class MentorBuilder {
        private String name;
        private String expertise;

        public MentorBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public MentorBuilder setExpertise(String expertise) {
            this.expertise = expertise;
            return this;
        }

        public Mentor build() {
            Mentor mentor = new Mentor();
            mentor.name = this.name;
            mentor.expertise = this.expertise;
            return mentor;
        }
    }

    @Override
    public String toString() {
        return "Mentor{id=" + id + ", name='" + name + "', expertise='" + expertise + "', version=" + version + "}";
    }
}