package vn.clothing.fashion_shop.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.constants.enumEntity.GenderEnum;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    private String fullName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private Instant dob;
    private String avatar;
    private Integer age;
    private boolean activated;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    // private Instant createdAt;
    // private Instant updatedAt;
    // private String createdBy;
    // private String updatedBy;

    @OneToMany( mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    // @JsonIgnore
    @JsonManagedReference
    List<Address> addresses = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;

    // @PrePersist
    // public void handleSetCreatedUser(){
    //     this.setCreatedBy(SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : null);
    //     this.setCreatedAt(Instant.now());
    // } 

    // @PreUpdate
    // public void handleSetUpdatedUser(){
    //     this.setUpdatedBy(SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : null);
    //     this.setUpdatedAt(Instant.now());
    // } 
}
