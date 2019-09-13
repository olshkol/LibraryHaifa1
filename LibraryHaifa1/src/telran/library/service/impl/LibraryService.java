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

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LibraryService implements ILibrary {
    BookRepository bookRepository;
    AuthorRepository authorRepository;
    PublisherRepository publisherRepository;
    ReaderRepository readerRepository;
    RecordRepository recordRepository;

    Mapper<BookEntity, Book> bookMapper;
    Mapper<AuthorEntity, PublisherAuthor> authorMapper;
    Mapper<PublisherEntity, PublisherAuthor> publisherMapper;
    Mapper<ReaderEntity, Reader> readerMapper;
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
        if (readerRepository.existsById(reader.getId()))
            return LibReturnCode.READER_ALREADY_EXISTS;
        readerRepository.save(readerMapper.toEntity(reader));
        return LibReturnCode.OK;
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
        if (publisherRepository.existsById(publisher.getName()))
            return LibReturnCode.PUBLISHER_ALREADY_EXISTS;
        publisherRepository.save(publisherMapper.toEntity(publisher));
        return LibReturnCode.OK;
    }

    @Override
    public PublisherAuthor getPublisherByName(String publisherName) {
        Optional<PublisherEntity> publisher = publisherRepository.findById(publisherName);
        return publisher.map(publisherMapper::toDto).orElse(null);
    }

    @Override
    public List<PublisherAuthor> getPublishersByCountry(String country) {
        return publisherRepository.getPublisherEntitiesByCountry(country)
                .stream()
                .map(publisherMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PublisherAuthor getPublisherByBook(long isbn) {
        Optional<BookEntity> bookEntity = bookRepository.findById(isbn);
        return bookEntity.map(entity -> (publisherMapper.toDto(entity.getPublisher()))).orElse(null);
    }

    @Override
    public LibReturnCode addAuthor(PublisherAuthor author) {
        if (authorRepository.existsById(author.getName()))
            return LibReturnCode.AUTHOR_ALREADY_EXISTS;
        AuthorEntity entity = authorMapper.toEntity(author);
        authorRepository.save(entity);
        return LibReturnCode.OK;
    }

    @Override
    public List<PublisherAuthor> getAuthorsByName(String name) {
        // TODO author one as name is Id
        List<PublisherAuthor> authors = new ArrayList<>();
        Optional<AuthorEntity> authorEntity = authorRepository.findById(name);
        authorEntity.ifPresent(entity -> authors.add(authorMapper.toDto(entity)));
        return authors;
    }

    @Override
    public List<PublisherAuthor> getAuthorsByCountry(String country) {
        return authorRepository.getAuthorEntitiesByCountry(country)
                .stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublisherAuthor> getAuthorsByBook(long isbn) {
        Optional<BookEntity> bookEntity = bookRepository.findById(isbn);
        List<PublisherAuthor> authors = new ArrayList<>();
        if (bookEntity.isPresent()) {
            authors = bookEntity.get().getAuthors()
                    .stream()
                    .map(authorEntity -> new PublisherAuthor(authorEntity.getName(), authorEntity.getCountry()))
                    .collect(Collectors.toList());
        }
        return authors;
    }

	@Override
	@Transactional
	public LibReturnCode pickupBook(long isbn, long readerId, LocalDate pickupDate) {

		BookEntity book = bookRepo.findById(isbn).orElse(null);
		
		if (Objects.isNull(book)) {
			return LibReturnCode.BOOK_NOT_EXISTS;
		}
		if (recordRepo.countByBookAndDateOfReturningNull(book) == book.getAmountInLibrary()) {
			return LibReturnCode.ALL_EXEMPLARS_IN_USE;
		}
		if (!Objects.isNull(book.getArchivingDate())) {
			return LibReturnCode.BOOK_IN_ARCHIVE;
		}
		
		ReaderEntity reader = readerRepo.findById(readerId).orElse(null);
		
		if (Objects.isNull(reader)) {
			return LibReturnCode.READER_NOT_EXISTS;
		}
		if (recordRepo.existsByBookAndDateOfReturningIsNullAndReader(book, reader)) {
			return LibReturnCode.READER_BOOK_NOT_RETURN;
		}
		recordRepo.save(new RecordEntity(pickupDate, book, reader));
		return LibReturnCode.OK;
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