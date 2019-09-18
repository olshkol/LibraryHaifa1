package telran.library.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "isbn")
@ToString
public class Book {
    long isbn;
    int publishingYear;
    String publisherName;
    Set<String> authors;
    String title;
    int amountInLibrary;
    int amountInUse;
    SubjectBook subject;
    String language;
    int maxDaysInUse;
    LocalDate archivingDate;

    public Book(long isbn, int publishingYear, String publisherName, Set<String> authors, String title,
                int amountInLibrary, SubjectBook subject, String language, int maxDaysInUse) {
        super();
        this.isbn = isbn;
        this.publishingYear = publishingYear;
        this.publisherName = publisherName;
        this.authors = authors;
        this.title = title;
        this.amountInLibrary = amountInLibrary;
        this.subject = subject;
        this.language = language;
        this.maxDaysInUse = maxDaysInUse;
    }
}
