package telran.library.domain.entities;

import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Subselect("SELECT count(*) as counter FROM records GROUP BY reader_id")
public class ReaderRecords {
    @Id
    @GeneratedValue
    int id;

    long counter;

    public ReaderRecords() {
    }

    public ReaderRecords(long counter) {
        this.counter = counter;
    }
}
