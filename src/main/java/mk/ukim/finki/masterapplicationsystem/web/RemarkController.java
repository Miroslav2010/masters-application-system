package mk.ukim.finki.masterapplicationsystem.web;

import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.Remark;
import mk.ukim.finki.masterapplicationsystem.domain.dto.RemarkDto;
import mk.ukim.finki.masterapplicationsystem.service.RemarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/remark")
public class RemarkController {
    private final RemarkService remarkService;

    public RemarkController(RemarkService remarkService) {
        this.remarkService = remarkService;
    }

    @GetMapping("/{stepId}")
    public ResponseEntity<List<Remark>> getRemarks(@PathVariable String stepId) {
        return ResponseEntity.ok(remarkService.findAllByStepId(stepId));
    }

    @PostMapping("/{processId}")
    public ResponseEntity createRemark(@PathVariable String processId, @RequestBody String remark) {
        //TODO: get current person
        Person person = new Person();
        RemarkDto remarkDto = new RemarkDto(remark, person);
        remarkService.saveRemark(processId, remarkDto);
        return ResponseEntity.ok().build();
    }

}
