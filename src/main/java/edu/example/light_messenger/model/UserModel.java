package edu.example.light_messenger.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof UserModel userModel)) return false;
        return Objects.equals(username, userModel.username);
    }

}
