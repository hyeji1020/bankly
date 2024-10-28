package com.project.bankassetor.primary.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "config")
@Data
@Entity
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public String code;

    private String val;

    private String description;

}