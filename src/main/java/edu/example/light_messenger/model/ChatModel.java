package edu.example.light_messenger.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="chat", schema = "public")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq")
    @SequenceGenerator(name = "chat_seq", sequenceName = "chat_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1")
    UserModel user1;

    @ManyToOne
    @JoinColumn(name = "user2")
    UserModel user2;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    Set<MessageModel> messages;

}

