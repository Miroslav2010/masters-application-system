package mk.ukim.finki.masterapplicationsystem.repository;

import mk.ukim.finki.masterapplicationsystem.domain.Remark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RemarkRepository extends JpaRepository<Remark, String> {

    List<Remark> findAllByStepId(String stepId);

    List<Remark> findAllByPersonId(String personId);

}
