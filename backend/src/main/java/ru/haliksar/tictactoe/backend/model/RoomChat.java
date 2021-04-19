package ru.haliksar.tictactoe.backend.model;

import java.time.Clock;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name = "RoomChat")
@Data
public class RoomChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userId;

    private String nickname;

    @Size(max = 500, message = "А поменьше 500 символов нельзя?")
    private String message;

    @ManyToOne
    @JoinColumn(name = "Room_id", nullable = false)
    private Room room;

    @Temporal(TemporalType.TIMESTAMP)
    private Date postDate = new Date(Clock.systemUTC().millis());
}
