package mk.ukim.finki.masterapplicationsystem.repository;

import mk.ukim.finki.masterapplicationsystem.domain.Master;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterRepository extends JpaRepository<Master, String> {
}
