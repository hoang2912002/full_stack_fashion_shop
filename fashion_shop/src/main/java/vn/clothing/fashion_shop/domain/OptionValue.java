package vn.clothing.fashion_shop.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "option_values")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionValue extends AbstractAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    private String value;
    private String slug;

    private boolean activated;
    // private Instant createdAt;
    // private Instant updatedAt;
    // private String createdBy;
    // private String updatedBy;

    @OneToMany( mappedBy = "optionValue", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    List<Variant> variants;

    @ManyToOne()
    @JoinColumn(name = "option_id")
    private Option option;
}
