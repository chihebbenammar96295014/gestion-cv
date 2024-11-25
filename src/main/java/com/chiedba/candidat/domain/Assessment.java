package com.chiedba.candidat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Assessment.
 */
@Entity
@Table(name = "assessment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Assessment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "niveau")
    private String niveau;

    @Column(name = "date")
    private Instant date;

    @OneToMany(mappedBy = "assessment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reponses", "appUser", "assessment" }, allowSetters = true)
    private Set<FeedbackTest> feedbackTests = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_assessment__question",
        joinColumns = @JoinColumn(name = "assessment_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "assessments" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Assessment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Assessment nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Assessment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNiveau() {
        return this.niveau;
    }

    public Assessment niveau(String niveau) {
        this.setNiveau(niveau);
        return this;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Instant getDate() {
        return this.date;
    }

    public Assessment date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Set<FeedbackTest> getFeedbackTests() {
        return this.feedbackTests;
    }

    public void setFeedbackTests(Set<FeedbackTest> feedbackTests) {
        if (this.feedbackTests != null) {
            this.feedbackTests.forEach(i -> i.setAssessment(null));
        }
        if (feedbackTests != null) {
            feedbackTests.forEach(i -> i.setAssessment(this));
        }
        this.feedbackTests = feedbackTests;
    }

    public Assessment feedbackTests(Set<FeedbackTest> feedbackTests) {
        this.setFeedbackTests(feedbackTests);
        return this;
    }

    public Assessment addFeedbackTest(FeedbackTest feedbackTest) {
        this.feedbackTests.add(feedbackTest);
        feedbackTest.setAssessment(this);
        return this;
    }

    public Assessment removeFeedbackTest(FeedbackTest feedbackTest) {
        this.feedbackTests.remove(feedbackTest);
        feedbackTest.setAssessment(null);
        return this;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Assessment questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Assessment addQuestion(Question question) {
        this.questions.add(question);
        question.getAssessments().add(this);
        return this;
    }

    public Assessment removeQuestion(Question question) {
        this.questions.remove(question);
        question.getAssessments().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assessment)) {
            return false;
        }
        return id != null && id.equals(((Assessment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assessment{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", niveau='" + getNiveau() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
