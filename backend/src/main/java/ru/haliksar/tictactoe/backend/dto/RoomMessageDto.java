package ru.haliksar.tictactoe.backend.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
public class RoomMessageDto {
    private String userId;

    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    private Date postDate;
}
