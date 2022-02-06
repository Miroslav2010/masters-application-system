package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;

import java.util.List;

public interface ProcessService {

    List<Process> findAll();

    Process findProcessById(String id);

    Master getProcessMaster(String id);

    Process getProcessByMasterId(String id);

    ProcessState getProcessState(String id);

    Process setState(Process process, int processStateId);

    Process nextState(String id);

    Process goToStep(String id, ProcessState state);

    Process save(Master master);

}
