package telran.library.mappers;

import telran.library.dto.*;
import telran.library.domain.entities.*;

public class AuthorMapper extends AbstractMapper<AuthorEntity, PublisherAuthor> {

    public AuthorMapper(Class<AuthorEntity> entityClass, 
    		Class<PublisherAuthor> dtoClass) {
        super(entityClass, dtoClass);
    }
}
