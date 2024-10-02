package com.project.bankassetor.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "account")
@Entity
@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "accountNumber", nullable = false)
    private long accountNumber;

    @Column(name = "balance")
    private int balance;

}
