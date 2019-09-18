package telran.library.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Record {
	long isbn;
	long readerId;
	LocalDate datePickingUp;
	LocalDate dateOfReturning;
	long daysDelayed;
	boolean bookIsLost;

    public Record(long isbn, long readerId, LocalDate datePickingUp) {
        super();
        this.isbn = isbn;
        this.readerId = readerId;
        this.datePickingUp = datePickingUp;
    }
}
