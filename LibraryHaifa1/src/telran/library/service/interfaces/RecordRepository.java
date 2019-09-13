package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import telran.library.domain.entities.BookEntity;
import telran.library.domain.entities.ReaderEntity;
import telran.library.domain.entities.RecordEntity;

public interface RecordRepository extends JpaRepository<RecordEntity, Integer> {
    int countByBookAndDateOfReturningNull(BookEntity book);
    boolean existsByBookAndReaderAndDateOfReturningNull(BookEntity book, ReaderEntity reader);
}