package com.chiedba.candidat.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.chiedba.candidat.domain.FeedbackTest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackTestDTO implements Serializable {

    private Long id;

    private Float note;

    private String commentaires;

    private AppUserDTO appUser;

    private AssessmentDTO assessment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getNote() {
        return note;
    }

    public void setNote(Float note) {
        this.note = note;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public AssessmentDTO getAssessment() {
        return assessment;
    }

    public void setAssessment(AssessmentDTO assessment) {
        this.assessment = assessment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackTestDTO)) {
            return false;
        }

        FeedbackTestDTO feedbackTestDTO = (FeedbackTestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, feedbackTestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackTestDTO{" +
            "id=" + getId() +
            ", note=" + getNote() +
            ", commentaires='" + getCommentaires() + "'" +
            ", appUser=" + getAppUser() +
            ", assessment=" + getAssessment() +
            "}";
    }
}
