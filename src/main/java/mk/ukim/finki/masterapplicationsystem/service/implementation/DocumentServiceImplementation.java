package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Document;
import mk.ukim.finki.masterapplicationsystem.repository.DocumentRepository;
import mk.ukim.finki.masterapplicationsystem.repository.FileRepository;
import mk.ukim.finki.masterapplicationsystem.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;

public class DocumentServiceImplementation implements DocumentService {
    private FileRepository fileRepository;
    private DocumentRepository documentRepository;
    private final Logger logger = LoggerFactory.getLogger(DocumentServiceImplementation.class);

    public DocumentServiceImplementation(FileRepository fileRepository, DocumentRepository documentRepository) {
        this.fileRepository = fileRepository;
        this.documentRepository = documentRepository;
    }

    private Document save(String userId, MultipartFile file,String subFolder) throws IOException {
        String fileLocation = fileRepository.save(userId,file,subFolder);
        Document doc = new Document(OffsetDateTime.now(), fileLocation);
        try{
            documentRepository.save(doc);
        }catch (Exception e){
            fileRepository.delete(fileLocation);
            throw e;
        }
        logger.info(String.format("Saved file %s in database for user with id %s",file.getName(),userId));
        return doc;
    }
    public Document saveApplicationDocument(String userId, MultipartFile file) throws IOException {
        return this.save(userId,file,"/documents-application");
    }
    public Document saveDraft(String userId, MultipartFile file) throws IOException {
        return this.save(userId,file,"/drafts");
    }
    public Document saveRepost(String userId, MultipartFile file) throws IOException {
        return this.save(userId,file,"/reports");
    }
    public Document findDocumentById(String id){
        return documentRepository.getById(id);
    }
    public Resource findFileByDocumentId(String  id) throws FileNotFoundException {
        Document doc = documentRepository.getById(id);
        String fileLocation = doc.getLocation();
        return fileRepository.findByLocation(fileLocation);
    }

    public Resource findFileByLocation(String location) throws FileNotFoundException {
        return fileRepository.findByLocation(location);
    }

}
