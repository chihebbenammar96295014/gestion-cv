package com.chiedba.candidat.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.chiedba.candidat.domain.Resume} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResumeDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResumeDTO)) {
            return false;
        }

        ResumeDTO resumeDTO = (ResumeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resumeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResumeDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
