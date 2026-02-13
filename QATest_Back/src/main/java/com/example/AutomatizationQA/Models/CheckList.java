package com.example.AutomatizationQA.Models;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "check_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "last_result")
    private String lastResult;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;
}
