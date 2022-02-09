package mk.ukim.finki.masterapplicationsystem.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String fullName;
    private Collection<Role> roles;
}
