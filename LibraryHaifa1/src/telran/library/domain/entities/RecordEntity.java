package telran.library.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@EqualsAndHashCode(of = "recordId")
@ToString(exclude = {"book", "reader"})
@Getter
@Setter
@Entity
@Table(name = "records", indexes = {@Index(columnList = "date_of_returning"),
        @Index(columnList = "book_isbn"), @Index(columnList = "reader_id")})
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int recordId;
    @Column(name = "date_picking_up")
    LocalDate datePickingUp;
    @Column(name = "date_of_returning")
    LocalDate dateOfReturning = null;
    boolean bookIsLost = false;

    @ManyToOne
    @JoinColumn(name = "book_isbn")
    BookEntity book;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    ReaderEntity reader;

    public RecordEntity(LocalDate datePickingUp, BookEntity book, ReaderEntity reader) {
        super();
        this.datePickingUp = datePickingUp;
        this.book = book;
        this.reader = reader;
    }


}
