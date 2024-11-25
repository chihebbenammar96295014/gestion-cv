package com.chiedba.candidat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reponse.
 */
@Entity
@Table(name = "reponse")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reponse implements Serializable {

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
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reponses", "appUser", "assessment" }, allowSetters = true)
    private FeedbackTest feedbackTest;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reponse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Reponse order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getContent() {
        return this.content;
    }

    public Reponse content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FeedbackTest getFeedbackTest() {
        return this.feedbackTest;
    }

    public void setFeedbackTest(FeedbackTest feedbackTest) {
        this.feedbackTest = feedbackTest;
    }

    public Reponse feedbackTest(FeedbackTest feedbackTest) {
        this.setFeedbackTest(feedbackTest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reponse)) {
            return false;
        }
        return id != null && id.equals(((Reponse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reponse{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            ", content='" + getContent() + "'" +
            "}";
    }
}
