package mk.ukim.finki.masterapplicationsystem.repository;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public interface FileRepository {
    String save(UUID userId, MultipartFile file, String subFolder) throws IOException;
    void delete(String location);
    Resource findByLocation(String location) throws FileNotFoundException;
}
