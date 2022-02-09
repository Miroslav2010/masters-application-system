package mk.ukim.finki.masterapplicationsystem.domain.dto;

import lombok.Data;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@Data
public class LoginResponseDto {
    String fullName;
    Collection<Role> roles;

    public LoginResponseDto(String name, Collection<Role> authorities) {
        this.fullName = name;
        this.roles=authorities;
    }
}
