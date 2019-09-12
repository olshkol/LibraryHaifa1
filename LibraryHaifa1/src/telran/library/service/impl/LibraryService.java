package telran.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.library.domain.entities.*;
import telran.library.dto.*;
import telran.library.service.interfaces.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LibraryService implements ILibrary {
    final BookRepository bookRepository;
    final AuthorRepository authorRepository;
    final PublisherRepository publisherRepository;
    final ReaderRepository readerRepository;
    final RecordRepository recordRepository;

    @Override
    public LibReturnCode addBookItem(Book book) {
        if (bookRepository.existsById(book.getIsbn()))
            return LibReturnCode.BOOK_ALREADY_EXISTS;

        PublisherEntity publisherEntity = publisherRepository.findById(book.getPublisherName()).orElse(null);
        if (publisherEntity == null)
            return LibReturnCode.PUBLISHER_NOT_EXISTS;

        List<AuthorEntity> authorEntities = authorRepository.findAllById(book.getAuthors());
        if (authorEntities.isEmpty())
            return LibReturnCode.AUTHOR_NOT_EXISTS;

        BookEntity bookEntity = new BookEntity(book, new HashSet<>(authorEntities), publisherEntity);

        bookRepository.save(bookEntity);
        return LibReturnCode.OK;
    }

    @Override
    public LibReturnCode addBookExemplar(long isbn, int amount) {
        // TODO (11.09.2019) (addBookExemplar)
        return null;
    }

    @Override
    public Book getBookItem(long isbn) {
        Optional<BookEntity> book = bookRepository.findById(isbn);
        return book.map(BookEntity::getBookDTO).orElse(null);
    }

    @Override
    public LibReturnCode moveToArchive(long isbn) {
        // TODO (11.09.2019) (moveToArchive)
        return null;
    }

    @Override
    public LibReturnCode removeExemplar(long isbn) {
        // TODO (11.09.2019) (removeExemplar)
        return null;
    }

    @Override
    public LibReturnCode lostExemplar(long isbn, long readerId) {
        // TODO (11.09.2019) (lostExemplar)
        return null;
    }

    @Override
    public LibReturnCode addReader(Reader reader) {
        if (reader == null)
            return LibReturnCode.WRONG_DATA;
        if (readerRepository.existsById(reader.getId()))
            return LibReturnCode.READER_ALREADY_EXISTS;

        ReaderEntity readerEntity = new ReaderEntity(reader);

        readerRepository.save(readerEntity);
        return LibReturnCode.OK;
    }

    @Override
    public Reader getReader(long readerId) {
        // TODO (11.09.2019) (getReader)
        return null;
    }

    @Override
    public LibReturnCode updateReaderEmail(long readerId, String email) {
        // TODO (11.09.2019) (updateReaderEmail)
        return null;
    }

    @Override
    public LibReturnCode updateReaderPhone(long readerId, String phone) {
        // TODO (11.09.2019) (updateReaderPhone)
        return null;
    }

    @Override
    public LibReturnCode updateReaderAddress(long readerId, String address) {
        // TODO (11.09.2019) (updateReaderAddress)
        return null;
    }

    @Override
    public LibReturnCode addPublisher(PublisherAuthor publisher) {
        if (publisher == null ||
                publisher.getName() == null || publisher.getName().isEmpty() ||
                publisher.getCountry() == null || publisher.getCountry().isEmpty())
            return LibReturnCode.WRONG_DATA;
        if (publisherRepository.existsById(publisher.getName()))
            return LibReturnCode.PUBLISHER_ALREADY_EXISTS;
        publisherRepository.save(new PublisherEntity(publisher));
        return LibReturnCode.OK;
    }

    @Override
    public PublisherAuthor getPublisherByName(String publisherName) {
        Optional<PublisherEntity> publisher = publisherRepository.findById(publisherName);
        return publisher.map(PublisherEntity::getPublisherDTO).orElse(null);
    }

    @Override
    public List<PublisherAuthor> getPublishersByCountry(String country) {
        return publisherRepository.getPublisherEntitiesByCountry(country)
                .stream()
                .map(PublisherEntity::getPublisherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PublisherAuthor getPublisherByBook(long isbn) {
        Optional<BookEntity> bookEntity = bookRepository.findById(isbn);
        return bookEntity.map(entity -> (entity.getPublisher().getPublisherDTO())).orElse(null);
    }

    @Override
    public LibReturnCode addAuthor(PublisherAuthor author) {
        if (author == null ||
                author.getName() == null || author.getName().isEmpty() ||
                author.getCountry() == null || author.getCountry().isEmpty())
            return LibReturnCode.WRONG_DATA;
        if (authorRepository.existsById(author.getName()))
            return LibReturnCode.AUTHOR_ALREADY_EXISTS;
        authorRepository.save(new AuthorEntity(author.getName(), author.getCountry()));
        return LibReturnCode.OK;
    }

    @Override
    public List<PublisherAuthor> getAuthorsByName(String name) {
        // TODO author one as name is Id
        List<PublisherAuthor> authors = new ArrayList<>();
        Optional<AuthorEntity> authorEntity = authorRepository.findById(name);
        authorEntity.ifPresent(entity -> authors.add(entity.getAuthorDTO()));
        return authors;
    }

    @Override
    public List<PublisherAuthor> getAuthorsByCountry(String country) {
        return authorRepository.getAuthorEntitiesByCountry(country)
                .stream()
                .map(AuthorEntity::getAuthorDTO)
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
    public LibReturnCode pickupBook(long isbn, long readerId, LocalDate pickupDate) {
        BookEntity bookEntity = bookRepository.findById(isbn).orElse(null);
        if (bookEntity == null)
            return LibReturnCode.BOOK_NOT_EXISTS;
        if (bookEntity.getArchivingDate() != null)
            return LibReturnCode.BOOK_IN_ARCHIVE;
        if (bookEntity.getAmountInLibrary() == 0)
            return LibReturnCode.BOOK_IN_USE;

        ReaderEntity readerEntity = readerRepository.findById(readerId).orElse(null);
        if (readerEntity == null)
            return LibReturnCode.READER_NOT_EXISTS;

        List<RecordEntity> recordsByBook = readerEntity.getRecords()
                .stream()
                .filter(recordEntity -> recordEntity.getBook().equals(bookEntity))
                .collect(Collectors.toList());
        if (!recordsByBook.isEmpty())
            return LibReturnCode.READER_BOOK_NOT_RETURN;

        RecordEntity recordEntity = new RecordEntity(pickupDate, bookEntity, readerEntity);
        recordRepository.save(recordEntity);
        return LibReturnCode.OK;
    }

    @Override
    public LibReturnCode returnBook(long isbn, long readerId, LocalDate returnDate) {
        // TODO (11.09.2019) (returnBook)
        return null;
    }

    @Override
    public List<Record> findRecordsByBook(long isbn, LocalDate from, LocalDate to) {
        // TODO (11.09.2019) (findRecordsByBook)
        return null;
    }

    @Override
    public List<Record> findRecordsByReader(long readerId, LocalDate from, LocalDate to) {
        // TODO (11.09.2019) (findRecordsByReader)
        return null;
    }

    @Override
    public List<Record> findRecordsByReturnDate(LocalDate returnDate) {
        // TODO (11.09.2019) (findRecordsByReturnDate)
        return null;
    }

    @Override
    public List<Record> findOpenRecords() {
        // TODO (11.09.2019) (findOpenRecords)
        return null;
    }

    @Override
    public List<Book> getMostPopularBooks(LocalDate from_date, LocalDate to_date, int from_age, int to_age) {
        // TODO (11.09.2019) (getMostPopularBooks)
        return null;
    }

    @Override
    public List<PublisherAuthor> getMostPopularAuthors(LocalDate from, LocalDate to) {
        // TODO (11.09.2019) (getMostPopularAuthors)
        return null;
    }

    @Override
    public List<Reader> getMostActiveReaders(LocalDate from, LocalDate to) {
        // TODO (11.09.2019) (getMostActiveReaders)
        return null;
    }

    @Override
    public List<Reader> getMostDelayingReaders() {
        // TODO (11.09.2019) (getMostDelayingReaders)
        return null;
    }

    @Override
    public List<Book> getBooksNotPickedUp(int days) {
        // TODO (11.09.2019) (getBooksNotPickedUp)
        return null;
    }

    @Override
    public boolean isExistBookInArchive(long isbn) {
        // TODO (11.09.2019) (isExistBookInArchive)
        return false;
    }

    @Override
    public List<Book> getDelayedBooksByReader(long readerId) {
        // TODO (11.09.2019) (getDelayedBooksByReader)
        return null;
    }

    @Override
    public List<ReaderBookDelay> getReadersDelayingBooks() {
        // TODO (11.09.2019) (getReadersDelayingBooks)
        return null;
    }
}


