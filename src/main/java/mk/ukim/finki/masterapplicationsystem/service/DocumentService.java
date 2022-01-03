package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Document;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface DocumentService {
    Document saveApplicationDocument(String userId, MultipartFile file) throws IOException;
    Document saveDraft(String userId, MultipartFile file) throws IOException;
    Document saveRepost(String userId, MultipartFile file) throws IOException;
    Document findDocumentById(String id);
    Resource findFileByDocumentId(String  id) throws FileNotFoundException;
    Resource findFileByLocation(String location) throws FileNotFoundException;
}
