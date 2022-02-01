package mk.ukim.finki.masterapplicationsystem.repository;

import mk.ukim.finki.masterapplicationsystem.domain.Step;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StepRepository extends JpaRepository<Step, String> {

    List<Step> findAllByProcessId(String processId);

    Optional<Step> findByProcessIdAndName(String processId, String stepName);

    Optional<Step> findFirstByProcessIdAndNameOrderByOrderNumberDesc(String processId, String stepName);

    List<Step> findAllByProcessIdAndName(String processId, String stepName);
}
