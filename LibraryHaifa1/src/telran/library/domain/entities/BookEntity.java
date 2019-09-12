package telran.library.domain.entities;

import lombok.*;
import telran.library.dto.Book;
import telran.library.dto.SubjectBook;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Enumerated(EnumType.STRING) // enum as string in database
    SubjectBook subject;
    String language;
    @Setter int maxDaysInUse; //setter
    @Setter LocalDate archivingDate; //setter
    @ManyToMany(fetch = FetchType.EAGER)
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

    public BookEntity(Book book, Set<AuthorEntity> authors, PublisherEntity publisher){
        this(book.getIsbn(),
                book.getPublishingYear(),
                book.getTitle(),
                book.getAmountInLibrary(),
                0,
                book.getSubject(),
                book.getLanguage(),
                book.getMaxDaysInUse(),
                authors,
                publisher);
    }

    public Book getBookDTO(){
        return new Book(this.isbn,
                this.publishingYear,
                this.publisher.name,
                this.authors.stream()
                        .map(AuthorEntity::getName)
                        .collect(Collectors.toSet()),
                this.title,
                this.amountInLibrary,
                this.subject,
                this.language,
                this.maxDaysInUse);
    }
}
