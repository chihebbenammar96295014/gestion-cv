package com.chiedba.candidat.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.chiedba.candidat.domain.Reponse} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReponseDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer order;

    @NotNull
    private String content;

    private FeedbackTestDTO feedbackTest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FeedbackTestDTO getFeedbackTest() {
        return feedbackTest;
    }

    public void setFeedbackTest(FeedbackTestDTO feedbackTest) {
        this.feedbackTest = feedbackTest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReponseDTO)) {
            return false;
        }

        ReponseDTO reponseDTO = (ReponseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reponseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReponseDTO{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            ", content='" + getContent() + "'" +
            ", feedbackTest=" + getFeedbackTest() +
            "}";
    }
}
