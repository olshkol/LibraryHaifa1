package telran.library.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import telran.library.dto.*;
import telran.library.service.interfaces.ILibrary;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootApplication (scanBasePackages = "telran.library") // components root directory (component, service, repository)
@EnableJpaRepositories(basePackages = "telran.library.service") // repositories
@EntityScan(basePackages = "telran.library.domain.entities") //entities
class LibraryServiceTest {
    static ConfigurableApplicationContext applicationContext;

    static ILibrary libraryService;

    private static final long ISBN1 = 1;
    private static final int PUBLISH_YEAR1 = 2010;
    private static final String PUBLISHER_NAME1 = "publisher1";
    private static final String LANGUAGE1 = "language1";
    private static final int MAX_DAYS1 = 20;
    private static final String COUNTRY1 = "country1";
    private static final String AUTHOR_NAME1 = "author1";
    private static final long READER_ID1 = 1;
    private static final String READER_NAME1 = "Moshe";
    private static final String PHONE1 = "1234567";
    private static final String EMAIL1 = "moshe@gmail.com";
    private static final String ADDRESS1 = "address1";
    private static final int BIRTH_YEAR1 = 1980;
    private static final LocalDate BIRTH_DATE1 = LocalDate.of(BIRTH_YEAR1, 1, 1);
    private static Set<String> authors1 = new HashSet<>(Arrays.asList(AUTHOR_NAME1));
    private static final String TITLE1 = "title1";
    private static final int AMOUNT1 = 10;
    private static final SubjectBook SUBJECT1 = SubjectBook.EDUCATION;

    private static Book book1 = new Book(ISBN1, PUBLISH_YEAR1, PUBLISHER_NAME1, authors1, TITLE1, AMOUNT1, SUBJECT1, LANGUAGE1, MAX_DAYS1);
    private static PublisherAuthor publisher1 = new PublisherAuthor(PUBLISHER_NAME1, COUNTRY1);
    private static PublisherAuthor author1 = new PublisherAuthor(AUTHOR_NAME1, COUNTRY1);
    private static Reader reader1 = new Reader(READER_ID1, READER_NAME1, PHONE1, EMAIL1, ADDRESS1, BIRTH_DATE1);

    @BeforeEach
    void setUp() {
        applicationContext = SpringApplication.run(LibraryServiceTest.class);
        libraryService = applicationContext.getBean(ILibrary.class);

        libraryService.addAuthor(author1);
        libraryService.addPublisher(publisher1);
        libraryService.addBookItem(book1);
        libraryService.addReader(reader1);
    }

    @AfterEach
    void tearDown() {
        applicationContext.close();
    }

    @Test
    void addBookItem() {
        assertEquals(LibReturnCode.BOOK_ALREADY_EXISTS, libraryService.addBookItem(book1));

        Book book2 = new Book(
                ISBN1+10, PUBLISH_YEAR1, "hhhh", authors1, "title",
                5, SUBJECT1,"HH", 10);
        assertEquals(LibReturnCode.PUBLISHER_NOT_EXISTS, libraryService.addBookItem(book2));

        Set<String> authors2 = new HashSet<>();
        authors2.add("111");
        Book book3 = new Book(
                ISBN1+10, PUBLISH_YEAR1, PUBLISHER_NAME1, authors2, "title",
                5, SUBJECT1,"HH", 10);
        assertEquals(LibReturnCode.AUTHOR_NOT_EXISTS, libraryService.addBookItem(book3));

        Book book4 = new Book(
                ISBN1+10, PUBLISH_YEAR1, PUBLISHER_NAME1, authors1, TITLE1,
                5, SUBJECT1,LANGUAGE1, 10);
        assertEquals(LibReturnCode.OK, libraryService.addBookItem(book4));
    }

    @Test
    void addBookExemplar() {
        fail("Not yet implemented");
        //TODO addBookExemplar (Oleg, 11.09.2019)
    }

    @Test
    void getBookItem() {
        assertNull(libraryService.getBookItem(ISBN1+10));
        assertEquals(book1, libraryService.getBookItem(ISBN1));
    }

    @Test
    void moveToArchive() {
        fail("Not yet implemented");
        //TODO moveToArchive (Oleg, 11.09.2019)

    }

    @Test
    void removeExemplar() {
        fail("Not yet implemented");
        //TODO removeExemplar (Oleg, 11.09.2019)

    }

    @Test
    void lostExemplar() {
        fail("Not yet implemented");
        //TODO lostExemplar (Oleg, 11.09.2019)

    }

    @Test
    void addReader() {
        assertEquals(LibReturnCode.READER_ALREADY_EXISTS, libraryService.addReader(reader1));

        Reader reader2 = new Reader(READER_ID1+5, READER_NAME1, PHONE1, EMAIL1, ADDRESS1, BIRTH_DATE1);
        assertEquals(LibReturnCode.OK, libraryService.addReader(reader2));
    }

