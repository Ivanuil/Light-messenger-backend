package edu.example.kafkatest.repository;

import edu.example.kafkatest.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, String> {
}
