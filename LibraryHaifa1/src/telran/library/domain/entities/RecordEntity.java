package telran.library.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@EqualsAndHashCode(of = "recordId")
@ToString(exclude = {"book", "reader"})
@Getter

@Entity
@Table(name = "records")
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int recordId;
    LocalDate datePickingingUp;
    @Setter LocalDate dateOfReturning=null;
    @Setter int daysDelayed=0;
    @Setter boolean bookIsLost=false;

    @ManyToOne
     BookEntity book;

    @ManyToOne
    ReaderEntity reader;

	public RecordEntity(LocalDate datePickingingUp, BookEntity book, ReaderEntity reader) {
		super();
		this.datePickingingUp = datePickingingUp;
		this.book = book;
		this.reader = reader;
	}
    
    
}
