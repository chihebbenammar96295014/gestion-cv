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
 * A Resume.
 */
@Entity
@Table(name = "resume")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "resume")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "resume" }, allowSetters = true)
    private Set<Competence> competences = new HashSet<>();

    @OneToMany(mappedBy = "resume")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "resume" }, allowSetters = true)
    private Set<ExperiencePro> experiencePros = new HashSet<>();

    @OneToMany(mappedBy = "resume")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "resume" }, allowSetters = true)
    private Set<Formation> formations = new HashSet<>();

    @JsonIgnoreProperties(value = { "resume", "feedbackTests" }, allowSetters = true)
    @OneToOne(mappedBy = "resume")
    private AppUser appUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Resume id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Resume description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Competence> getCompetences() {
        return this.competences;
    }

    public void setCompetences(Set<Competence> competences) {
        if (this.competences != null) {
            this.competences.forEach(i -> i.setResume(null));
        }
        if (competences != null) {
            competences.forEach(i -> i.setResume(this));
        }
        this.competences = competences;
    }

    public Resume competences(Set<Competence> competences) {
        this.setCompetences(competences);
        return this;
    }

    public Resume addCompetence(Competence competence) {
        this.competences.add(competence);
        competence.setResume(this);
        return this;
    }

    public Resume removeCompetence(Competence competence) {
        this.competences.remove(competence);
        competence.setResume(null);
        return this;
    }

    public Set<ExperiencePro> getExperiencePros() {
        return this.experiencePros;
    }

    public void setExperiencePros(Set<ExperiencePro> experiencePros) {
        if (this.experiencePros != null) {
            this.experiencePros.forEach(i -> i.setResume(null));
        }
        if (experiencePros != null) {
            experiencePros.forEach(i -> i.setResume(this));
        }
        this.experiencePros = experiencePros;
    }

    public Resume experiencePros(Set<ExperiencePro> experiencePros) {
        this.setExperiencePros(experiencePros);
        return this;
    }

    public Resume addExperiencePro(ExperiencePro experiencePro) {
        this.experiencePros.add(experiencePro);
        experiencePro.setResume(this);
        return this;
    }

    public Resume removeExperiencePro(ExperiencePro experiencePro) {
        this.experiencePros.remove(experiencePro);
        experiencePro.setResume(null);
        return this;
    }

    public Set<Formation> getFormations() {
        return this.formations;
    }

    public void setFormations(Set<Formation> formations) {
        if (this.formations != null) {
            this.formations.forEach(i -> i.setResume(null));
        }
        if (formations != null) {
            formations.forEach(i -> i.setResume(this));
        }
        this.formations = formations;
    }

    public Resume formations(Set<Formation> formations) {
        this.setFormations(formations);
        return this;
    }

    public Resume addFormation(Formation formation) {
        this.formations.add(formation);
        formation.setResume(this);
        return this;
    }

    public Resume removeFormation(Formation formation) {
        this.formations.remove(formation);
        formation.setResume(null);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        if (this.appUser != null) {
            this.appUser.setResume(null);
        }
        if (appUser != null) {
            appUser.setResume(this);
        }
        this.appUser = appUser;
    }

    public Resume appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resume)) {
            return false;
        }
        return id != null && id.equals(((Resume) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resume{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
