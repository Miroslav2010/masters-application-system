package mk.ukim.finki.masterapplicationsystem.repository;

import mk.ukim.finki.masterapplicationsystem.domain.Master;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MasterRepository extends JpaRepository<Master, String> {

    List<Master> findAllByStudentIdAndFinishedDateNull(String Id);

    List<Master> findAllByStudentId(String Id);

}
