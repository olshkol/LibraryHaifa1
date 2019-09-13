package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.library.domain.entities.ReaderEntity;

public interface ReaderRepository extends JpaRepository<ReaderEntity, Long> {


}
