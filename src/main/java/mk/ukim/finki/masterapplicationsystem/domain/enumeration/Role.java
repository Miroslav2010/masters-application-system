package mk.ukim.finki.masterapplicationsystem.domain.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT,
    PROFESSOR,
    COMMITTEE,
    STUDENT_SERVICE,
    SECRETARY,
    NNK,
    HEAD_OF_NNK;

    @Override
    public String getAuthority() {
        return name();
    }
}
