package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import telran.library.domain.entities.PublisherEntity;

import java.util.List;

public interface PublisherRepository extends JpaRepository<PublisherEntity, String> {
    List<PublisherEntity> getPublisherEntitiesByCountry(String country);
}
