package mk.ukim.finki.masterapplicationsystem.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "filesystem")
public class FileSystemConfiguration {
    private boolean removeData = false;
    private String dataDirectory = ".";

    public boolean isRemoveData() {
        return removeData;
    }

    public void setRemoveData(boolean removeData) {
        this.removeData = removeData;
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }
}
