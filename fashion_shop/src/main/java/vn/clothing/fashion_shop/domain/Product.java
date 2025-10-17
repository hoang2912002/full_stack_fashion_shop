package vn.clothing.fashion_shop.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String slug;

    private Double price;
    private String thumbnail;
    private Integer quantity;
    
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private boolean activated;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne()
    @JoinColumn(name = "manufacture_id")
    private Manufacture manufacture;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;
}
