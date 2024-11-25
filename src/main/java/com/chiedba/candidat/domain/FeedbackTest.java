package com.chiedba.candidat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FeedbackTest.
 */
@Entity
@Table(name = "feedback_test")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "note")
    private Float note;

    @Column(name = "commentaires")
    private String commentaires;

    @OneToMany(mappedBy = "feedbackTest")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "feedbackTest" }, allowSetters = true)
    private Set<Reponse> reponses = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "resume", "feedbackTests" }, allowSetters = true)
    private AppUser appUser;

    @ManyToOne
    @JsonIgnoreProperties(value = { "feedbackTests", "questions" }, allowSetters = true)
    private Assessment assessment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeedbackTest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getNote() {
        return this.note;
    }

    public FeedbackTest note(Float note) {
        this.setNote(note);
        return this;
    }

    public void setNote(Float note) {
        this.note = note;
    }

    public String getCommentaires() {
        return this.commentaires;
    }

    public FeedbackTest commentaires(String commentaires) {
        this.setCommentaires(commentaires);
        return this;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public Set<Reponse> getReponses() {
        return this.reponses;
    }

    public void setReponses(Set<Reponse> reponses) {
        if (this.reponses != null) {
            this.reponses.forEach(i -> i.setFeedbackTest(null));
        }
        if (reponses != null) {
            reponses.forEach(i -> i.setFeedbackTest(this));
        }
        this.reponses = reponses;
    }

    public FeedbackTest reponses(Set<Reponse> reponses) {
        this.setReponses(reponses);
        return this;
    }

    public FeedbackTest addReponse(Reponse reponse) {
        this.reponses.add(reponse);
        reponse.setFeedbackTest(this);
        return this;
    }

    public FeedbackTest removeReponse(Reponse reponse) {
        this.reponses.remove(reponse);
        reponse.setFeedbackTest(null);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public FeedbackTest appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Assessment getAssessment() {
        return this.assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public FeedbackTest assessment(Assessment assessment) {
        this.setAssessment(assessment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackTest)) {
            return false;
        }
        return id != null && id.equals(((FeedbackTest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackTest{" +
            "id=" + getId() +
            ", note=" + getNote() +
            ", commentaires='" + getCommentaires() + "'" +
            "}";
    }
}
