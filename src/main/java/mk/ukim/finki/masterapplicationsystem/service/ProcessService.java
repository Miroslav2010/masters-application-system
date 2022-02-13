package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.MasterPreviewDTO;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.MasterPreviewView;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProcessService {

    List<Process> findAll();

    List<Process> findAllByPersonAssigned(String personId);

    Page<MasterPreviewView> findAllView(String personId, String filter, Pageable pageable);

    Process findProcessById(String id);

    Master getProcessMaster(String id);

    Process getProcessByMasterId(String id);

    List<Process> findAllThatAreInStates(List<ProcessState> processStates);

    ProcessState getProcessState(String id);

    Process setState(Process process, int processStateId);

    Process nextState(String id);

    Process goToStep(String id, ProcessState state);

    Process save(Master master);

}
