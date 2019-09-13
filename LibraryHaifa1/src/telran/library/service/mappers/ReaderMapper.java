package telran.library.service.mappers;

import telran.library.domain.entities.ReaderEntity;
import telran.library.dto.Reader;

public class ReaderMapper extends AbstractMapper<ReaderEntity, Reader> {


    public ReaderMapper(Class<ReaderEntity> entityClass, Class<Reader> dtoClass) {
        super(entityClass, dtoClass);
    }


}
