package org.buglaban.travelapi.model;
// CATEGORY ENTITY - Danh mục tour
import jakarta.persistence.*;
import lombok.*;
import org.buglaban.travelapi.util.CategoryStatus;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends AbstractEntity {

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CategoryStatus status = CategoryStatus.ACTIVE;

    // Self-referencing relationship cho parent category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Category> children = new HashSet<>();

    // Quan hệ One-to-Many với Tour
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Tour> tours = new HashSet<>();

    /*
     * Check if this is a root category (no parent)
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Get tour count for this category
     */
    public int getTourCount() {
        return tours != null ? tours.size() : 0;
    }

}
