package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.library.domain.entities.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

}
