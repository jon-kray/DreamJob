package ru.ecosystem.dreamjob.app.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    private long id;

    private String username;

    private String password;
}
