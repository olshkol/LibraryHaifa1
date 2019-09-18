package telran.library.mappers;

import telran.library.domain.entities.AuthorEntity;
import telran.library.dto.PublisherAuthor;

public class AuthorMapper extends AbstractMapper<AuthorEntity, PublisherAuthor> {

    AuthorMapper(Class<AuthorEntity> entityClass,
                 Class<PublisherAuthor> dtoClass) {
        super(entityClass, dtoClass);
    }
}
