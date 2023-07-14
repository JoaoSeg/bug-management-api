package com.segolin.client.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String status;
    private Timestamp creation;
    private Timestamp lastUpdate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id",
            foreignKey = @ForeignKey(name = "FK_EMPLOYEE_BUG"))
    private Employee employee;

}
