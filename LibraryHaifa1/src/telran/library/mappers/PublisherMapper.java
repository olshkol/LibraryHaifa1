package telran.library.mappers;

import telran.library.domain.entities.PublisherEntity;
import telran.library.dto.PublisherAuthor;


public class PublisherMapper extends AbstractMapper<PublisherEntity, PublisherAuthor> {

    PublisherMapper(Class<PublisherEntity> entityClass, Class<PublisherAuthor> dtoClass) {
        super(entityClass, dtoClass);
    }
}
