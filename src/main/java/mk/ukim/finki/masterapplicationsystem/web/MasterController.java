package mk.ukim.finki.masterapplicationsystem.web;

import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.domain.dto.request.*;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.ValidationResponseDTO;
import mk.ukim.finki.masterapplicationsystem.service.MajorService;
import mk.ukim.finki.masterapplicationsystem.service.MasterManagementService;
import mk.ukim.finki.masterapplicationsystem.service.MasterService;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/master")
public class MasterController {
    private final MasterService masterService;
    private final MajorService majorService;
    private final MasterManagementService masterManagementService;

    public MasterController(MasterService masterService, MajorService majorService, MasterManagementService masterManagementService) {
        this.masterService = masterService;
        this.majorService = majorService;
        this.masterManagementService = masterManagementService;
    }

    @GetMapping("{id}")
    public Master findMasterById(@PathVariable String id) {
        return masterService.findMasterById(id);
    }

    @PostMapping("/major")
    public Major createMajor() {
        return majorService.save("Major");
    }

    @PostMapping
    public Process createMaster(@RequestBody MasterCreateDTO masterCreateDTO) {
        return masterManagementService.createMaster(masterCreateDTO.getMentorId(), masterCreateDTO.getFirstCommitteeId(),
                masterCreateDTO.getSecondCommitteeId(), masterCreateDTO.getMajorId());
    }

    @PostMapping("/master-topic")
    public MasterTopic createMasterTopic(@RequestBody MasterTopicStepDTO masterTopicStepDTO) throws IOException {
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        return masterManagementService.createMasterTopic(masterTopicStepDTO.getProcessId(), masterTopicStepDTO.getTopic(),
                masterTopicStepDTO.getDescription(), firstFile, firstFile, firstFile, firstFile);
    }

    @PostMapping("/validation-step")
    public void validate(@RequestBody ValidationStepDTO validationStepDTO) {
        masterManagementService.validateStep(validationStepDTO.getProcessId(), validationStepDTO.getValidationStatus());
    }

    @PostMapping("/archive-number")
    public Master setArchiveNumber(@RequestBody ArchiveNumberDTO archiveNumberDTO) {
        return masterManagementService.setArchiveNumber(archiveNumberDTO.getProcessId(), archiveNumberDTO.getArchiveNumber());
    }

    @PostMapping("/draft")
    public Attachment uploadDraft(@RequestBody DraftDTO draftDTO) throws IOException {
        MockMultipartFile draft = new MockMultipartFile("draft", "draft.pdf", "application/pdf", "some draft".getBytes());
        return masterManagementService.uploadDraft(draftDTO.getProcessId(), draft);
    }

    @PostMapping("/confirm-draft")
    public Process confirmUpload(@RequestParam String processId) {
        return masterManagementService.confirmUpload(processId);
    }

    @PostMapping("/cancel-revision")
    public Process cancelTheRevisionLoop(@RequestParam String processId) {
        return masterManagementService.cancelRevisionLoop(processId);
    }

    //------------------------------------------------------------------------------------------------------------------

    @GetMapping("/{processId}/validation-details")
    public ResponseEntity<ValidationResponseDTO> getValidationDetails(@PathVariable String processId) {
        return ResponseEntity.ok(masterManagementService.getValidationDetails(processId));
    }

}
