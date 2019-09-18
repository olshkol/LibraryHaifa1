package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.library.domain.entities.AuthorEntity;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, String> {
    List<AuthorEntity> getAuthorEntitiesByCountry(String country);
}
