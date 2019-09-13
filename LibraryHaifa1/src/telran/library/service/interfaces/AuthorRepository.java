package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.library.domain.entities.AuthorEntity;

public interface AuthorRepository extends JpaRepository<AuthorEntity, String> {

}
