package mk.ukim.finki.masterapplicationsystem.domain.dto;

import lombok.Data;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;

import java.util.ArrayList;
import java.util.List;

@Data
public class PersonDto {

    private String fullName;
    private final List<Role> roles = new ArrayList<>();
    private String index;
    private String password;
}
