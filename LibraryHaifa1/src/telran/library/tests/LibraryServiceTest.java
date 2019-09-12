package telran.library.tests;

import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import telran.library.dto.*;
import telran.library.service.interfaces.ILibrary;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootApplication
@ComponentScan(basePackages = "telran.library.service")
@EnableJpaRepositories(basePackages = "telran.library.service")
@EntityScan(basePackages = "telran.library.domain.entities")
//@SpringBootApplication(scanBasePackages = "telran.library")
class LibraryServiceTest {
    private ConfigurableApplicationContext applicationContext;

    private ILibrary libraryService;

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
    Set<String> authors1 = new HashSet<>(Arrays.asList(AUTHOR_NAME1));
    private static final String TITLE1 = "title1";
    private static final int AMOUNT1 = 10;
    private static final SubjectBook SUBJECT1 = SubjectBook.EDUCATION;

    Book book1 = new Book(ISBN1, PUBLISH_YEAR1, PUBLISHER_NAME1, authors1, TITLE1, AMOUNT1, SUBJECT1, LANGUAGE1, MAX_DAYS1);
    PublisherAuthor publisher1 = new PublisherAuthor(PUBLISHER_NAME1, COUNTRY1);
    PublisherAuthor author1 = new PublisherAuthor(AUTHOR_NAME1, COUNTRY1);
    Reader reader1 = new Reader(READER_ID1, READER_NAME1, PHONE1, EMAIL1, ADDRESS1, BIRTH_DATE1);


    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // TODO (11.09.2019) setUpBeforeClass
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        // TODO (11.09.2019) tearDownAfterClass
    }

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

        Book book3 = new Book(
                ISBN1+10, PUBLISH_YEAR1, PUBLISHER_NAME1, new HashSet<>(), "title",
                5, SUBJECT1,"HH", 10);
        assertEquals(LibReturnCode.AUTHOR_NOT_EXISTS, libraryService.addBookItem(book3));

        Book book4 = new Book(
                ISBN1+10, PUBLISH_YEAR1, PUBLISHER_NAME1, authors1, TITLE1,
                5, SUBJECT1,LANGUAGE1, 10);
        assertEquals(LibReturnCode.OK, libraryService.addBookItem(book4));
    }

    @Test
    void addBookExemplar() {
        //TODO addBookExemplar (Oleg, 11.09.2019)

    }

    @Test
    void getBookItem() {
        assertNull(libraryService.getBookItem(ISBN1+10));

        assertEquals(book1, libraryService.getBookItem(ISBN1));
    }

    @Test
    void moveToArchive() {
        //TODO moveToArchive (Oleg, 11.09.2019)

    }

    @Test
    void removeExemplar() {
        //TODO removeExemplar (Oleg, 11.09.2019)

    }

    @Test
    void lostExemplar() {
        //TODO lostExemplar (Oleg, 11.09.2019)

    }

    @Test
    void addReader() {
        assertEquals(LibReturnCode.WRONG_DATA, libraryService.addReader(null));

        assertEquals(LibReturnCode.READER_ALREADY_EXISTS, libraryService.addReader(reader1));

        Reader reader2 = new Reader(READER_ID1+5, READER_NAME1, PHONE1, EMAIL1, ADDRESS1, BIRTH_DATE1);
        assertEquals(LibReturnCode.OK, libraryService.addReader(reader2));
    }

    @Test
    void getReader() {
        //TODO getReader (Oleg, 11.09.2019)

    }

    @Test
    void updateReaderEmail() {
        //TODO updateReaderEmail (Oleg, 11.09.2019)

    }

    @Test
    void updateReaderPhone() {
        //TODO updateReaderPhone (Oleg, 11.09.2019)

    }

    @Test
    void updateReaderAddress() {
        //TODO updateReaderAddress (Oleg, 11.09.2019)

    }

    @Test
    void addPublisher() {
        assertEquals(LibReturnCode.PUBLISHER_ALREADY_EXISTS, libraryService.addPublisher(publisher1));

        PublisherAuthor publisher2 = new PublisherAuthor();
        assertEquals(LibReturnCode.WRONG_DATA, libraryService.addPublisher(publisher2));

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

        PublisherAuthor author2 = new PublisherAuthor();
        assertEquals(LibReturnCode.WRONG_DATA, libraryService.addPublisher(author2));

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
        //TODO pickupBook (Oleg, 11.09.2019)

    }

    @Test
    void returnBook() {
        //TODO returnBook (Oleg, 11.09.2019)

    }

    @Test
    void findRecordsByBook() {
        //TODO findRecordsByBook (Oleg, 11.09.2019)

    }

    @Test
    void findRecordsByReader() {
        //TODO findRecordsByReader (Oleg, 11.09.2019)

    }

    @Test
    void findRecordsByReturnDate() {
        //TODO findRecordsByReturnDate (Oleg, 11.09.2019)

    }

    @Test
    void findOpenRecords() {
        //TODO findOpenRecords (Oleg, 11.09.2019)

    }

    @Test
    void getBooksNotPickedUp() {
        //TODO getBooksNotPickedUp (Oleg, 11.09.2019)

    }

    @Test
    void isExistBookInArchive() {
        //TODO isExistBookInArchive (Oleg, 11.09.2019)

    }

    @Test
    void getDelayedBooksByReader() {
        //TODO getDelayedBooksByReader (Oleg, 11.09.2019)

    }

    @Test
    void getReadersDelayingBooks() {
        //TODO getReadersDelayingBooks (Oleg, 11.09.2019)

    }
}