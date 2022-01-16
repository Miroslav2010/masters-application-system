package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Remark;
import mk.ukim.finki.masterapplicationsystem.domain.Step;
import mk.ukim.finki.masterapplicationsystem.repository.RemarkRepository;
import mk.ukim.finki.masterapplicationsystem.service.RemarkService;
import mk.ukim.finki.masterapplicationsystem.service.StepService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RemarkServiceImpl implements RemarkService {
    private final RemarkRepository remarkRepository;
    private final StepService stepService;

    public RemarkServiceImpl(RemarkRepository remarkRepository, StepService stepService) {
        this.remarkRepository = remarkRepository;
        this.stepService = stepService;
    }

    @Override
    public Remark findById(String id) {
        return remarkRepository.findById(id).orElseThrow(() -> new RuntimeException("Remark with id " + id + " was not found"));
    }

    @Override
    public List<Remark> findAllByStepId(String stepId) {
        return remarkRepository.findAllByStepId(stepId);
    }

    @Override
    public List<Remark> findAllByStepName(String processId, String stepName) {
        Step step = stepService.getStepFromProcess(processId, stepName);
        return remarkRepository.findAllByStepId(step.getId());
    }

    @Override
    public List<Remark> findAllByPersonId(String personId) {
        return remarkRepository.findAllByPersonId(personId);
    }
}
