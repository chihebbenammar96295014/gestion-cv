package com.chiedba.candidat.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.chiedba.candidat.domain.Assessment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssessmentDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    private String description;

    private String niveau;

    private Instant date;

    private Set<QuestionDTO> questions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Set<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionDTO> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssessmentDTO)) {
            return false;
        }

        AssessmentDTO assessmentDTO = (AssessmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assessmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssessmentDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", niveau='" + getNiveau() + "'" +
            ", date='" + getDate() + "'" +
            ", questions=" + getQuestions() +
            "}";
    }
}
