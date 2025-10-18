package vn.clothing.fashion_shop.domain;

import java.time.Instant;

import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.clothing.fashion_shop.security.SecurityUtils;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String city;
    private String district;
    private String ward;
    private boolean activated;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


    @PrePersist
    public void handleSetCreatedUser(){
        this.setCreatedBy(SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : null);
        this.setCreatedAt(Instant.now());
    } 

    @PreUpdate
    public void handleSetUpdatedUser(){
        this.setUpdatedBy(SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : null);
        this.setUpdatedAt(Instant.now());
    } 
}
