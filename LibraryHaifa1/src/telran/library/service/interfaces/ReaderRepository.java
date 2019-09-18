package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telran.library.domain.entities.ReaderEntity;

@Repository
public interface ReaderRepository extends JpaRepository<ReaderEntity, Long> {


}
