package com.chiedba.candidat.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.chiedba.candidat.domain.Competence} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompetenceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Float niveau;

    private ResumeDTO resume;

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

    public Float getNiveau() {
        return niveau;
    }

    public void setNiveau(Float niveau) {
        this.niveau = niveau;
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
        if (!(o instanceof CompetenceDTO)) {
            return false;
        }

        CompetenceDTO competenceDTO = (CompetenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, competenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompetenceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", niveau=" + getNiveau() +
            ", resume=" + getResume() +
            "}";
    }
}
