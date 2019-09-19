package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import telran.library.domain.entities.BookEntity;
import telran.library.domain.entities.ReaderEntity;
import telran.library.domain.entities.RecordEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Integer> {
	int countByBookAndDateOfReturningNull(BookEntity book);
	boolean existsByBookAndDateOfReturningIsNullAndReader(BookEntity book, ReaderEntity reader);
	RecordEntity getByBookAndReaderAndDateOfReturningIsNull(BookEntity book, ReaderEntity reader);
	RecordEntity getByBookAndReaderAndDatePickingUp(BookEntity book, ReaderEntity reader, LocalDate datePickingingUp);
	List<RecordEntity> getByDateOfReturningIsNull();
	List<RecordEntity> getByBookAndDatePickingUpBetween(BookEntity book, LocalDate from, LocalDate to);
	List<RecordEntity> getByReaderAndDatePickingUpBetween(ReaderEntity reader, LocalDate from, LocalDate to);
	List<RecordEntity> getByDateOfReturning(LocalDate dateOfReturning);

	@Query("SELECT DISTINCT record.book FROM RecordEntity record " +
			"WHERE (record.reader=:reader) AND " +
			"(FUNCTION('DATEDIFF', 'DAY', record.datePickingUp, FUNCTION('CURRENT_DATE')) > record.book.maxDaysInUse) " +
			"AND (record.dateOfReturning IS NULL)")
	List<BookEntity> getDelayedBooksByReader(ReaderEntity reader);

	@Query("SELECT record FROM RecordEntity record " +
			"WHERE (FUNCTION('DATEDIFF', 'DAY', record.datePickingUp, FUNCTION('CURRENT_DATE')) > record.book.maxDaysInUse)" +
			"AND (record.dateOfReturning IS NULL)")
	List<RecordEntity> getReadersDelayingBooks();

	@Query(value = "SELECT * FROM readers WHERE readers.id IN " +
			"(SELECT records.reader_id FROM records WHERE records.date_picking_up BETWEEN :fromDate AND :toDate " +
			"GROUP BY records.reader_id HAVING COUNT(*)=(SELECT MAX(counter) FROM " +
			"(SELECT COUNT(*) as counter FROM records WHERE records.date_picking_up BETWEEN :fromDate AND :toDate GROUP BY records.reader_id)))",
			nativeQuery = true)
	List<ReaderEntity> getMostActiveReaders(@Param("fromDate") LocalDate fromDate,
											@Param("toDate") LocalDate toDate);

	@Query(value = "SELECT reader FROM ReaderEntity reader WHERE size(records) = (SELECT max(counter) from ReaderRecords)")
	List<ReaderEntity> getMostActiveReaders();
}
