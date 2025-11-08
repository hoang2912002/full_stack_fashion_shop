package vn.clothing.fashion_shop.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.security.SecurityUtils;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }
    
    private String name;
    private String slug;
    private boolean activated;

    // private Instant createdAt;
    // private Instant updatedAt;
    // private String createdBy;
    // private String updatedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"roles"})
    @JoinTable(name = "permission_role", joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions;

    @OneToMany( mappedBy = "role", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    List<User> users;
}
