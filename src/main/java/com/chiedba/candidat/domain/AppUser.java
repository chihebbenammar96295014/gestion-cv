package com.chiedba.candidat.domain;

import com.chiedba.candidat.domain.enumeration.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Column(name = "num_telephone")
    private String numTelephone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_user", nullable = false)
    private Role typeUser;

    @JsonIgnoreProperties(value = { "competences", "experiencePros", "formations", "appUser" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Resume resume;

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reponses", "appUser", "assessment" }, allowSetters = true)
    private Set<FeedbackTest> feedbackTests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public AppUser nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public AppUser prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public AppUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return this.motDePasse;
    }

    public AppUser motDePasse(String motDePasse) {
        this.setMotDePasse(motDePasse);
        return this;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public AppUser adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumTelephone() {
        return this.numTelephone;
    }

    public AppUser numTelephone(String numTelephone) {
        this.setNumTelephone(numTelephone);
        return this;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public Role getTypeUser() {
        return this.typeUser;
    }

    public AppUser typeUser(Role typeUser) {
        this.setTypeUser(typeUser);
        return this;
    }

    public void setTypeUser(Role typeUser) {
        this.typeUser = typeUser;
    }

    public Resume getResume() {
        return this.resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public AppUser resume(Resume resume) {
        this.setResume(resume);
        return this;
    }

    public Set<FeedbackTest> getFeedbackTests() {
        return this.feedbackTests;
    }

    public void setFeedbackTests(Set<FeedbackTest> feedbackTests) {
        if (this.feedbackTests != null) {
            this.feedbackTests.forEach(i -> i.setAppUser(null));
        }
        if (feedbackTests != null) {
            feedbackTests.forEach(i -> i.setAppUser(this));
        }
        this.feedbackTests = feedbackTests;
    }

    public AppUser feedbackTests(Set<FeedbackTest> feedbackTests) {
        this.setFeedbackTests(feedbackTests);
        return this;
    }

    public AppUser addFeedbackTest(FeedbackTest feedbackTest) {
        this.feedbackTests.add(feedbackTest);
        feedbackTest.setAppUser(this);
        return this;
    }

    public AppUser removeFeedbackTest(FeedbackTest feedbackTest) {
        this.feedbackTests.remove(feedbackTest);
        feedbackTest.setAppUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return id != null && id.equals(((AppUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", motDePasse='" + getMotDePasse() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", numTelephone='" + getNumTelephone() + "'" +
            ", typeUser='" + getTypeUser() + "'" +
            "}";
    }
}
