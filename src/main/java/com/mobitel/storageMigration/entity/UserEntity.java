package com.mobitel.storageMigration.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "business_center")


public class UserEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private int id;

    @Column(name = "devision")
    private String devision;

    @Column(name = "received_date")
    private String receivedDate;

    @Column(name = "expire_date")
    private String expireDate;

    @Column(name = "box_range")
    private String boxRange;

}
