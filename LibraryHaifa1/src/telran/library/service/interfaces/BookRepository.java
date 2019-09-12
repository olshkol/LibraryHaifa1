package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.library.domain.entities.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

}
