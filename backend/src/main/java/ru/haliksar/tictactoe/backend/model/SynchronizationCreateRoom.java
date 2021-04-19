package ru.haliksar.tictactoe.backend.model;

import java.time.Clock;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "SynchronizationCreateRoom")
@NoArgsConstructor
public class SynchronizationCreateRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userId;

    private String nickname;

    private int serverPort;

    @Temporal(TemporalType.TIMESTAMP)
    private Date postDate = new Date(Clock.systemUTC().millis());

    public SynchronizationCreateRoom(String userId, int serverPort, String nickname) {
        this.userId = userId;
        this.serverPort = serverPort;
        this.nickname = nickname;
    }
}
