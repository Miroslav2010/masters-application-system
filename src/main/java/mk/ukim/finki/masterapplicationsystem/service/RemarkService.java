package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.Remark;
import mk.ukim.finki.masterapplicationsystem.domain.Step;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.RemarkResponseDTO;

import java.util.List;

public interface RemarkService {

    Remark findById(String id);

    List<Remark> findAllByStepId(String stepId);

    List<Remark> findAllByStepName(String processId, String stepName);

    List<Remark> findAllByPersonId(String personId);

    List<RemarkResponseDTO> findAllRemarksForCurrentStep(String processId);

    Remark deleteById(String remarkId);

    Remark saveNewRemark(Person person, Step step);

    Remark saveRemark(String processId, Person person, String remarkMessage);

    Remark editRemark(String processId, Person person, String remarkId, String remarkMessage);

}
