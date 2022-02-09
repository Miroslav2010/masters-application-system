package mk.ukim.finki.masterapplicationsystem.web;

import mk.ukim.finki.masterapplicationsystem.domain.dto.request.RemarkRequestDTO;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.RemarkResponseDTO;
import mk.ukim.finki.masterapplicationsystem.service.PersonService;
import mk.ukim.finki.masterapplicationsystem.service.RemarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/api/remark")
public class RemarkController {
    private final RemarkService remarkService;
    private final PersonService personService;

    public RemarkController(RemarkService remarkService, PersonService personService) {
        this.remarkService = remarkService;
        this.personService = personService;
    }

//    @GetMapping("/{stepId}")
//    public ResponseEntity<List<Remark>> getRemarks(@PathVariable String stepId) {
//        return ResponseEntity.ok(remarkService.findAllByStepId(stepId));
//    }

    @PostMapping("/{processId}")
    public ResponseEntity createRemark(@PathVariable String processId, @RequestBody RemarkRequestDTO remark) {
        //TODO: get current person
        //TODO: Rollback
        //permission fo step
        remarkService.saveRemark(processId, remark.getRemark());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{processId}/edit")
    public ResponseEntity editRemark(@PathVariable String processId, @RequestBody RemarkRequestDTO remark) {
        //TODO: get current person
        //TODO: Rollback
//        RemarkDto remarkDto = new RemarkDto(remark, person);
        remarkService.saveRemark(processId, remark.getRemark());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{remarkId}")
    public void deleteRemark(@PathVariable String remarkId) {
        remarkService.deleteById(remarkId);
    }

    @GetMapping("/{processId}")
    public List<RemarkResponseDTO> getAllRemarks(@PathVariable String processId) {
        return remarkService.findAllRemarksForCurrentStep(processId);
    }

    @GetMapping("/step/{stepId}")
    public List<RemarkResponseDTO> getAllRemarksForStep(@PathVariable String stepId) {
        List<RemarkResponseDTO> remarks = new ArrayList<>();
        remarkService.findAllByStepId(stepId).forEach(s -> remarks.add(new RemarkResponseDTO(s.getId(), s.getPerson().getFullName(), s.getRemark())));
        return remarks;
    }

}
