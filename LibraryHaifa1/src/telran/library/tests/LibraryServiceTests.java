package telran.library.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import telran.library.dto.Book;
import telran.library.dto.LibReturnCode;
import telran.library.dto.PublisherAuthor;
import telran.library.dto.Reader;
import telran.library.dto.SubjectBook;
import telran.library.service.interfaces.ILibrary;

import java.time.LocalDate;
import java.util.*;
//Three annotations below are because of LibraryServiceTests in not root package
@SpringBootApplication(scanBasePackages = "telran.library")  // components root directory
@EnableJpaRepositories(basePackages = "telran.library.service")  // repositories root directory
@EntityScan(basePackages = "telran.library.domain.entities")// entities root directory

class LibraryServiceTests {
	
	ConfigurableApplicationContext configurableApplicationContext;
	
	private static final long ISBN1 = 1;
	private static final int PUBLISH_YEAR1 = 2010;
	private static final String PUBLISH_NAME1 = "publisher1";
	private static final String AUTHOR_NAME1 = "name1";
	private static final String TITLE1 = "title1";
	private static final int AMOUNT1 = 10;
	private static final SubjectBook SUBJECT1 = SubjectBook.LITERATURE;
	private static final String LANGUGE1 = "en";
	private static final int MAX_DAYS1 = 20;
	private static final String COUNTRY1 = "country1";
	private static final long READER_ID1 = 1;
	private static final String READER_NAME1 = "Moshe";
	private static final String PHONE1 = "1234567";
	private static final String EMAIL1 = "moshe@gmail.com";
	private static final String ADDRESS1 = "address1";
	private static final int BIRTH_YEAR1 = 1980;
	private static final LocalDate BIRTH_DATE1 = LocalDate.of(BIRTH_YEAR1, 1, 1);
	
	ILibrary library;
	Set<String> authors1 = new HashSet<>(Arrays.asList(AUTHOR_NAME1));
	Book book1 = new Book(ISBN1,
			PUBLISH_YEAR1, PUBLISH_NAME1,
			authors1, TITLE1, AMOUNT1, 
			SUBJECT1, LANGUGE1, MAX_DAYS1);
	PublisherAuthor publisher1 = new PublisherAuthor(PUBLISH_NAME1, COUNTRY1);
	PublisherAuthor author1 = new PublisherAuthor(AUTHOR_NAME1, COUNTRY1);
	Reader reader1 = new Reader(READER_ID1, READER_NAME1, PHONE1,
			EMAIL1, ADDRESS1, BIRTH_DATE1);
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		configurableApplicationContext = SpringApplication.run(LibraryServiceTests.class);
		library = configurableApplicationContext.getBean(ILibrary.class);
		library.addAuthor(author1);
		library.addPublisher(publisher1);
		library.addBookItem(book1);
		library.addReader(reader1);
	
	}

	@AfterEach
	void tearDown() throws Exception {
		configurableApplicationContext.close();
	}

	@Test
	void testAddBookItem() {
		assertEquals(LibReturnCode.BOOK_ALREADY_EXISTS,
				library.addBookItem(book1));
		Book book2 = new  Book
				(ISBN1+10, PUBLISH_YEAR1,
						"hhhh", authors1,
						"title", 5, SUBJECT1,
						"HH", 10);
		//assertEquals(,library.addBookItem(book2));
		

	}

	@Test
	void testAddBookExemplar() {
		fail("Not yet implemented");
	}

	@Test
	void testGetBookItem() {
		fail("Not yet implemented");
	}

	@Test
	void testMoveToArchive() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveExemplar() {
		fail("Not yet implemented");
	}

	@Test
	void testLostExemplar() {
		fail("Not yet implemented");
	}

	@Test
	void testAddReader() {
		fail("Not yet implemented");
	}

	@Test
	void testGetReader() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateReaderEmail() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateReaderPhone() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateReaderAddress() {
		fail("Not yet implemented");
	}

	@Test
	void testAddPublisher() {
		fail("Not yet implemented");
	}

	@Test
	void testGetPublisherByName() {
		fail("Not yet implemented");
	}

	@Test
	void testGetPublishersByCountry() {
		fail("Not yet implemented");
	}

	@Test
	void testGetPublisherByBook() {
		fail("Not yet implemented");
	}

	@Test
	void testAddAuthor() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAuthorsByName() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAuthorsByCountry() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAuthorsByBook() {
		fail("Not yet implemented");
	}

	@Test
	void testPickupBook() {
		fail("Not yet implemented");
	}

	@Test
	void testReturnBook() {
		fail("Not yet implemented");
	}

	@Test
	void testFindRecordsByBook() {
		fail("Not yet implemented");
	}

	@Test
	void testFindRecordsByReader() {
		fail("Not yet implemented");
	}

	@Test
	void testFindRecordsByReturnDate() {
		fail("Not yet implemented");
	}

	@Test
	void testFindOpenRecords() {
		fail("Not yet implemented");
	}

	@Test
	void testGetBooksNotPickedUp() {
		fail("Not yet implemented");
	}

	@Test
	void testIsExistBookInArchive() {
		fail("Not yet implemented");
	}

	@Test
	void testGetDelayedBooksByReader() {
		fail("Not yet implemented");
	}

	@Test
	void testGetReadersDelayingBooks() {
		fail("Not yet implemented");
	}

}
