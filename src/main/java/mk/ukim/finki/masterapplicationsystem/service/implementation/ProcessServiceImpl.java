package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.repository.ProcessRepository;
import mk.ukim.finki.masterapplicationsystem.service.ProcessService;
import org.springframework.stereotype.Service;

@Service
public class ProcessServiceImpl implements ProcessService {
    private final ProcessRepository processRepository;

    public ProcessServiceImpl(ProcessRepository processRepository) {
        this.processRepository = processRepository;
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
    public Process nextState(String id) {
        Process process = findProcessById(id);
        ProcessState processState = process.getProcessState();
        if (processState.equals(ProcessState.FINISHED))
            throw new RuntimeException("Process with id " + id + " can not go on next step because it is on the last step");
        int processStateId = process.getProcessState().ordinal() + 1;
        process.setProcessState(ProcessState.values()[processStateId]);
        return processRepository.save(process);
    }

    @Override
    public Process goToStep(String id, ProcessState state) {
        Process process = findProcessById(id);
        process.setProcessState(state);
        return processRepository.save(process);
    }

    @Override
    public Process save(Master master) {
        Process process = new Process();
        process.setMaster(master);
        return processRepository.save(process);
    }
}
