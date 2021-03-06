package mk.ukim.finki.masterapplicationsystem.web;

import mk.ukim.finki.masterapplicationsystem.domain.Document;
import mk.ukim.finki.masterapplicationsystem.service.DocumentService;
import mk.ukim.finki.masterapplicationsystem.service.MasterManagementService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/api/document")
public class DocumentController {
    private MasterManagementService masterManagementService;
    private DocumentService documentService;

    public DocumentController(MasterManagementService masterManagementService, DocumentService documentService) {
        this.masterManagementService = masterManagementService;
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<Resource> downloadFile(@RequestParam String documentId, @RequestParam String fileLocation) throws IOException {
        Resource downloadFile = documentService.findFileByLocation(fileLocation);
        Document document = documentService.findDocumentById(documentId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")
                .body(downloadFile);
    }

//    @GetMapping("/{processId}")
//    public List<String> getDrafts(@PathVariable String processId) {
//        return masterManagementService.getDocumentLocations(processId);
//    }

}
