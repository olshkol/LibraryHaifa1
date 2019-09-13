package telran.library.service.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import telran.library.domain.entities.*;
import telran.library.dto.Book;
import telran.library.dto.PublisherAuthor;
import telran.library.dto.Reader;
import telran.library.dto.Record;

@SpringBootConfiguration
public class Config {     // необходимо для того, чтобы аутоварить ModelMapper в AbstractMapper (также необходима депенденси в поме)

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);
        return mapper;
    }

    @Bean
    public Mapper<AuthorEntity, PublisherAuthor> authorMapper() {
        return new AuthorMapper(AuthorEntity.class, PublisherAuthor.class);
    }

    @Bean
    public Mapper<PublisherEntity, PublisherAuthor> publisherMapper() {
        return new PublisherMapper(PublisherEntity.class, PublisherAuthor.class);
    }

    @Bean
    public Mapper<BookEntity, Book> bookMapper() {
        return new BookMapper(BookEntity.class, Book.class);
    }

    @Bean
    public Mapper<ReaderEntity, Reader> readerMapper() {
        return new ReaderMapper(ReaderEntity.class, Reader.class);
    }

    @Bean
    public Mapper<RecordEntity, Record> recordMapper() {
        return new RecordMapper(RecordEntity.class, Record.class);
    }
}
