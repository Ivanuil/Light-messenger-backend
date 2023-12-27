package edu.example.kafkatest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "webhook")
@Getter
@Setter
public class WebHookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "webhook_seq")
    @SequenceGenerator(name = "webhook_seq", sequenceName = "webhook_seq", allocationSize = 1)
    private Long id;

    @Column(name="url")
    private String url;

    @Column(name="username")
    private String username;

}
