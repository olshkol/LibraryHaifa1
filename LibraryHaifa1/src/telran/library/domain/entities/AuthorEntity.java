package telran.library.domain.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString
@Getter
@Setter
@Entity
@Table(name = "authors")
public class AuthorEntity {
    @Id
    String name;
    String country;
}