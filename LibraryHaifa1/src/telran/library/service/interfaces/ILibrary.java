package telran.library.service.interfaces;
import telran.library.dto.*;

import java.time.LocalDate;
import java.util.*;
public interface ILibrary extends ILogger {
	LibReturnCode addBookItem ( Book book); 
	LibReturnCode addBookExemplar(long isbn,  int amount); 
	 Book getBookItem (long isbn);
	 LibReturnCode moveToArchive(long isbn);  
	 LibReturnCode removeExemplar(long isbn); 
	 LibReturnCode lostExemplar(long isbn, long readerId); 
	 LibReturnCode addReader(Reader reader); 
	 Reader getReader(long readerId); 
	 LibReturnCode updateReaderEmail(long readerId,  String email);  
	 LibReturnCode updateReaderPhone(long readerId,  String phone);  
	 LibReturnCode updateReaderAddress(long readerId, String address);  
	 LibReturnCode addPublisher(PublisherAuthor publisher); 
	 PublisherAuthor getPublisherByName( String publisherName);
	 List<PublisherAuthor> getPublishersByCountry( String country); 
	 PublisherAuthor getPublisherByBook(long isbn);
	 LibReturnCode addAuthor( PublisherAuthor author);
	 List<PublisherAuthor> getAuthorsByName(String name); 
	 List<PublisherAuthor> getAuthorsByCountry( String country); 
	 List<PublisherAuthor> getAuthorsByBook(long isbn);
	 LibReturnCode pickupBook(long isbn, long readerId,
			 LocalDate pickupDate);  //(OK, BOOK_NOT_EXISTS,
	//READER_NOT_EXISTS, WRONG_DATA, READER_BOOK_NOT_RETURN, ALL_BOOK_IN_USE, BOOK_IN_ARCHIVE)
	 LibReturnCode returnBook(long isbn, long readerId, LocalDate returnDate) ;// (OK, RECORD_NOT_FOUND, BOOK_NOT_EXISTS, READER_NOT_EXISTS, WRONG_DATA)
	 List<Record>findRecordsByBook(long isbn , LocalDate from , LocalDate to ); 
	 List<Record>findRecordsByReader(long readerId, LocalDate from ,
			 LocalDate to ); 
	 List<Record> findRecordsByReturnDate(LocalDate returnDate ); 
	 List<Record> findOpenRecords(); 
	 List<Book> getMostPopularBooks(LocalDate from_date, LocalDate to_date ,
			int from_age,  int to_age ); 
	 List<PublisherAuthor> getMostPopularAuthors(LocalDate from ,
			 LocalDate to ); 
	 List<Reader> getMostActiveReaders(LocalDate from, LocalDate to); 
	 List<Reader> getMostDelayingReaders(); 
	 List<Book> getBooksNotPickedUp(int days); 
	boolean isExistBookInArchive(long isbn);
	 List<Book> getDelayedBooksByReader(long readerId );
	 List<ReaderBookDelay> getReadersDelayingBooks();

}
