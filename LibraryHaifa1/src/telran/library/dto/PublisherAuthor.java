package telran.library.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString
public class PublisherAuthor {
    String name;
    String country;
}
