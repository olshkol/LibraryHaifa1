package telran.library.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import telran.library.domain.entities.AuthorEntity;
import telran.library.domain.entities.BookEntity;
import telran.library.dto.Book;
import telran.library.service.interfaces.AuthorRepository;
import telran.library.service.interfaces.PublisherRepository;
import telran.library.service.interfaces.RecordRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BookMapper extends AbstractMapper<BookEntity, Book> {

    @Autowired
    private AuthorRepository authorRepo;

    @Autowired
    private PublisherRepository publisherRepo;
        
    @Autowired
    private RecordRepository recordRepo;

    BookMapper(Class<BookEntity> entityClass, Class<Book> dtoClass) {
        super(entityClass, dtoClass);
    }

    @PostConstruct
    public void setupMapper(){
        modelMapper.createTypeMap(Book.class, BookEntity.class)
                .addMappings(m->m.skip(BookEntity::setPublisher))
                .addMappings(m->m.skip(BookEntity::setAuthors))
                .addMappings(m->m.skip(BookEntity::setShelf))
                .setPostConverter(toEntityConverter());
        modelMapper.createTypeMap(BookEntity.class, Book.class)
                .addMappings(m->m.skip(Book::setPublisherName))
                .addMappings(m->m.skip(Book::setAuthors))
                .setPostConverter(toDtoConverter());
    }

    @Override
    void mapSpecificFieldsToEntity(Book book, BookEntity bookEntity) {
        bookEntity.setPublisher(publisherRepo
        		.findById(book.getPublisherName()).orElse(null));
        Set<AuthorEntity> authors = new HashSet<>(
        		authorRepo.findAllById(book.getAuthors()));
        bookEntity.setAuthors(authors);
        bookEntity.setShelf(0);
    }

    @Override
    void mapSpecificFieldsToDto(BookEntity bookEntity, Book book) {
        book.setPublisherName(bookEntity.getPublisher().getName());
        book.setAmountInUse(
        		recordRepo.countByBookAndDateOfReturningNull(bookEntity));
        book.setAuthors(bookEntity.getAuthors().stream()
        		.map(AuthorEntity::getName)
        		.collect(Collectors.toSet()));
    }
}
