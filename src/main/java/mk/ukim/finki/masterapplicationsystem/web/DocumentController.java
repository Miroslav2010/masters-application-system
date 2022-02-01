package mk.ukim.finki.masterapplicationsystem.web;

import mk.ukim.finki.masterapplicationsystem.service.DocumentService;
import mk.ukim.finki.masterapplicationsystem.service.MasterManagementService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/document")
public class DocumentController {
    private MasterManagementService masterManagementService;

    public DocumentController(MasterManagementService masterManagementService) {
        this.masterManagementService = masterManagementService;
    }

//    @GetMapping("/{processId}")
//    public List<String> getDrafts(@PathVariable String processId) {
//        return masterManagementService.getDocumentLocations(processId);
//    }

}
