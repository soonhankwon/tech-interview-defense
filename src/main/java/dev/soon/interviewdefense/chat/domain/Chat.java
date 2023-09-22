package dev.soon.interviewdefense.chat.domain;

import dev.soon.interviewdefense.chat.controller.dto.ChatRoomReqDto;
import dev.soon.interviewdefense.chat.controller.dto.DefenseChatRoomReqDto;
import dev.soon.interviewdefense.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChatTopic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private ChatMode mode;

    private Integer aiQuestionsMaxNumber;

    private Integer defenseScore;

    private LocalDateTime createAt;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private final List<ChatMessage> chatMessages = new ArrayList<>();

    public Chat(ChatRoomReqDto dto, User user) {
        this.topic = dto.topic();
        this.user = user;
        this.mode = ChatMode.MENTOR;
        this.aiQuestionsMaxNumber = 100;
        this.defenseScore = 0;
        this.createAt = LocalDateTime.now();
    }

    public Chat(DefenseChatRoomReqDto dto, User user) {
        this.topic = dto.topic();
        this.user = user;
        this.mode = ChatMode.DEFENSE;
        this.aiQuestionsMaxNumber = dto.length().executeRandomCounter();
        this.defenseScore = 0;
        this.createAt = LocalDateTime.now();
    }

    public void saveMessage(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
    }

    public void decreaseAIQuestionMaxNumber() {
        if (this.aiQuestionsMaxNumber == 0)
            return;
        this.aiQuestionsMaxNumber--;
    }

    public boolean isDefenseEnd() {
        return aiQuestionsMaxNumber == 0;
    }

    public void increaseScore(int score) {
        this.defenseScore += score;
    }
}