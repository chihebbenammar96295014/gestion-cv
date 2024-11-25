package com.chiedba.candidat.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.chiedba.candidat.domain.ExperiencePro} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExperienceProDTO implements Serializable {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private String title;

    private String description;

    private String fonction;

    private String place;

    private ResumeDTO resume;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public ResumeDTO getResume() {
        return resume;
    }

    public void setResume(ResumeDTO resume) {
        this.resume = resume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExperienceProDTO)) {
            return false;
        }

        ExperienceProDTO experienceProDTO = (ExperienceProDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, experienceProDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExperienceProDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", fonction='" + getFonction() + "'" +
            ", place='" + getPlace() + "'" +
            ", resume=" + getResume() +
            "}";
    }
}
