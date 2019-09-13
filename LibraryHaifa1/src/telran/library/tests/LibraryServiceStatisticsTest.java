package telran.library.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import telran.library.dto.Book;
import telran.library.dto.PublisherAuthor;
import telran.library.dto.Reader;
import telran.library.dto.SubjectBook;
import telran.library.service.interfaces.ILibrary;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication(scanBasePackages = "telran.library") // components root directory (component, service, repository)
@EnableJpaRepositories(basePackages = "telran.library.service") // repositories
@EntityScan(basePackages = "telran.library.domain.entities") //entities
class LibraryServiceStatisticsTest {
    private static final int N_BOOKS = 4;
    static ConfigurableApplicationContext ctx;
    static int BIRTH_YEAR_OLD = 1950;
    static int BIRTH_YEAR_YOUNG = 2000;
    static Book[] books;
    static Reader[] readers;
    static LocalDate initialDate = LocalDate.of(2010,1,1);
    static long isbns[] = {1,1,1,2,2,2,3,4,4};
    static long readerIds[] = {1,1,1,1,1,1,1,2,2};
    static ILibrary library;

    @BeforeEach
    void setUp() {
        ctx = SpringApplication.run(LibraryServiceStatisticsTest.class);
        library = ctx.getBean(ILibrary.class);
        createBooks();
        createReaders();
        pickReturn();
    }

    private void pickReturn() {
        for (int i = 0; i < isbns.length - 1; i++) {
            initialDate = initialDate.plusDays(i);
            library.pickupBook(isbns[i], readerIds[i], initialDate);
            library.returnBook(isbns[i], readerIds[i], initialDate);
        }
        library.pickupBook(isbns[isbns.length-1], readerIds[readerIds.length-1], initialDate);
    }

    private void createReaders() {
        library.addReader(new Reader(1, "name1", "phone1", "email1", "address1", LocalDate.of(BIRTH_YEAR_OLD, 1, 1)));
        library.addReader(new Reader(2, "name2", "phone2", "email2", "address2", LocalDate.of(BIRTH_YEAR_YOUNG, 1, 1)));
    }

    private void createBooks() {
        library.addAuthor(new PublisherAuthor("name", "country"));
        library.addPublisher(new PublisherAuthor("name", "country"));
        for (int i = 1; i <= N_BOOKS; i++) {
            library.addBookItem(
                    new Book(i, 1920, "name", new HashSet<>(Arrays.asList("name")), "title",
                            1, SubjectBook.LITERATURE, "en", 20));
        }
    }

    @AfterEach
    void tearDown() {
        //TODO tearDown (Oleg, 12.09.2019)
    }

    @Test
    void getMostPopularBooks() {
        //TODO getMostPopularBooks (Oleg, 12.09.2019)

    }

    @Test
    void getMostPopularAuthors() {
        //TODO getMostPopularAuthors (Oleg, 12.09.2019)

    }

    @Test
    void getMostActiveReaders() {
        //TODO getMostActiveReaders (Oleg, 12.09.2019)

    }

    @Test
    void getMostDelayingReaders() {
        //TODO getMostDelayingReaders (Oleg, 12.09.2019)

    }
}