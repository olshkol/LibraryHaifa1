package telran.library.mappers;

import org.springframework.stereotype.Component;

import telran.library.domain.entities.ReaderEntity;
import telran.library.dto.Reader;

@Component
public class ReaderMapper extends AbstractMapper<ReaderEntity, Reader> {


    public ReaderMapper(Class<ReaderEntity> entityClass, Class<Reader> dtoClass) {
        super(entityClass, dtoClass);
    }


}
