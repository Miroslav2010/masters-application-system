package mk.ukim.finki.masterapplicationsystem.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import mk.ukim.finki.masterapplicationsystem.domain.Professor;
import mk.ukim.finki.masterapplicationsystem.domain.Student;

@Data
@Getter
@AllArgsConstructor
public class StudentMentorDTO {

    Student student;

    Professor mentor;

}
