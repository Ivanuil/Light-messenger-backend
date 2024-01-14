package edu.example.light_messenger.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name="message", schema = "public")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", sequenceName = "message_seq", allocationSize = 1)
    private Long id;

    @Column(name = "from_username")
    private String from;

    @ManyToOne
    @JoinColumn(name = "to_username")
    private UserModel to;

    @CreationTimestamp
    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "text")
    private String text;

}
