package mk.ukim.finki.masterapplicationsystem.domain;

import lombok.Data;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class Person implements UserDetails {

    @Id
    private final String id = UUID.randomUUID().toString();
    private String fullName;

    private String password;

    @ElementCollection(targetClass= Role.class)
    @Enumerated(EnumType.STRING)
    private final List<Role> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return fullName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
