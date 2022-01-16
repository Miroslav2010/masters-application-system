package mk.ukim.finki.masterapplicationsystem.domain.dto;

import mk.ukim.finki.masterapplicationsystem.domain.Person;

public class RemarkDto {
    private String remark;
    private Person person;

    public RemarkDto(String remark, Person person) {
        this.remark = remark;
        this.person = person;
    }
}
