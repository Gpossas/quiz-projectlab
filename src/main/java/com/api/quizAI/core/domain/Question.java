package com.api.quizAI.core.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "questions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Question
{
    @Builder.Default
    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false, name = "question_value")
    private String value;

    @Column(name = "sent_at")
    @JsonIgnore
    private OffsetDateTime sentAt;

    @OneToMany(targetEntity = Answer.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id", nullable = false)
    private Set<Answer> answers;
}
