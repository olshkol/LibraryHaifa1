package telran.library.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootApplication(scanBasePackages = "telran.library")  // components root directory
@EnableJpaRepositories(basePackages = "telran.library.service")  // repositories root directory
@EntityScan(basePackages = "telran.library.domain.entities") // entities root directory
class LibraryServiceStatisticsTests {
    private static final int N_BOOKS = 4;
    static ConfigurableApplicationContext ctx;
    static int BIRTH_YEAR_OLD = 1950;
    static int BIRTH_YEAR_YOUNG = 2000;
    static Book[] books;
    static Reader[] readers;
    static LocalDate initialDate = LocalDate.of(2010, 1, 1);
    static long isbns[] = {1, 1, 1, 2, 2, 2, 3, 4, 4};
    static long readerIds[] = {1, 1, 1, 1, 1, 1, 1, 2, 2};
    static ILibrary library;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        ctx = SpringApplication.run(LibraryServiceStatisticsTests.class);
        library = ctx.getBean(ILibrary.class);
        createBooks();
        createReaders();
        pickReturn();
    }

    private static void pickReturn() {
        for (int i = 0; i < isbns.length - 1; i++) {
            initialDate = initialDate.plusDays(i);
            library.pickupBook(isbns[i], readerIds[i], initialDate);
            library.returnBook(isbns[i], readerIds[i], initialDate);
        }
        library.pickupBook(isbns[isbns.length - 1], readerIds[isbns.length - 1], initialDate);

    }

    private static void createReaders() {
        library.addReader(new Reader(1, "name", "1234567", "email@tt",
                "address", LocalDate.of(BIRTH_YEAR_OLD, 1, 1)));
        library.addReader(new Reader(2, "name", "1234567", "email@tt",
                "address", LocalDate.of(BIRTH_YEAR_YOUNG, 1, 1)));


    }

    private static void createBooks() {
        library.addAuthor(new PublisherAuthor("name", "country"));
        library.addPublisher(new PublisherAuthor("name", "country"));
        for (int i = 1; i <= N_BOOKS; i++) {
            library.addBookItem
                    (new Book(i, 1920, "name",
                            new HashSet<>(Arrays.asList("name")),
                            "title", 10, SubjectBook.LITERATURE,
                            "en", 20));
        }
    }

    @AfterAll
    static void tearDownAfterClass() {
        ctx.close();
    }

    @Test
    void testGetMostPopularBooks() {
        fail("Not yet implemented");
       /* int age1 = LocalDate.now().getYear() - BIRTH_YEAR_YOUNG - 1;
        int age2 = LocalDate.now().getYear() - BIRTH_YEAR_OLD + 1;
        Book bookItem1 = library.getBookItem(isbns[0]);
        Book bookItem2 = library.getBookItem(isbns[3]);
        assertEquals(Arrays.asList(bookItem1, bookItem2), library.getMostPopularBooks(initialDate, LocalDate.now(), age1, age2));*/
    }

    @Test
    void testGetMostPopularAuthors() {
        fail("Not yet implemented");
    }

    @Test
    void testGetMostActiveReaders() {
        assertEquals(Arrays.asList(library.getReader(1)),
                library.getMostActiveReaders(initialDate, LocalDate.now()));
    }

    @Test
    void testGetMostDelayingReaders() {
        assertEquals(Arrays.asList(library.getReader(2)), library.getMostDelayingReaders());
    }
}
