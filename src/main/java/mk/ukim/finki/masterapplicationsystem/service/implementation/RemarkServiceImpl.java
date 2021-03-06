package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.Remark;
import mk.ukim.finki.masterapplicationsystem.domain.Step;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.RemarkResponseDTO;
import mk.ukim.finki.masterapplicationsystem.domain.mapper.RemarkMapper;
import mk.ukim.finki.masterapplicationsystem.repository.RemarkRepository;
import mk.ukim.finki.masterapplicationsystem.service.PermissionService;
import mk.ukim.finki.masterapplicationsystem.service.PersonService;
import mk.ukim.finki.masterapplicationsystem.service.RemarkService;
import mk.ukim.finki.masterapplicationsystem.service.StepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RemarkServiceImpl implements RemarkService {
    private final RemarkMapper remarkMapper = RemarkMapper.INSTANCE;
    private final RemarkRepository remarkRepository;
    private final StepService stepService;
    private final PersonService personService;
    private final PermissionService permissionService;

    private final Logger logger = LoggerFactory.getLogger(RemarkServiceImpl.class);

    public RemarkServiceImpl(RemarkRepository remarkRepository, StepService stepService, PersonService personService, PermissionService permissionService) {
        this.remarkRepository = remarkRepository;
        this.stepService = stepService;
        this.personService = personService;
        this.permissionService = permissionService;
    }

    @Override
    public Remark findById(String id) {
        return remarkRepository.findById(id).orElseThrow(() -> new RuntimeException("Remark with id " + id + " was not found"));
    }

    public Remark saveRemark(String processId, String remarkMessage) {
        Person person = personService.getLoggedInUser();
        permissionService.canPersonWriteRemark(processId, person.getId());
        Step currentStep = stepService.getActiveStep(processId);
        Remark remark = new Remark(person, currentStep);
        remark.setRemark(remarkMessage);
        remark.setDateTime(OffsetDateTime.now());
        return remarkRepository.save(remark);
    }

    @Override
    public Remark editRemark(String processId, String remarkId, String remarkMessage) {
        //can make remark
        Person person = personService.getLoggedInUser();
        permissionService.canPersonWriteRemark(processId, person.getId());
        Remark remark = findById(remarkId);
        remark.setRemark(remarkMessage);
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

    @Override
    public List<RemarkResponseDTO> findAllRemarksForCurrentStep(String processId) {
        Person loggedInPerson = personService.getLoggedInUser();
        //TODO: Find by person id and step id
        Step step = stepService.getActiveStep(processId);
        List<Remark> remarks = remarkRepository.findAllByStepId(step.getId());
        List<RemarkResponseDTO> remarkResponseDTOS = new ArrayList<>();
        remarks.forEach(s -> remarkResponseDTOS.add(new RemarkResponseDTO(s.getId(), s.getPerson().getFullName(), s.getRemark())));
        return remarkResponseDTOS;
    }

    @Override
    public Remark deleteById(String remarkId) {
        Person loggedInPerson = personService.getLoggedInUser();
        Remark remark = remarkRepository.getById(remarkId);
        if (!remark.getPerson().getId().equals(loggedInPerson.getId()))
            throw new RuntimeException("User tried to delete a remark that is not created by him.");
        remarkRepository.deleteById(remarkId);
        return remark;
    }

    public Remark saveNewRemark(Person person, Step step) {
        Person loggedInPerson = personService.getLoggedInUser();
        Remark remark = new Remark(loggedInPerson, step);
        return remarkRepository.save(remark);
    }
}
