package edu.example.light_messenger.repository;

import edu.example.light_messenger.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, String> {
}
