package vn.clothing.fashion_shop.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends AbstractAuditingEntity  {
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
    
    // 🔹 category cha
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Category parent;

    // 🔹 danh sách category con
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonManagedReference
    //@JsonIgnoreProperties: Khi serialize Category sang JSON, bỏ qua luôn 2 field children và parent.
    //Do mình để ở relationship cho nên là khi lấy children đầu tiên ra nó kèm theo điều kiện này nên chỉ lấy 1 cấp
    // @JsonIgnoreProperties({"children", "parent"})  // bỏ qua children cấp 2 trở đi
    private Set<Category> children = new HashSet<>();

    @OneToMany( mappedBy = "category", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    List<Product> products;

    //Do đang dùng đệ quy cho nên phải JsonIgnore đến bảng khác để tránh loop
    @OneToMany( mappedBy = "category", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    List<PromotionProduct> promotionProducts = new ArrayList<>();
}
