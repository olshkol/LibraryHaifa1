package telran.library.domain.entities;
import lombok.*;
import telran.library.dto.SubjectBook;

import javax.persistence.*;


import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode(of = "isbn")
@ToString(exclude = {"authors", "publisher", "records"})
@Getter

@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    long isbn;
    int publishingYear;
    String title;
    @Setter int amountInLibrary; //setter
    @Setter int shelf;//setter
    SubjectBook subject;
    String language;
    @Setter int maxDaysInUse; //setter
    @Setter LocalDate archivingDate; //setter
    @ManyToMany
    Set<AuthorEntity> authors;

    @ManyToOne
    PublisherEntity publisher; 

    @OneToMany(mappedBy = "book")
    Set<RecordEntity> records;

	public BookEntity(long isbn, int publishingYear, String title, int amountInLibrary, int shelf, SubjectBook subject,
			String language, int maxDaysInUse, Set<AuthorEntity> authors, PublisherEntity publisher) {
		super();
		this.isbn = isbn;
		this.publishingYear = publishingYear;
		this.title = title;
		this.amountInLibrary = amountInLibrary;
		this.shelf = shelf;
		this.subject = subject;
		this.language = language;
		this.maxDaysInUse = maxDaysInUse;
		this.authors = authors;
		this.publisher = publisher;
	}
    
}
