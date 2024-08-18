package com.E1i3.NoExit.domain.chat.dto;

import com.E1i3.NoExit.domain.chat.domain.ChatRoom;
import lombok.*;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;

import javax.persistence.*;

@Getter
@Setter
public class ChatMessage {
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
    private MessageType type;
    private Object content;
    private String sender;
    private String roomId;

}

