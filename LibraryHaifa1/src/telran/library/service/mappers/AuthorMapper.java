package telran.library.service.mappers;

import telran.library.domain.entities.AuthorEntity;
import telran.library.dto.PublisherAuthor;

public class AuthorMapper extends AbstractMapper<AuthorEntity, PublisherAuthor> {

    public AuthorMapper(Class<AuthorEntity> entityClass, Class<PublisherAuthor> dtoClass) {
        super(entityClass, dtoClass);
    }
}
