package com.example.AutomatizationQA.Models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "components")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate;


    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "component", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Region> regions;
}

