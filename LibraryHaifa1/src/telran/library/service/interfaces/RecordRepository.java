package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
	RecordEntity getByBookAndReaderAndDatePickingingUp(BookEntity book, ReaderEntity reader, LocalDate datePickingingUp);
	List<RecordEntity> getByDateOfReturningIsNull();
	List<RecordEntity> getByBookAndDatePickingingUpBetween(BookEntity book, LocalDate from, LocalDate to);
	List<RecordEntity> getByReaderAndDatePickingingUpBetween(ReaderEntity reader, LocalDate from, LocalDate to);
	List<RecordEntity> getByDateOfReturning(LocalDate dateOfReturning);

	@Query("SELECT DISTINCT record.book FROM RecordEntity record " +
			"WHERE (record.reader=:reader) AND " +
			"(FUNCTION('DATEDIFF', 'DAY', record.datePickingingUp, FUNCTION('CURRENT_DATE')) > record.book.maxDaysInUse) " +
			"AND (record.dateOfReturning IS NULL)")
	List<BookEntity> getDelayedBooksByReader(ReaderEntity reader);

	@Query("SELECT record FROM RecordEntity record " +
			"WHERE (FUNCTION('DATEDIFF', 'DAY', record.datePickingingUp, FUNCTION('CURRENT_DATE')) > record.book.maxDaysInUse)" +
			"AND (record.dateOfReturning IS NULL)")
	List<RecordEntity> getReadersDelayingBooks();

//	@Query("SELECT record.book, COUNT (record.book) AS count_r FROM RecordEntity record " +
//			"WHERE (record.datePickingingUp BETWEEN :fromDate AND :toDate) " +
//			"AND (count_r = (SELECT MAX(COUNT (r)) FROM RecordEntity r WHERE (r.datePickingingUp BETWEEN :fromDate AND :toDate)))" +
//			"GROUP BY record.book order by count_r")
//	List<BookEntity> getMostPopularBook(LocalDate fromDate, LocalDate toDate, int fromAge, int toAge);
}
