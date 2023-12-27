package edu.example.kafkatest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user", schema = "public")
@Setter
@Getter
public class UserModel {

    @Id
    @Column(name = "username")
    private String username;

    @Column
    private String password;

}
