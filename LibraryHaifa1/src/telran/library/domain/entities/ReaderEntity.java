package telran.library.domain.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "records")
@Getter
@Setter
@Entity
@Table(name = "readers")
public class ReaderEntity {
    @Id
    long id;
    String fullName;
    String phone;
    String email;
    String address;
    LocalDate birthDate;

    @OneToMany(mappedBy = "reader")
    Set<RecordEntity> records;

    public ReaderEntity(long id, String fullName, String phone, String email, String address, LocalDate birthDate) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.birthDate = birthDate;
	}
}
