package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import telran.library.domain.entities.AuthorEntity;

import java.util.List;

public interface AuthorRepository extends JpaRepository<AuthorEntity, String> {
    List<AuthorEntity> getAuthorEntitiesByCountry(String country);
}
