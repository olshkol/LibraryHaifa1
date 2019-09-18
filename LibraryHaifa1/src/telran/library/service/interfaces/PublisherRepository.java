package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.library.domain.entities.PublisherEntity;

import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, String> {
    List<PublisherEntity> getPublisherEntitiesByCountry(String country);
}
