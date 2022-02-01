package mk.ukim.finki.masterapplicationsystem.repository.implementation;

import mk.ukim.finki.masterapplicationsystem.configuration.FileSystemConfiguration;
import mk.ukim.finki.masterapplicationsystem.repository.FileRepository;
import org.aspectj.util.FileUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;


@Repository
public class FileRepositoryImplementation implements FileRepository {
    private FileSystemConfiguration fileSystemConfiguration;
    private static String dataFolderName = "";
    private static String applicationDocumentsFolderName = "/documents-application";
    private static String draftsFolderName = "/drafts";
    private static String reportsFolderName = "/reports";

    public FileRepositoryImplementation(FileSystemConfiguration fileSystemConfiguration) {
        this.fileSystemConfiguration = fileSystemConfiguration;
    }

    @PostConstruct
    public void init() {
        File folder = new File(fileSystemConfiguration.getDataDirectory() + dataFolderName);
        folder.mkdirs();
    }

    public String save(String userId, MultipartFile file, String subFolder) throws IOException {
        this.checkDirectoriesForUser(userId);
        String saveLocation =  "/" + userId + subFolder + "/" + new Date().getTime() + file.getName();
        File localFile = new File(makeFullPath(saveLocation));
        file.transferTo(localFile);
        return saveLocation;
    }

    @Override
    public void delete(String location) {
        File localFile = new File(location);
        if (localFile.exists()) {
            localFile.delete();
        }
    }

    @Override
    public Resource findByLocation(String location) throws FileNotFoundException {
        File file = new File(makeFullPath(location));
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        return new FileSystemResource(file);
    }

    private String makeFullPath(String relativePath){
        return fileSystemConfiguration.getDataDirectory() + relativePath;
    }


    private void checkDirectoriesForUser(String userId) {
        boolean isDirectoryPresent = this.isDirectoryStructureCreated(userId);
        if (!isDirectoryPresent) {
            this.createDirectoryStructureForUser(userId);
        }
    }

    private boolean isDirectoryStructureCreated(String userId) {
        String userFolder = fileSystemConfiguration.getDataDirectory() + dataFolderName + "/" + userId.toString();
        File folder = new File(userFolder);
        return folder.exists();
    }

    private void createDirectoryStructureForUser(String  userId) {
        String userFolder = fileSystemConfiguration.getDataDirectory() + dataFolderName + "/" + userId;
        File documentsApplicationFolder = new File(userFolder + applicationDocumentsFolderName);
        File draftsFolder = new File(userFolder + draftsFolderName);
        File reportsFolder = new File(userFolder + reportsFolderName);
        documentsApplicationFolder.mkdirs();
        draftsFolder.mkdirs();
        reportsFolder.mkdirs();
    }
}
