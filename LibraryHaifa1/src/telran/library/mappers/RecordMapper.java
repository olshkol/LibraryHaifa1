package telran.library.mappers;


import org.springframework.beans.factory.annotation.Autowired;
import telran.library.domain.entities.RecordEntity;
import telran.library.dto.Record;
import telran.library.service.interfaces.BookRepository;
import telran.library.service.interfaces.ReaderRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class RecordMapper extends AbstractMapper<RecordEntity, Record> {

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private ReaderRepository readerRepo;

    RecordMapper(Class<RecordEntity> entityClass, Class<Record> dtoClass) {
        super(entityClass, dtoClass);
    }

    @PostConstruct
    public void setupMapper(){
        modelMapper.createTypeMap(Record.class,RecordEntity.class)
                .addMappings(m->m.skip(RecordEntity::setBook))
                .addMappings(m->m.skip(RecordEntity::setReader))
                .setPostConverter(toEntityConverter());
        modelMapper.createTypeMap(RecordEntity.class, Record.class)
                .addMappings(m->m.skip(Record::setIsbn))
                .addMappings(m->m.skip(Record::setReaderId))
                .setPostConverter(toDtoConverter());
    }

    @Override
    void mapSpecificFieldsToEntity(Record record, RecordEntity recordEntity) {
        if(!Objects.isNull(record)) {
            recordEntity.setReader(readerRepo.findById(record.getReaderId()).orElse(null));
            recordEntity.setBook(bookRepo.findById(record.getIsbn()).orElse(null));
        }
    }

    @Override
    void mapSpecificFieldsToDto(RecordEntity recordEntity,Record record) {
        if(!Objects.isNull(recordEntity)) {
            record.setIsbn(recordEntity.getBook().getIsbn());
            record.setReaderId(recordEntity.getReader().getId());
            record.setDatePickingUp(recordEntity.getDatePickingingUp());
            long daysInUse = ChronoUnit.DAYS.between(recordEntity.getDatePickingingUp(), LocalDate.now());
            record.setDaysDelayed(daysInUse - recordEntity.getBook().getMaxDaysInUse());
        }
    }

}
