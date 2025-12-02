package vn.clothing.fashion_shop.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shop_managements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopManagement extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }
    @Column(unique = true)
    private String slug;
    @Column(unique = true)
    private String name; // Tên gian hàng
    private String businessName; // Tên công ty
    private String businessNo; // Mã cty
    private Instant businessDateIssue; // Ngày thành lập
    private String businessPlace; // Địa chỉ cty
    private String taxCode; // Mã thuế

    private Integer businessType;

    
    private String accountName;
    private String accountNumber;
    private String bankName;
    private String bankBranch;
    
    private String logo;
    private String thumbnail;
    
    private String businessLicence;
    private String identificationImageFirst;
    private String identificationImageSecond;
    
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    
    private String address;

    // private List<Category> categories;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany( mappedBy = "shopManagement", fetch = FetchType.LAZY)
    @JsonManagedReference
    List<Product> products = new ArrayList<>();
}
