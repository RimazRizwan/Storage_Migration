package com.mobitel.storageMigration.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "primary_key")
public class PrimaryKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "box_id")
    private int boxId;

    @Column(name = "new_box_id", unique = true) // Ensure uniqueness for newBoxId
    private String newBoxId;

    @Column(name = "division")
    private String division;

    @Column(name = "status")
    private String status;


    public PrimaryKeyEntity(String newBoxId, String division, String status) {
        this.newBoxId = newBoxId;
        this.division = division;
        this.status = status;
    }

    public PrimaryKeyEntity() {

    }
}
