package com.api.quizAI.core.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Room
{
    @Id
    private final UUID id = UUID.randomUUID();

    @Column(name = "room_code", nullable = false, unique = true)
    private final String roomCode = generateRandomString();

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "max_number_players")
    private Integer maxNumberOfPlayers;

    @OneToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @OneToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Score> scores = new ArrayList<>();

    private String generateRandomString()
    {
        final String charactersAllowed = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom random = new SecureRandom();

        StringBuilder stringBuilder = new StringBuilder();
        int maxCodeLength = 8;

        for (int i = 0; i < maxCodeLength; i++)
        {
            stringBuilder.append(charactersAllowed.charAt(random.nextInt(0, charactersAllowed.length())));
        }

        return stringBuilder.toString();
    }
}