    @Test
    void getReader() {
        fail("Not yet implemented");
        //TODO getReader (Oleg, 11.09.2019)

    }

    @Test
    void updateReaderEmail() {
        fail("Not yet implemented");
        //TODO updateReaderEmail (Oleg, 11.09.2019)

    }

    @Test
    void updateReaderPhone() {
        fail("Not yet implemented");
        //TODO updateReaderPhone (Oleg, 11.09.2019)

    }

    @Test
    void updateReaderAddress() {
        fail("Not yet implemented");
        //TODO updateReaderAddress (Oleg, 11.09.2019)

    }

    @Test
    void addPublisher() {
        assertEquals(LibReturnCode.PUBLISHER_ALREADY_EXISTS, libraryService.addPublisher(publisher1));
        PublisherAuthor publisher3 = new PublisherAuthor("Publisher2", COUNTRY1);
        assertEquals(LibReturnCode.OK, libraryService.addPublisher(publisher3));
    }

    @Test
    void getPublisherByName() {
        assertNull(libraryService.getPublisherByName("hhh"));
        assertEquals(publisher1, libraryService.getPublisherByName(PUBLISHER_NAME1));
    }

    @Test
    void getPublishersByCountry() {
        assertEquals(new ArrayList<>(), libraryService.getPublishersByCountry("hh"));

        PublisherAuthor publisher2 = new PublisherAuthor("Publisher2", COUNTRY1);
        libraryService.addPublisher(publisher2);
        List<PublisherAuthor> publishers = new ArrayList<>(Arrays.asList(publisher1, publisher2));
        assertEquals(publishers, libraryService.getPublishersByCountry(COUNTRY1));
    }

    @Test
    void getPublisherByBook() {
        assertNull(libraryService.getPublisherByBook(ISBN1+10));
        assertEquals(publisher1, libraryService.getPublisherByBook(ISBN1));
    }

    @Test
    void addAuthor() {
        assertEquals(LibReturnCode.AUTHOR_ALREADY_EXISTS, libraryService.addAuthor(author1));
        PublisherAuthor author3 = new PublisherAuthor("Author2", COUNTRY1);
        assertEquals(LibReturnCode.OK, libraryService.addPublisher(author3));
    }

    @Test
    void getAuthorsByName() {
        assertEquals(new ArrayList<>(),libraryService.getAuthorsByName("hhh"));
        List<PublisherAuthor> authors = new ArrayList<>(Arrays.asList(author1));
        assertEquals(authors, libraryService.getAuthorsByName(AUTHOR_NAME1));
    }

    @Test
    void getAuthorsByCountry() {
        assertEquals(new ArrayList<>(), libraryService.getAuthorsByCountry("hh"));
        PublisherAuthor author2 = new PublisherAuthor("Author2", COUNTRY1);
        libraryService.addAuthor(author2);
        List<PublisherAuthor> authors = new ArrayList<>(Arrays.asList(author1, author2));
        assertEquals(authors, libraryService.getAuthorsByCountry(COUNTRY1));

    }

    @Test
    void getAuthorsByBook() {
        assertEquals(new ArrayList<>() ,libraryService.getAuthorsByBook(ISBN1+10));

        List<PublisherAuthor> authors = new ArrayList<>(Arrays.asList(author1));
        assertEquals(authors, libraryService.getAuthorsByBook(ISBN1));
    }

    @Test
    void pickupBook() {
        fail("Not yet implemented");
        //TODO pickupBook (Oleg, 11.09.2019)

    }

    @Test
    void returnBook() {
        fail("Not yet implemented");
        //TODO returnBook (Oleg, 11.09.2019)

    }

    @Test
    void findRecordsByBook() {
        fail("Not yet implemented");
        //TODO findRecordsByBook (Oleg, 11.09.2019)

    }

    @Test
    void findRecordsByReader() {
        fail("Not yet implemented");
        //TODO findRecordsByReader (Oleg, 11.09.2019)

    }

    @Test
    void findRecordsByReturnDate() {
        fail("Not yet implemented");
        //TODO findRecordsByReturnDate (Oleg, 11.09.2019)

    }

    @Test
    void findOpenRecords() {
        fail("Not yet implemented");
        //TODO findOpenRecords (Oleg, 11.09.2019)

    }

    @Test
    void getBooksNotPickedUp() {
        fail("Not yet implemented");
        //TODO getBooksNotPickedUp (Oleg, 11.09.2019)

    }

    @Test
    void isExistBookInArchive() {
        fail("Not yet implemented");
        //TODO isExistBookInArchive (Oleg, 11.09.2019)

    }

    @Test
    void getDelayedBooksByReader() {
        fail("Not yet implemented");
        //TODO getDelayedBooksByReader (Oleg, 11.09.2019)

    }

    @Test
    void getReadersDelayingBooks() {
        fail("Not yet implemented");
        //TODO getReadersDelayingBooks (Oleg, 11.09.2019)

    }
}