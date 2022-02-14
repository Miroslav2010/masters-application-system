package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.service.PersonService;
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
    private final PersonService personService;

    public ProcessStateHelperService(ProcessService processService, PersonService personService) {
        this.processService = processService;
        this.personService = personService;
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
    public List<Person> getEmailReceivers(String processId){
        ProcessState processState = processService.getProcessState(processId);
        List<Person> responsiblePersons = this.getResponsiblePersonsForStep(processId);
        if (EnumSet.of(DRAFT_NNK_REVIEW,INITIAL_NNK_REVIEW).contains(processState))
        {
            responsiblePersons.addAll(personService.getAllNNKMembers());
        }
        else if (EnumSet.of(SECOND_DRAFT_SECRETARY_REVIEW,DRAFT_SECRETARY_REVIEW,REPORT_SECRETARY_REVIEW,INITIAL_SECRETARY_REVIEW).contains(processState)){
            responsiblePersons.addAll(personService.getAllSecretaries());
        } else if (EnumSet.of(STUDENT_SERVICE_REVIEW,REPORT_STUDENT_SERVICE).contains(processState)) {
            responsiblePersons.addAll(personService.getStudentServiceMembers());
        }
        return responsiblePersons;
    }

    public String getTypeOfStep(ProcessState processState) {
        if (processState == DOCUMENT_APPLICATION)
            return "MasterTopic";
        else if (EnumSet.of(INITIAL_MENTOR_REVIEW, STUDENT_SERVICE_REVIEW, INITIAL_NNK_REVIEW, INITIAL_SECRETARY_REVIEW,
                DRAFT_MENTOR_REVIEW, DRAFT_SECRETARY_REVIEW, DRAFT_NNK_REVIEW, SECOND_DRAFT_SECRETARY_REVIEW, DRAFT_COMMITTEE_REVIEW,
                REPORT_REVIEW, REPORT_SECRETARY_REVIEW, REPORT_STUDENT_SERVICE).contains(processState))
            return "Validation";
        else if (EnumSet.of(STUDENT_DRAFT, STUDENT_CHANGES_DRAFT, MENTOR_REPORT).contains(processState))
            return "Attachment";
        else
            return "Finished";
    }
}
