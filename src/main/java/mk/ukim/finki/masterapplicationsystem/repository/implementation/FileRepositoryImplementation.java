package mk.ukim.finki.masterapplicationsystem.repository.implementation;

import mk.ukim.finki.masterapplicationsystem.Configurations.FileSystemConfiguration;
import mk.ukim.finki.masterapplicationsystem.repository.FileRepository;
import org.aspectj.util.FileUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.Date;
import java.util.UUID;


@Repository
public class FileRepositoryImplementation implements FileRepository {
    private FileSystemConfiguration fileSystemConfiguration;
    private static String dataFolderName="/MasterDocuments";
    private static String applicationDocumentsFolderName = "/documents-application";
    private static String draftsFolderName = "/drafts";
    private static String reportsFolderName = "/reports";

    public FileRepositoryImplementation(FileSystemConfiguration fileSystemConfiguration) {
        this.fileSystemConfiguration = fileSystemConfiguration;
    }
    @PostConstruct
    public void init() throws FileSystemException {
        File folder = new File(fileSystemConfiguration.getDataDirectory() + dataFolderName);
        if(fileSystemConfiguration.isRemoveData()){
            FileUtil.deleteContents(folder);
        }
        boolean result = folder.mkdirs();
        if(!result)
        {
            throw new FileSystemException("Could not create folders!");
        }
    }

    public String save(UUID userId, MultipartFile file,String subFolder) throws IOException {
        this.checkDirectoriesForUser(userId);
        String saveLocation = fileSystemConfiguration.getDataDirectory() + "/" + userId.toString()+subFolder+"/"+new Date().getTime()+file.getName();
        File localFile = new File(saveLocation);
        file.transferTo(localFile);
        return saveLocation;
    }

    @Override
    public void delete(String location) {
        File localFile = new File(location);
        if (localFile.exists()){
            localFile.delete();
        }
    }

    @Override
    public Resource findByLocation(String location) throws FileNotFoundException {
        File file = new File(location);
        if(!file.exists() || file.isDirectory()){
            throw new FileNotFoundException();
        }
        return new FileSystemResource(file);
    }


    private void checkDirectoriesForUser(UUID userId){
        boolean isDirectoryPresent = this.isDirectoryStructureCreated(userId);
        if (!isDirectoryPresent){
            this.createDirectoryStructureForUser(userId);
        }
    }

    private boolean isDirectoryStructureCreated(UUID userId){
        String userFolder=fileSystemConfiguration.getDataDirectory()+dataFolderName+"/"+userId.toString();
        File folder = new File(userFolder);
        return folder.exists();
    }
    private void createDirectoryStructureForUser(UUID userId){
        String userFolder=fileSystemConfiguration.getDataDirectory()+dataFolderName+"/"+userId.toString();
        File documentsApplicationFolder= new File(userFolder+applicationDocumentsFolderName);
        File draftsFolder = new File(userFolder+draftsFolderName);
        File reportsFolder = new File(userFolder+reportsFolderName);
        documentsApplicationFolder.mkdirs();
        draftsFolder.mkdirs();
        reportsFolder.mkdirs();
    }
}
