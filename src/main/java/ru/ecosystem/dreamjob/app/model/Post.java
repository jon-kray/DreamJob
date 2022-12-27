package ru.ecosystem.dreamjob.app.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class Post implements Serializable {

    private long id;

    private String name;

    private String company;

    private String description;

    private LocalDateTime created;
}
