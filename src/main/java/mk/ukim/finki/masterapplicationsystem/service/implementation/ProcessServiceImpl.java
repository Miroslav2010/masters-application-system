package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.repository.ProcessRepository;
import mk.ukim.finki.masterapplicationsystem.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {
    private final Logger logger = LoggerFactory.getLogger(ProcessServiceImpl.class);
    private final ProcessRepository processRepository;

    public ProcessServiceImpl(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    @Override
    public List<Process> findAll() {
        return processRepository.findAll();
    }

    @Override
    public List<Process> findAllByPersonAssigned(String personId) {
        return processRepository.findAllByMaster_StudentIdOrMaster_MentorIdOrMaster_CommitteeFirstIdOrMaster_CommitteeSecondId(personId, personId, personId, personId);
    }

    @Override
    public Process findProcessById(String id) {
        return processRepository.findById(id).orElseThrow(() -> new RuntimeException("Process with id " + id + " was not found"));
    }

    @Override
    public Master getProcessMaster(String id) {
        Process process = findProcessById(id);
        return process.getMaster();
    }

    @Override
    public Process getProcessByMasterId(String id) {
        return processRepository.findByMasterId(id).orElseThrow(() -> new RuntimeException("Master with id " + id + " was not found"));
    }

    @Override
    public ProcessState getProcessState(String id) {
        Process process = findProcessById(id);
        return process.getProcessState();
    }

    @Override
    public Process setState(Process process, int processStateId) {
        process.setProcessState(ProcessState.values()[processStateId]);
        process = processRepository.save(process);
        logger.info("Moved process to next step: {}, previous was {}", ProcessState.values()[processStateId], ProcessState.values()[processStateId]);
        return process;
    }

    @Override
    public Process nextState(String id) {
        Process process = findProcessById(id);
        ProcessState processState = process.getProcessState();
        if (processState.equals(ProcessState.FINISHED))
            throw new RuntimeException("Process with id " + id + " can not go on next step because it is on the last step");
        int processStateId = process.getProcessState().ordinal() + 1;
        return setState(process, processStateId);
    }

    @Override
    public Process goToStep(String id, ProcessState state) {
        Process process = findProcessById(id);
        process.setProcessState(state);
        process = processRepository.save(process);
        logger.info("Process with id: {} was set to state {}", id, state);
        return process;
    }

    @Override
    public Process save(Master master) {
        Process process = new Process();
        process.setMaster(master);
        return processRepository.save(process);
    }
}
