package com.eylul.repsy.repsy_assignment.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Table(name = "packages", schema = "repsy")
@Data
@NoArgsConstructor
public class PackageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String version;

    private String author;

    @Column(columnDefinition = "jsonb")
    private Map<String, Object> meta;

    public PackageEntity(String name, String version, String author, Map<String, Object> meta) {
        this.name = name;
        this.version = version;
        this.author = author;
        this.meta = meta;
    }
}