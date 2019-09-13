package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.library.domain.entities.PublisherEntity;

public interface PublisherRepository extends JpaRepository<PublisherEntity, String> {

}
