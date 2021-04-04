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
@Table(name = "SynchronizationSetTable")
@NoArgsConstructor
public class SynchronizationSetTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int serverPort;

    private String userId;

    private long roomId;

    private int index;

    @Temporal(TemporalType.TIMESTAMP)
    private Date postDate = new Date(Clock.systemUTC().millis());

    public SynchronizationSetTable(int serverPort, String userId, long roomId, int index) {
        this.serverPort = serverPort;
        this.userId = userId;
        this.roomId = roomId;
        this.index = index;
    }
}
