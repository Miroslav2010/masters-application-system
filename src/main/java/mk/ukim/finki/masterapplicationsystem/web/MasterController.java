package mk.ukim.finki.masterapplicationsystem.web;

import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.*;
import mk.ukim.finki.masterapplicationsystem.domain.dto.request.*;
import mk.ukim.finki.masterapplicationsystem.service.MajorService;
import mk.ukim.finki.masterapplicationsystem.service.MasterManagementService;
import mk.ukim.finki.masterapplicationsystem.service.MasterService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
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
        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
        return masterManagementService.createMasterTopic(masterTopicStepDTO.getProcessId(), masterTopicStepDTO.getTopic(),
                masterTopicStepDTO.getDescription(), multipartFile, multipartFile,
                multipartFile, multipartFile);
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
        return masterManagementService.uploadDraft(draftDTO.getProcessId(), draftDTO.getDraft());
    }

    @PostMapping("/confirm-draft")
    public Process uploadDraft(@RequestParam String processId) {
        return masterManagementService.confirmUpload(processId);
    }

}
