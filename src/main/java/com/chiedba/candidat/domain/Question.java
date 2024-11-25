package com.chiedba.candidat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "jhi_order", nullable = false)
    private Integer order;

    @NotNull
    @Column(name = "question", nullable = false)
    private String question;

    @NotNull
    @Column(name = "reponse_correcte", nullable = false)
    private String reponseCorrecte;

    @ManyToMany(mappedBy = "questions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "feedbackTests", "questions" }, allowSetters = true)
    private Set<Assessment> assessments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Question order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getQuestion() {
        return this.question;
    }

    public Question question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponseCorrecte() {
        return this.reponseCorrecte;
    }

    public Question reponseCorrecte(String reponseCorrecte) {
        this.setReponseCorrecte(reponseCorrecte);
        return this;
    }

    public void setReponseCorrecte(String reponseCorrecte) {
        this.reponseCorrecte = reponseCorrecte;
    }

    public Set<Assessment> getAssessments() {
        return this.assessments;
    }

    public void setAssessments(Set<Assessment> assessments) {
        if (this.assessments != null) {
            this.assessments.forEach(i -> i.removeQuestion(this));
        }
        if (assessments != null) {
            assessments.forEach(i -> i.addQuestion(this));
        }
        this.assessments = assessments;
    }

    public Question assessments(Set<Assessment> assessments) {
        this.setAssessments(assessments);
        return this;
    }

    public Question addAssessment(Assessment assessment) {
        this.assessments.add(assessment);
        assessment.getQuestions().add(this);
        return this;
    }

    public Question removeAssessment(Assessment assessment) {
        this.assessments.remove(assessment);
        assessment.getQuestions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            ", question='" + getQuestion() + "'" +
            ", reponseCorrecte='" + getReponseCorrecte() + "'" +
            "}";
    }
}
