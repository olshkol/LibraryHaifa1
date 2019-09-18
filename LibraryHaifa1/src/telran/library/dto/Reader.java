package telran.library.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Reader {
	long id;
	String fullName;
	String phone;
	String email;
	String address;
	LocalDate birthDate;
}
