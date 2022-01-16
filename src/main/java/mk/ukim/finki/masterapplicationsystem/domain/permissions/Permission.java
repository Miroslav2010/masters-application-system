package mk.ukim.finki.masterapplicationsystem.domain.permissions;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Permission {

    @Id
    private final String id = UUID.randomUUID().toString();

    private String processId;

    private String personId;

    @ElementCollection(targetClass = PermissionType.class)
    @Enumerated(EnumType.STRING)
    private List<PermissionType> permissionTypes;

    public Permission(String processId, String personId, List<PermissionType> permissionTypes) {
        this.processId = processId;
        this.personId = personId;
        this.permissionTypes = permissionTypes;
    }

    public Permission() {

    }
}
