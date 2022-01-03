package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Remark;

import java.util.List;

public interface RemarkService {

    Remark findById(String id);

    List<Remark> findAllByStepId(String stepId);

    List<Remark> findAllByStepName(String processId, String stepName);

    List<Remark> findAllByPersonId(String personId);

}
