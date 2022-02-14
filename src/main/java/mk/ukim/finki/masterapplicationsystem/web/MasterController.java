package mk.ukim.finki.masterapplicationsystem.web;

import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.domain.dto.request.ArchiveNumberDTO;
import mk.ukim.finki.masterapplicationsystem.domain.dto.request.MasterCreateDTO;
import mk.ukim.finki.masterapplicationsystem.domain.dto.request.ValidationStepDTO;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.*;
import mk.ukim.finki.masterapplicationsystem.service.MajorService;
import mk.ukim.finki.masterapplicationsystem.service.MasterManagementService;
import mk.ukim.finki.masterapplicationsystem.service.MasterService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:3000")
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

    @GetMapping("/major")
    public ResponseEntity<List<Major>> getAllMajors(){
        return ResponseEntity.ok(majorService.findAllMajors());
    }

    @PostMapping("/major")
    public Major createMajor() {
        return majorService.save("Major");
    }

    @PostMapping
    public Process createMaster(@RequestBody MasterCreateDTO masterCreateDTO) {
        return masterManagementService.createMaster(masterCreateDTO.getStudentId(), masterCreateDTO.getFirstCommitteeId(),
                masterCreateDTO.getSecondCommitteeId(), masterCreateDTO.getMajorId());
    }

    @PostMapping(value = "/master-topic/{processId}")
    public MasterTopic createMasterTopic(@PathVariable String processId, @RequestParam String topic, @RequestParam String description,
                                         @RequestParam MultipartFile biography, @RequestParam MultipartFile mentorApproval, @RequestParam MultipartFile application,
                                         @RequestParam MultipartFile supplement) throws IOException {
        return masterManagementService.createMasterTopic(processId, topic, description, biography, mentorApproval,
                application, supplement);
    }

    @PostMapping("/validation-step")
    public void validate(@RequestBody ValidationStepDTO validationStepDTO) {
        masterManagementService.validateStep(validationStepDTO.getProcessId(), validationStepDTO.getValidationStatus());
    }

    @PostMapping("/archive-number")
    public Master setArchiveNumber(@RequestBody ArchiveNumberDTO archiveNumberDTO) {
        return masterManagementService.setArchiveNumber(archiveNumberDTO.getProcessId(), archiveNumberDTO.getArchiveNumber());
    }

    @PostMapping("/{processId}/draft")
    public Attachment uploadDraft(@PathVariable String processId, @RequestParam MultipartFile draft) throws IOException {
//        MockMultipartFile draftt = new MockMultipartFile("draft", "draft.pdf", "application/pdf", "some draft".getBytes());
        Attachment attachment = masterManagementService.uploadDraft(processId, draft);
        confirmUpload(processId);
        return attachment;
    }

    @PostMapping("/confirm-draft")
    public Process confirmUpload(@RequestParam String processId) {
        return masterManagementService.confirmUpload(processId);
    }

    @PostMapping("/{processId}/cancel-revision")
    public Process cancelTheRevisionLoop(@PathVariable String processId) {
        return masterManagementService.cancelRevisionLoop(processId);
    }

    //------------------------------------------------------------------------------------------------------------------

    @GetMapping("/{processId}/validation-details")
    public ResponseEntity<ValidationResponseDTO> getValidationDetails(@PathVariable String processId) {
        return ResponseEntity.ok(masterManagementService.getValidationDetails(processId));
    }

    @GetMapping("/all")
    public MasterPreviewListDTO getAllMasters(Pageable pageable, @RequestParam( defaultValue = "") String filter) {
        return masterManagementService.getAllMasters(pageable, filter);
    }

    @GetMapping("/{processId}/all-steps")
    public StepPreviewDTO getSteps(@PathVariable String processId) {
        return masterManagementService.getAllFinishedSteps(processId);
    }

    @GetMapping("/{processId}/masterInfo")
    public MasterBasicInfoDTO getStudent(@PathVariable String processId) {
        return masterManagementService.getMasterBasicInfo(processId);
    }

    @GetMapping("/{processId}/current-step")
    public CurrentStepDTO getCurrentStepInfo (@PathVariable String processId) {
        return masterManagementService.getCurrentStepInfo(processId);
    }

}
