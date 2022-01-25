package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.service.ProcessService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState.*;
import static mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState.REPORT_REVIEW;

@Service
public class ProcessStateHelperService {
    private final ProcessService processService;

    public ProcessStateHelperService(ProcessService processService) {
        this.processService = processService;
    }

    public List<Person> getResponsiblePersonsForStep(String processId) {
        ProcessState processState = processService.getProcessState(processId);
        List<Person> responsiblePersons = new ArrayList<>();
        Master master = processService.getProcessMaster(processId);
        if (EnumSet.of(INITIAL_MENTOR_REVIEW, DRAFT_MENTOR_REVIEW).contains(processState))
            responsiblePersons.add(master.getMentor());
        else if (EnumSet.of(APPLICATION, DOCUMENT_APPLICATION, STUDENT_DRAFT, STUDENT_CHANGES_DRAFT).contains(processState))
            responsiblePersons.add(master.getStudent());
        else if (EnumSet.of(DRAFT_COMMITTEE_REVIEW, REPORT_REVIEW).contains(processState)) {
            responsiblePersons.add(master.getCommitteeFirst());
            responsiblePersons.add(master.getCommitteeSecond());
        }
        return responsiblePersons;
    }
}
