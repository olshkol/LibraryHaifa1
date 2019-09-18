package telran.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telran.library.domain.entities.*;
import telran.library.dto.*;
import telran.library.mappers.Mapper;
import telran.library.service.interfaces.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LibraryService implements ILibrary {
    private static LocalDate MIN_DATE = LocalDate.of(1900, 1, 1);

    BookRepository bookRepo;
    AuthorRepository authorRepo;
    PublisherRepository publisherRepo;
    ReaderRepository readerRepo;
    RecordRepository recordRepo;

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
        if (authors.size() < book.getAuthors().size())
            return LibReturnCode.AUTHOR_NOT_EXISTS;
        bookRepo.save(bookMapper.toEntity(book));
        return LibReturnCode.OK;
    }

    @Override
    @Transactional
    public LibReturnCode addBookExemplar(long isbn, int amount) {
        BookEntity book = bookRepo.findById(isbn).orElse(null);
        if (Objects.isNull(book)) return LibReturnCode.BOOK_NOT_EXISTS;
        if (book.getArchivingDate() != null) return LibReturnCode.BOOK_IN_ARCHIVE;
        book.setAmountInLibrary(book.getAmountInLibrary() + amount);
        bookRepo.save(book);
        return LibReturnCode.OK;
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookItem(long isbn) {
        BookEntity book = bookRepo.findById(isbn).orElse(null);
        if (Objects.isNull(book)) return null;
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public LibReturnCode moveToArchive(long isbn) {
        BookEntity book = bookRepo.findById(isbn).orElse(null);
        if (Objects.isNull(book)) return LibReturnCode.BOOK_NOT_EXISTS;
        if (book.getArchivingDate() != null) return LibReturnCode.BOOK_ALREADY_IN_ARCHIVE;
        if (recordRepo.countByBookAndDateOfReturningNull(book) != 0)
            return LibReturnCode.BOOK_IN_USE;
        book.setArchivingDate(LocalDate.now());
        bookRepo.save(book);
        return LibReturnCode.OK;
    }

    @Override
    @Transactional
    public LibReturnCode removeExemplar(long isbn) {
        BookEntity book = bookRepo.findById(isbn).orElse(null);
        if (Objects.isNull(book)) return LibReturnCode.BOOK_NOT_EXISTS;
        //fixme book in use?
        if (recordRepo.countByBookAndDateOfReturningNull(book) != 0)
            return LibReturnCode.BOOK_IN_USE;
        bookRepo.deleteById(isbn);
        return LibReturnCode.OK;
    }

    @Override
    @Transactional
    public LibReturnCode lostExemplar(long isbn, long readerId) {
        BookEntity book = bookRepo.findById(isbn).orElse(null);
        if (Objects.isNull(book)) return LibReturnCode.BOOK_NOT_EXISTS;
        ReaderEntity reader = readerRepo.findById(readerId).orElse(null);
        if (Objects.isNull(reader)) return LibReturnCode.READER_NOT_EXISTS;
        RecordEntity record = recordRepo.getByBookAndReaderAndDateOfReturningIsNull(book, reader);
        record.setDateOfReturning(LocalDate.now()); //fixme what to do with date return?
        record.setBookIsLost(true);
        recordRepo.save(record);
        return LibReturnCode.OK;
    }

    @Override
    @Transactional
    public LibReturnCode addReader(Reader reader) {
        if (readerRepo.existsById(reader.getId()))
            return LibReturnCode.READER_ALREADY_EXISTS;
        readerRepo.save(readerMapper.toEntity(reader));
        return LibReturnCode.OK;
    }

    @Override
    public Reader getReader(long readerId) {
        ReaderEntity reader = readerRepo.findById(readerId).orElse(null);
        if (Objects.isNull(reader)) return null;
        return readerMapper.toDto(reader);
    }

    @Override
    @Transactional
    public LibReturnCode updateReaderEmail(long readerId, String email) {
        ReaderEntity reader = readerRepo.findById(readerId).orElse(null);
        if (Objects.isNull(reader)) return LibReturnCode.READER_NOT_EXISTS;
        reader.setEmail(email);
        readerRepo.save(reader);
        return LibReturnCode.OK;
    }

    @Override
    @Transactional
    public LibReturnCode updateReaderPhone(long readerId, String phone) {
        ReaderEntity reader = readerRepo.findById(readerId).orElse(null);
        if (Objects.isNull(reader)) return LibReturnCode.READER_NOT_EXISTS;
        reader.setPhone(phone);
        readerRepo.save(reader);
        return LibReturnCode.OK;
    }

    @Override
    @Transactional
    public LibReturnCode updateReaderAddress(long readerId, String address) {
        ReaderEntity reader = readerRepo.findById(readerId).orElse(null);
        if (Objects.isNull(reader)) return LibReturnCode.READER_NOT_EXISTS;
        reader.setAddress(address);
        readerRepo.save(reader);
        return LibReturnCode.OK;
    }

    @Override
    @Transactional
    public LibReturnCode addPublisher(PublisherAuthor publisher) {
        if (publisherRepo.existsById(publisher.getName()))
            return LibReturnCode.PUBLISHER_ALREADY_EXISTS;
        publisherRepo.save(publisherMapper.toEntity(publisher));
        return LibReturnCode.OK;
    }

    @Override
    public PublisherAuthor getPublisherByName(String publisherName) {
        PublisherEntity publisher = publisherRepo.findById(publisherName).orElse(null);
        if (Objects.isNull(publisher)) return null;
        return publisherMapper.toDto(publisher);
    }

    @Override
    public List<PublisherAuthor> getPublishersByCountry(String country) {
        return publisherRepo.getPublisherEntitiesByCountry(country)
                .stream()
                .map(publisherMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PublisherAuthor getPublisherByBook(long isbn) {
        Optional<BookEntity> optionalBookEntity = bookRepo.findById(isbn);
        return optionalBookEntity.map(bookEntity -> (publisherMapper.toDto(bookEntity.getPublisher()))).orElse(null);
    }

    @Override
    @Transactional
    public LibReturnCode addAuthor(PublisherAuthor author) {
        if (authorRepo.existsById(author.getName()))
            return LibReturnCode.AUTHOR_ALREADY_EXISTS;
        AuthorEntity entity = authorMapper.toEntity(author);
        authorRepo.save(entity);
        return LibReturnCode.OK;
    }

    @Override
    public List<PublisherAuthor> getAuthorsByName(String name) {
        // fixme author one as name is Id
        List<PublisherAuthor> authors = new ArrayList<>();
        Optional<AuthorEntity> authorEntity = authorRepo.findById(name);
        authorEntity.ifPresent(entity -> authors.add(authorMapper.toDto(entity)));
        return authors;
    }

    @Override
    public List<PublisherAuthor> getAuthorsByCountry(String country) {
        return authorRepo.getAuthorEntitiesByCountry(country)
                .stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublisherAuthor> getAuthorsByBook(long isbn) {
        Optional<BookEntity> bookEntity = bookRepo.findById(isbn);
        List<PublisherAuthor> authors = new ArrayList<>();
        if (bookEntity.isPresent()) {
            authors = bookEntity.get().getAuthors()
                    .stream()
                    .map(authorEntity -> authorMapper.toDto(authorEntity))
                    .collect(Collectors.toList());
        }
        return authors;
    }

    @Override
    @Transactional
    public LibReturnCode pickupBook(long isbn, long readerId, LocalDate pickupDate) {
        BookEntity book = bookRepo.findById(isbn).orElse(null);
        if (Objects.isNull(book)) return LibReturnCode.BOOK_NOT_EXISTS;

        if (recordRepo.countByBookAndDateOfReturningNull(book) == book.getAmountInLibrary())
            return LibReturnCode.ALL_EXEMPLARS_IN_USE;

        if (!Objects.isNull(book.getArchivingDate()))
            return LibReturnCode.BOOK_IN_ARCHIVE;

        ReaderEntity reader = readerRepo.findById(readerId).orElse(null);
        if (Objects.isNull(reader))
            return LibReturnCode.READER_NOT_EXISTS;

        if (recordRepo.existsByBookAndDateOfReturningIsNullAndReader(book, reader))
            return LibReturnCode.READER_BOOK_NOT_RETURN;

        recordRepo.save(new RecordEntity(pickupDate, book, reader));
        return LibReturnCode.OK;
    }

    @Override
    @Transactional
    public LibReturnCode returnBook(long isbn, long readerId, LocalDate returnDate) {
        BookEntity book = bookRepo.findById(isbn).orElse(null);
        if (Objects.isNull(book)) return LibReturnCode.BOOK_NOT_EXISTS;
        ReaderEntity reader = readerRepo.findById(readerId).orElse(null);
        if (Objects.isNull(reader)) return LibReturnCode.READER_NOT_EXISTS;
        //fixme if no open record
        RecordEntity record = recordRepo.getByBookAndReaderAndDateOfReturningIsNull(book, reader);
        if (Objects.isNull(record)) return LibReturnCode.RECORD_NOT_FOUND;
        record.setDateOfReturning(returnDate);
        recordRepo.save(record);
        return LibReturnCode.OK;
    }

    @Override
    public List<Record> findRecordsByBook(long isbn, LocalDate from, LocalDate to) {
        BookEntity book = bookRepo.findById(isbn).orElse(null);
        if (Objects.isNull(book)) return new ArrayList<>();

        if (Objects.isNull(from)) from = MIN_DATE;
        if (Objects.isNull(to)) to = LocalDate.now();

        return recordRepo.getByBookAndDatePickingingUpBetween(book, from, to)
                .stream()
                .map(recordEntity -> recordMapper.toDto(recordEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<Record> findRecordsByReader(long readerId, LocalDate from, LocalDate to) {
        ReaderEntity reader = readerRepo.findById(readerId).orElse(null);
        if (Objects.isNull(reader)) return new ArrayList<>();

        if (Objects.isNull(from)) from = MIN_DATE;
        if (Objects.isNull(to)) to = LocalDate.now();

        return recordRepo.getByReaderAndDatePickingingUpBetween(reader, from, to)
                .stream()
                .map(recordEntity -> recordMapper.toDto(recordEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<Record> findRecordsByReturnDate(LocalDate returnDate) {
        if (Objects.isNull(returnDate)) return new ArrayList<>();
        return recordRepo.getByDateOfReturning(returnDate).stream()
                .map(recordEntity -> recordMapper.toDto(recordEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<Record> findOpenRecords() {
        return recordRepo.getByDateOfReturningIsNull().stream()
                .map(recordEntity -> recordMapper.toDto(recordEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getMostPopularBooks(LocalDate from_date, LocalDate to_date, int from_age, int to_age) {
        //todo getMostPopularBooks
        Map<BookEntity, Long> collect = recordRepo.findAll().stream()
                .filter(recordEntity -> {
                    int readerAge = Period.between(LocalDate.now(), recordEntity.getReader().getBirthDate()).getYears();
                    return (recordEntity.getDatePickingingUp().isAfter(from_date) &&
                            recordEntity.getDatePickingingUp().isBefore(to_date)) &&
                            (readerAge > from_age && readerAge < to_age);
                })
                .collect(Collectors.groupingBy(RecordEntity::getBook, TreeMap::new, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (aLong, aLong2) -> aLong, LinkedHashMap::new));

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
        BookEntity book = bookRepo.findById(isbn).orElse(null);
        if (Objects.isNull(book) || Objects.isNull(book.getArchivingDate())) return false;
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getDelayedBooksByReader(long readerId) {
        ReaderEntity reader = readerRepo.findById(readerId).orElse(null);
        if (Objects.isNull(reader)) return new ArrayList<>();
        return recordRepo.getDelayedBooksByReader(reader)
                .stream()
                .map(bookEntity -> bookMapper.toDto(bookEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReaderBookDelay> getReadersDelayingBooks() {
        return recordRepo.getReadersDelayingBooks()
                .stream()
                .map(recordEntity ->
                        new ReaderBookDelay(recordEntity.getBook().getIsbn(),
                                recordEntity.getReader().getId(),
                                ChronoUnit.DAYS.between(recordEntity.getDatePickingingUp(), LocalDate.now()) -
                                        recordEntity.getBook().getMaxDaysInUse()
                        ))
                .collect(Collectors.toList());
    }

    @Override
    public Record getRecord(long isbn, long readerId, LocalDate datePickingingUp) {
        BookEntity book = bookRepo.findById(isbn).orElse(null);
        if (Objects.isNull(book)) return null;
        ReaderEntity reader = readerRepo.findById(readerId).orElse(null);
        if (Objects.isNull(reader)) return null;
        return recordMapper.toDto(recordRepo.getByBookAndReaderAndDatePickingingUp(book, reader, datePickingingUp));
    }
}