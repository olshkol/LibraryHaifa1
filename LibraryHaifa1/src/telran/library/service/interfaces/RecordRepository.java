package telran.library.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import telran.library.domain.entities.RecordEntity;

public interface RecordRepository extends JpaRepository<RecordEntity, Integer> {
}
