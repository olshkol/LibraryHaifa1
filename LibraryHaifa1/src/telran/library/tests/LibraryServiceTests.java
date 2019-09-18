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

@SpringBootApplication(scanBasePackages = "telran.library") //components root directory (component, service, repository)
@EnableJpaRepositories(basePackages = "telran.library.service") //repositories
@EntityScan(basePackages = "telran.library.domain.entities")
        //entities
class LibraryServiceTests {
    private static ConfigurableApplicationContext applicationContext;

    private static ILibrary libraryService;

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
        applicationContext = SpringApplication.run(LibraryServiceTests.class);
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
                ISBN1 + 10, PUBLISH_YEAR1, "hhhh", authors1, "title",
                5, SUBJECT1, "HH", 10);
        assertEquals(LibReturnCode.PUBLISHER_NOT_EXISTS, libraryService.addBookItem(book2));

        Set<String> authors2 = new HashSet<>();
        authors2.add("111");
        Book book3 = new Book(
                ISBN1 + 10, PUBLISH_YEAR1, PUBLISHER_NAME1, authors2, "title",
                5, SUBJECT1, "HH", 10);
        assertEquals(LibReturnCode.AUTHOR_NOT_EXISTS, libraryService.addBookItem(book3));

        Book book4 = new Book(
                ISBN1 + 10, PUBLISH_YEAR1, PUBLISHER_NAME1, authors1, TITLE1,
                5, SUBJECT1, LANGUAGE1, 10);
        assertEquals(LibReturnCode.OK, libraryService.addBookItem(book4));
    }

    @Test
    void addBookExemplar() {
        assertEquals(LibReturnCode.BOOK_NOT_EXISTS, libraryService.addBookExemplar(ISBN1 + 10, 5));
        int amountInLibrary = libraryService.getBookItem(ISBN1).getAmountInLibrary();
        assertEquals(LibReturnCode.OK, libraryService.addBookExemplar(ISBN1, 5));
        assertEquals(amountInLibrary + 5, libraryService.getBookItem(ISBN1).getAmountInLibrary());
        libraryService.moveToArchive(ISBN1);
        assertEquals(LibReturnCode.BOOK_IN_ARCHIVE, libraryService.addBookExemplar(ISBN1, 5));
    }

    @Test
    void getBookItem() {
        assertNull(libraryService.getBookItem(ISBN1 + 10));
        assertEquals(book1, libraryService.getBookItem(ISBN1));
    }

    @Test
    void moveToArchive() {
        assertEquals(LibReturnCode.BOOK_NOT_EXISTS, libraryService.moveToArchive(ISBN1 + 10));

        assertEquals(LibReturnCode.OK, libraryService.moveToArchive(ISBN1));
        assertEquals(LibReturnCode.BOOK_ALREADY_IN_ARCHIVE, libraryService.moveToArchive(ISBN1));

        Book book2 = new Book(
                ISBN1 + 10, PUBLISH_YEAR1, PUBLISHER_NAME1, authors1, TITLE1, 5, SUBJECT1,
                "EN", 10);
        libraryService.addBookItem(book2);
        libraryService.pickupBook(book2.getIsbn(), READER_ID1, LocalDate.now());
        assertEquals(LibReturnCode.BOOK_IN_USE, libraryService.moveToArchive(book2.getIsbn()));
    }

    @Test
    void removeExemplar() {
        assertEquals(LibReturnCode.BOOK_NOT_EXISTS, libraryService.removeExemplar(ISBN1 + 10));
        assertEquals(LibReturnCode.OK, libraryService.removeExemplar(ISBN1));
        Book book2 = new Book(
                ISBN1 + 10, PUBLISH_YEAR1, PUBLISHER_NAME1, authors1, TITLE1, 5, SUBJECT1,
                "EN", 10);
        libraryService.addBookItem(book2);
        libraryService.pickupBook(book2.getIsbn(), READER_ID1, LocalDate.now());
        assertEquals(LibReturnCode.BOOK_IN_USE, libraryService.moveToArchive(book2.getIsbn()));
    }

    @Test
    void lostExemplar() {
        assertEquals(LibReturnCode.BOOK_NOT_EXISTS, libraryService.lostExemplar(ISBN1 + 10, READER_ID1));
        assertEquals(LibReturnCode.READER_NOT_EXISTS, libraryService.lostExemplar(ISBN1, READER_ID1 + 10));
        libraryService.pickupBook(ISBN1, READER_ID1, LocalDate.of(2010, 1, 1));
        assertEquals(LibReturnCode.OK, libraryService.lostExemplar(ISBN1, READER_ID1));
    }

    @Test
    void addReader() {
        assertEquals(LibReturnCode.READER_ALREADY_EXISTS, libraryService.addReader(reader1));

        Reader reader2 = new Reader(READER_ID1 + 5, READER_NAME1, PHONE1, EMAIL1, ADDRESS1, BIRTH_DATE1);
        assertEquals(LibReturnCode.OK, libraryService.addReader(reader2));
    }

    @Test
    void getReader() {
        assertNull(libraryService.getReader(READER_ID1 + 10));
        assertEquals(reader1, libraryService.getReader(READER_ID1));
    }

    @Test
    void updateReaderEmail() {
        assertEquals(LibReturnCode.READER_NOT_EXISTS,
                libraryService.updateReaderEmail(READER_ID1 + 10, "mail@gmail.com"));
        assertEquals(LibReturnCode.OK,
                libraryService.updateReaderEmail(READER_ID1, "mail@gmail.com"));
    }

    @Test
    void updateReaderPhone() {
        assertEquals(LibReturnCode.READER_NOT_EXISTS,
                libraryService.updateReaderPhone(READER_ID1 + 10, "0521234567"));
        assertEquals(LibReturnCode.OK,
                libraryService.updateReaderPhone(READER_ID1, "0521234567"));
    }

    @Test
    void updateReaderAddress() {
        assertEquals(LibReturnCode.READER_NOT_EXISTS,
                libraryService.updateReaderAddress(READER_ID1 + 10, "Israel Haifa"));
        assertEquals(LibReturnCode.OK,
                libraryService.updateReaderAddress(READER_ID1, "Israel Haifa"));
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
        assertNull(libraryService.getPublisherByBook(ISBN1 + 10));
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
        assertEquals(new ArrayList<>(), libraryService.getAuthorsByName("hhh"));
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
        assertEquals(new ArrayList<>(), libraryService.getAuthorsByBook(ISBN1 + 10));

        List<PublisherAuthor> authors = new ArrayList<>(Arrays.asList(author1));
        assertEquals(authors, libraryService.getAuthorsByBook(ISBN1));
    }

    @Test
    void pickupBook() {
        assertEquals(LibReturnCode.BOOK_NOT_EXISTS, libraryService.pickupBook(ISBN1 + 10, READER_ID1, LocalDate.now()));

        Book book2 = new Book(
                ISBN1 + 10, PUBLISH_YEAR1, PUBLISHER_NAME1, authors1, TITLE1, 1, SUBJECT1,
                "EN", 10);
        libraryService.addBookItem(book2);
        libraryService.pickupBook(ISBN1 + 10, READER_ID1, LocalDate.now());
        Reader reader2 = new Reader(READER_ID1 + 5, READER_NAME1, PHONE1, EMAIL1, ADDRESS1, BIRTH_DATE1);
        libraryService.addReader(reader2);
        assertEquals(LibReturnCode.ALL_EXEMPLARS_IN_USE, libraryService.pickupBook(ISBN1 + 10, reader2.getId(), LocalDate.now()));

        Book book3 = new Book(
                ISBN1 + 15, PUBLISH_YEAR1, PUBLISHER_NAME1, authors1, TITLE1, 5, SUBJECT1,
                "EN", 10);
        book3.setArchivingDate(LocalDate.now());
        libraryService.addBookItem(book3);
        assertEquals(LibReturnCode.BOOK_IN_ARCHIVE, libraryService.pickupBook(ISBN1 + 15, READER_ID1, LocalDate.now()));

        assertEquals(LibReturnCode.READER_NOT_EXISTS, libraryService.pickupBook(ISBN1, READER_ID1 + 15, LocalDate.now()));

        assertEquals(LibReturnCode.OK, libraryService.pickupBook(ISBN1, READER_ID1, LocalDate.now()));

        assertEquals(LibReturnCode.READER_BOOK_NOT_RETURN, libraryService.pickupBook(ISBN1, READER_ID1, LocalDate.now()));
    }

    @Test
    void returnBook() {
        assertEquals(LibReturnCode.BOOK_NOT_EXISTS, libraryService.returnBook(ISBN1 + 10, READER_ID1, LocalDate.now()));
        assertEquals(LibReturnCode.READER_NOT_EXISTS, libraryService.returnBook(ISBN1, READER_ID1 + 10, LocalDate.now()));
        assertEquals(LibReturnCode.RECORD_NOT_FOUND, libraryService.returnBook(ISBN1, READER_ID1, LocalDate.now().plusDays(10)));
        libraryService.pickupBook(ISBN1, READER_ID1, LocalDate.now());
        assertEquals(LibReturnCode.OK, libraryService.returnBook(ISBN1, READER_ID1, LocalDate.now().plusDays(10)));
    }

    @Test
    void findRecordsByBook() {
        assertEquals(new ArrayList<>(), libraryService.findRecordsByBook(ISBN1 + 10, null, null));
        libraryService.pickupBook(ISBN1, READER_ID1, LocalDate.now());

        assertEquals(Arrays.asList(libraryService.getRecord(ISBN1, READER_ID1, LocalDate.now())),
                libraryService.findRecordsByBook(ISBN1, LocalDate.of(2010, 1, 1), LocalDate.of(2020, 1, 1)));

        assertEquals(new ArrayList<>(),
                libraryService.findRecordsByBook(ISBN1, LocalDate.of(2010, 1, 1), LocalDate.of(2015, 1, 1)));

        assertEquals(Arrays.asList(libraryService.getRecord(ISBN1, READER_ID1, LocalDate.now())),
                libraryService.findRecordsByBook(ISBN1, null, null));
    }

    @Test
    void findRecordsByReader() {
        assertEquals(new ArrayList<>(), libraryService.findRecordsByReader(READER_ID1 + 10, null, null));
        libraryService.pickupBook(ISBN1, READER_ID1, LocalDate.now());

        assertEquals(Arrays.asList(libraryService.getRecord(ISBN1, READER_ID1, LocalDate.now())),
                libraryService.findRecordsByReader(READER_ID1, LocalDate.of(2010, 1, 1), LocalDate.of(2020, 1, 1)));

        assertEquals(new ArrayList<>(),
                libraryService.findRecordsByReader(READER_ID1, LocalDate.of(2010, 1, 1), LocalDate.of(2015, 1, 1)));

        assertEquals(Arrays.asList(libraryService.getRecord(ISBN1, READER_ID1, LocalDate.now())),
                libraryService.findRecordsByReader(READER_ID1, null, null));

    }

    @Test
    void findRecordsByReturnDate() {
        assertEquals(new ArrayList<>(), libraryService.findRecordsByReturnDate(LocalDate.of(2015, 2, 2)));

        libraryService.pickupBook(ISBN1, READER_ID1, LocalDate.now());
        assertEquals(new ArrayList<>(), libraryService.findRecordsByReturnDate(LocalDate.of(2015, 2, 2)));
        libraryService.returnBook(ISBN1, READER_ID1, LocalDate.now().plusDays(5));
        assertEquals(Arrays.asList(libraryService.getRecord(ISBN1, READER_ID1, LocalDate.now())),
                libraryService.findRecordsByReturnDate(LocalDate.now().plusDays(5)));
    }

    @Test
    void findOpenRecords() {
        assertEquals(new ArrayList<>(), libraryService.findOpenRecords());
        libraryService.pickupBook(ISBN1, READER_ID1, LocalDate.now());
        assertEquals(Arrays.asList(libraryService.getRecord(ISBN1, READER_ID1, LocalDate.now())),
                libraryService.findOpenRecords());
        libraryService.returnBook(ISBN1, READER_ID1, LocalDate.now());
        assertEquals(new ArrayList<>(), libraryService.findOpenRecords());
    }

    @Test
    void getBooksNotPickedUp() {
        fail("Not yet implemented");
        //TODO getBooksNotPickedUp (Oleg, 11.09.2019)
    }

    @Test
    void isExistBookInArchive() {
        assertFalse(libraryService.isExistBookInArchive(ISBN1 + 10));
        assertFalse(libraryService.isExistBookInArchive(ISBN1));
        libraryService.moveToArchive(ISBN1);
        assertTrue(libraryService.isExistBookInArchive(ISBN1));
    }

    @Test
    void getDelayedBooksByReader() {
        assertEquals(new ArrayList<>(), libraryService.getDelayedBooksByReader(READER_ID1));

        libraryService.pickupBook(ISBN1, READER_ID1, LocalDate.now().minusDays(5));
        assertEquals(new ArrayList<>(), libraryService.getDelayedBooksByReader(READER_ID1));
        libraryService.returnBook(ISBN1, READER_ID1, LocalDate.now());

        libraryService.pickupBook(ISBN1, READER_ID1, LocalDate.now().minusDays(MAX_DAYS1 + 10));
        assertEquals(Arrays.asList(libraryService.getBookItem(ISBN1)), libraryService.getDelayedBooksByReader(READER_ID1));
    }

    @Test
    void getReadersDelayingBooks() {
        assertEquals(new ArrayList<>(), libraryService.getReadersDelayingBooks());

        libraryService.pickupBook(ISBN1, READER_ID1, LocalDate.now().minusDays(5));
        assertEquals(new ArrayList<>(), libraryService.getReadersDelayingBooks());
        libraryService.returnBook(ISBN1, READER_ID1, LocalDate.now());

        libraryService.pickupBook(ISBN1, READER_ID1, LocalDate.now().minusDays(MAX_DAYS1 + 10));

        Book book2 = new Book(
                ISBN1 + 10, PUBLISH_YEAR1, PUBLISHER_NAME1, authors1, TITLE1, 1, SUBJECT1,
                "EN", MAX_DAYS1);
        libraryService.addBookItem(book2);
        libraryService.pickupBook(ISBN1 + 10, READER_ID1, LocalDate.now().minusDays(MAX_DAYS1 + 20));
        assertEquals(
                Arrays.asList(
                        new ReaderBookDelay(ISBN1, READER_ID1, 10),
                        new ReaderBookDelay(ISBN1 + 10, READER_ID1, 20)
                ),
                libraryService.getReadersDelayingBooks());
    }
}