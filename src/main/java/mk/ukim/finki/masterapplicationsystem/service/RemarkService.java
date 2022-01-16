package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Remark;
import mk.ukim.finki.masterapplicationsystem.domain.dto.RemarkDto;

import java.util.List;

public interface RemarkService {

    Remark findById(String id);

    List<Remark> findAllByStepId(String stepId);

    List<Remark> findAllByStepName(String processId, String stepName);

    List<Remark> findAllByPersonId(String personId);

    Remark saveRemark(String processId, RemarkDto remarkDto);

}
