package telran.library.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "records")
@Getter
@Setter
@Entity
@Table(name = "readers")
public class ReaderEntity {
    public ReaderEntity(long id, String fullName, String phone, String email, String address, LocalDate birthDate) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.birthDate = birthDate;
	}

	@Id
    long id;
    String fullName;
    @Setter String phone;
    @Setter String email;
    @Setter String address;
    LocalDate birthDate;

    @OneToMany(mappedBy = "reader")
    Set<RecordEntity> records;
}
