package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.Remark;
import mk.ukim.finki.masterapplicationsystem.domain.Step;
import mk.ukim.finki.masterapplicationsystem.domain.dto.RemarkDto;
import mk.ukim.finki.masterapplicationsystem.domain.mapper.RemarkMapper;
import mk.ukim.finki.masterapplicationsystem.repository.RemarkRepository;
import mk.ukim.finki.masterapplicationsystem.service.RemarkService;
import mk.ukim.finki.masterapplicationsystem.service.StepService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class RemarkServiceImpl implements RemarkService {
    private final RemarkMapper remarkMapper = RemarkMapper.INSTANCE;
    private final RemarkRepository remarkRepository;
    private final StepService stepService;

    private final Logger logger = LoggerFactory.getLogger(RemarkServiceImpl.class);

    public RemarkServiceImpl(RemarkRepository remarkRepository, StepService stepService) {
        this.remarkRepository = remarkRepository;
        this.stepService = stepService;
    }

    @Override
    public Remark findById(String id) {
        return remarkRepository.findById(id).orElseThrow(() -> new RuntimeException("Remark with id " + id + " was not found"));
    }
    public Remark saveRemark(String processId,RemarkDto remarkDto){
        Step currentStep = stepService.getStepFromProcess(processId,"");
        Remark remark = remarkMapper.remarkDtoToRemark(remarkDto);
        remark.setStep(currentStep);
        remark.setDateTime(OffsetDateTime.now());
        return remarkRepository.save(remark);
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

    public Remark saveNewRemark(Person person, Step step) {
        //get active user
        Remark remark = new Remark(person, step);
        return remarkRepository.save(remark);
    }
}
