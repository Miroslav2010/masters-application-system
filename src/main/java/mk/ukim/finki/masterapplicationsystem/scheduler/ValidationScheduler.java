package mk.ukim.finki.masterapplicationsystem.scheduler;

import mk.ukim.finki.masterapplicationsystem.domain.Step;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ValidationStatus;
import mk.ukim.finki.masterapplicationsystem.service.MasterManagementService;
import mk.ukim.finki.masterapplicationsystem.service.ProcessService;
import mk.ukim.finki.masterapplicationsystem.service.StepService;
import mk.ukim.finki.masterapplicationsystem.service.implementation.ProcessStateHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class ValidationScheduler {
    private final ProcessService processService;
    private final MasterManagementService masterManagementService;
    private final ProcessStateHelperService processStateHelperService;
    private final StepService stepService;
    private final Logger logger = LoggerFactory.getLogger(ValidationScheduler.class);

    public ValidationScheduler(ProcessService processService, MasterManagementService masterManagementService, ProcessStateHelperService processStateHelperService, StepService stepService) {
        this.processService = processService;
        this.masterManagementService = masterManagementService;
        this.processStateHelperService = processStateHelperService;
        this.stepService = stepService;
    }

    private boolean checkIfStepIsNotValidatedForThreeDays(Step step) {
        return step.getCreated().plusDays(3).isBefore(OffsetDateTime.now());
    }

    private void validateStep(Step step) {
        masterManagementService.schedulerValidateStep(step.getProcess().getId(), ValidationStatus.APPROVED);
    }

    private List<Step> getStepsToValidateAutomatically() {
        List<ProcessState> validationStates = processStateHelperService.getStepsFromType("Validation");
        List<Step> steps = processService.findAllThatAreInStates(validationStates)
                .stream().map(s -> stepService.getActiveStep(s.getId())).collect(Collectors.toList());
        return steps.stream().filter(this::checkIfStepIsNotValidatedForThreeDays).collect(Collectors.toList());
    }

    //    @Scheduled(fixedDelay = 360_000_000)
    @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
    public void scheduleFixedDelayTask() {
        logger.info("Scheduler for validation steps is started");
        List<Step> steps = getStepsToValidateAutomatically();
        steps.forEach(this::validateStep);
        logger.info("Scheduler finished. Validated {} steps", steps.size());
    }

}
