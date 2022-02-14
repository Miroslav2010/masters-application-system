package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.MasterPreviewDTO;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.MasterPreviewView;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;
import mk.ukim.finki.masterapplicationsystem.service.PersonService;
import mk.ukim.finki.masterapplicationsystem.service.ProcessService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState.*;

@Service
public class ProcessStateHelperService {
    private final ProcessService processService;
    private final PersonService personService;

    private final Map<String, Set<ProcessState>> typeStepMap = new HashMap<String, Set<ProcessState>>() {{
        put("MasterTopic", EnumSet.of(DOCUMENT_APPLICATION));
        put("Validation",
                EnumSet.of(INITIAL_MENTOR_REVIEW, STUDENT_SERVICE_REVIEW, INITIAL_NNK_REVIEW, INITIAL_SECRETARY_REVIEW,
                        DRAFT_MENTOR_REVIEW, DRAFT_SECRETARY_REVIEW, DRAFT_NNK_REVIEW, SECOND_DRAFT_SECRETARY_REVIEW, DRAFT_COMMITTEE_REVIEW,
                        REPORT_REVIEW, REPORT_SECRETARY_REVIEW, REPORT_STUDENT_SERVICE));
        put("Attachment", EnumSet.of(STUDENT_DRAFT, STUDENT_CHANGES_DRAFT, MENTOR_REPORT));
        put("Finished", EnumSet.of(APPLICATION_FINISHED, FINISHED));
    }};

    public ProcessStateHelperService(ProcessService processService, PersonService personService) {
        this.processService = processService;
        this.personService = personService;
    }

    public List<Person> getResponsiblePersonsForStep(String processId) {
        ProcessState processState = processService.getProcessState(processId);
        List<Person> responsiblePersons = new ArrayList<>();
        Master master = processService.getProcessMaster(processId);
        if (EnumSet.of(INITIAL_MENTOR_REVIEW, DRAFT_MENTOR_REVIEW, MENTOR_REPORT).contains(processState))
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

    public String getAssignedRolesForStep(String processId) {
        ProcessState processState = processService.getProcessState(processId);
        if (EnumSet.of(INITIAL_MENTOR_REVIEW, DRAFT_MENTOR_REVIEW, MENTOR_REPORT).contains(processState))
            return Role.PROFESSOR.toString();
        else if (EnumSet.of(APPLICATION, DOCUMENT_APPLICATION, STUDENT_DRAFT, STUDENT_CHANGES_DRAFT).contains(processState))
            return Role.STUDENT.toString();
        else if (EnumSet.of(DRAFT_COMMITTEE_REVIEW, REPORT_REVIEW).contains(processState))
            return Role.COMMITTEE.toString();
        else if (EnumSet.of(STUDENT_SERVICE_REVIEW, REPORT_STUDENT_SERVICE).contains(processState))
            return Role.STUDENT_SERVICE.toString();
        else if (EnumSet.of(INITIAL_SECRETARY_REVIEW, DRAFT_SECRETARY_REVIEW, SECOND_DRAFT_SECRETARY_REVIEW, REPORT_SECRETARY_REVIEW).contains(processState))
            return Role.SECRETARY.toString();
        else return Role.NNK.toString();
    }

    public List<ProcessState> getStepsFromType(String type) {
        return new ArrayList<>(typeStepMap.getOrDefault(type, Collections.emptySet()));
    }

    public String getTypeOfStep(ProcessState processState) {
        if (typeStepMap.get("MasterTopic").contains(processState))
            return "MasterTopic";
        else if (typeStepMap.get("Validation").contains(processState))
            return "Validation";
        else if (typeStepMap.get("Attachment").contains(processState))
            return "Attachment";
        else
            return "Finished";
    }

    public String formatDateTime(Date dateTime) {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(dateTime);
    }

    public MasterPreviewDTO convertToMasterPreview(MasterPreviewView masterView) {
        String label = valueOf(masterView.getSTEP()).label;
        String lastModifiedDateTime = formatDateTime(masterView.getLASTMODIFIED());

        return new MasterPreviewDTO(masterView.getID(), masterView.getSTUDENT(), masterView.getMENTOR(), label, lastModifiedDateTime);
    }
}
