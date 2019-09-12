package telran.library.service.impl;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import telran.library.dto.Book;
import telran.library.dto.LibReturnCode;
import telran.library.dto.PublisherAuthor;
import telran.library.dto.Reader;
import telran.library.dto.ReaderBookDelay;
import telran.library.dto.Record;
import telran.library.mappers.Mapper;
import telran.library.service.interfaces.AuthorRepository;
import telran.library.service.interfaces.BookRepository;
import telran.library.service.interfaces.ILibrary;
import telran.library.service.interfaces.PublisherRepository;
import telran.library.service.interfaces.ReaderRepository;
import telran.library.service.interfaces.RecordRepository;
import telran.library.domain.entities.*;
public class LibraryService implements ILibrary {

	@Autowired
	BookRepository bookRepo;
	@Autowired
	AuthorRepository authorRepo;
	@Autowired
	PublisherRepository publisherRepo;
	@Autowired
	ReaderRepository readerRepo;
	@Autowired
	RecordRepository recordRepo;
	
	@Autowired
	Mapper<BookEntity, Book> bookMapper;
	@Autowired
	Mapper<ReaderEntity, Reader> readerMapper;
	@Autowired
	Mapper<AuthorEntity, PublisherAuthor> authorMapper;
	@Autowired
	Mapper<PublisherEntity, PublisherAuthor> publisherMapper;
	@Autowired
	Mapper<RecordEntity, Record> recordMapper;
	
	
	@Override
	@Transactional
	public LibReturnCode addBookItem(Book book) {
	        if (bookRepo.existsById(book.getIsbn()))
	            return LibReturnCode.BOOK_ALREADY_EXISTS;

	        PublisherEntity publisherEntity = publisherRepo.findById(book.getPublisherName()).orElse(null);
	        if (Objects.isNull(publisherEntity))
	            return LibReturnCode.PUBLISHER_NOT_EXISTS;

	        List<AuthorEntity> authors = authorRepo.findAllById(book.getAuthors());
	        if (authors.size()<book.getAuthors().size())
	            return LibReturnCode.AUTHOR_NOT_EXISTS;
	        bookRepo.save(bookMapper.toEntity(book));
	        return LibReturnCode.OK;
	}

	@Override
	public LibReturnCode addBookExemplar(long isbn, int amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Book getBookItem(long isbn) {
        BookEntity book = bookRepo.findById(isbn).orElse(null);
        if(Objects.isNull(book)) return null;
        return bookMapper.toDto(book);
	}

	@Override
	public LibReturnCode moveToArchive(long isbn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LibReturnCode removeExemplar(long isbn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LibReturnCode lostExemplar(long isbn, long readerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LibReturnCode addReader(Reader reader) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getReader(long readerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LibReturnCode updateReaderEmail(long readerId, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LibReturnCode updateReaderPhone(long readerId, String phone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LibReturnCode updateReaderAddress(long readerId, String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LibReturnCode addPublisher(PublisherAuthor publisher) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PublisherAuthor getPublisherByName(String publisherName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublisherAuthor> getPublishersByCountry(String country) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PublisherAuthor getPublisherByBook(long isbn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LibReturnCode addAuthor(PublisherAuthor author) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublisherAuthor> getAuthorsByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublisherAuthor> getAuthorsByCountry(String country) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublisherAuthor> getAuthorsByBook(long isbn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LibReturnCode pickupBook(long isbn, long readerId, LocalDate pickupDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LibReturnCode returnBook(long isbn, long readerId, LocalDate returnDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> findRecordsByBook(long isbn, LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> findRecordsByReader(long readerId, LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> findRecordsByReturnDate(LocalDate returnDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> findOpenRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getMostPopularBooks(LocalDate from_date, LocalDate to_date, int from_age, int to_age) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublisherAuthor> getMostPopularAuthors(LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reader> getMostActiveReaders(LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reader> getMostDelayingReaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getBooksNotPickedUp(int days) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isExistBookInArchive(long isbn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Book> getDelayedBooksByReader(long readerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReaderBookDelay> getReadersDelayingBooks() {
		// TODO Auto-generated method stub
		return null;
	}

}
