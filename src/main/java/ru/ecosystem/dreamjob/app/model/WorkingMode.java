package ru.ecosystem.dreamjob.app.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class WorkingMode implements Serializable {

    private long id;

    private Type mode;

    public enum Type {
        REMOTELY("Удаленный режим"),
        IN_THE_OFFICE("Работа в офисе"),
        HYBRID("Гибридный режим");

        @Getter
        private final String named;

        Type(String named) {
            this.named = named;
        }
    }
}
