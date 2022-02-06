package mk.ukim.finki.masterapplicationsystem.web;

import mk.ukim.finki.masterapplicationsystem.domain.Step;
import mk.ukim.finki.masterapplicationsystem.domain.dto.response.StepValidationDTO;
import mk.ukim.finki.masterapplicationsystem.service.StepService;
import mk.ukim.finki.masterapplicationsystem.service.StepValidationService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin()
public class HistoryController {
    private final StepService stepService;
    private final StepValidationService stepValidationService;

    public HistoryController(StepService stepService, StepValidationService stepValidationService) {
        this.stepService = stepService;
        this.stepValidationService = stepValidationService;
    }

    @GetMapping("/{stepId}")
    public Step getStepHistory(@PathVariable String stepId) {
        return stepService.findHistoryStepById(stepId);
    }

    @GetMapping("/validations/{stepId}")
    public List<StepValidationDTO> getStepValidations(@PathVariable String stepId) {
        List<StepValidationDTO> stepValidations = new ArrayList<>();
        stepValidationService.findAllByValidationId(stepId).forEach(s ->
                stepValidations.add(new StepValidationDTO(s.getId(), s.getValidationStatus().toString(), s.getPerson().getFullName())));
        return stepValidations;
    }
}
