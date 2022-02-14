package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Master;
import mk.ukim.finki.masterapplicationsystem.domain.Person;
import mk.ukim.finki.masterapplicationsystem.domain.Process;
import mk.ukim.finki.masterapplicationsystem.domain.enumeration.ProcessState;
import mk.ukim.finki.masterapplicationsystem.service.EmailService;
import mk.ukim.finki.masterapplicationsystem.service.PersonService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    private final PersonService personService;

    private final TemplateEngine templateEngine;

    private final ProcessStateHelperService processStateHelperService;

    public EmailServiceImpl(JavaMailSender emailSender, PersonService personService, TemplateEngine templateEngine, ProcessStateHelperService processStateHelperService) {
        this.emailSender = emailSender;
        this.personService = personService;
        this.templateEngine = templateEngine;
        this.processStateHelperService = processStateHelperService;
    }

    public void sendMail(Master master, Person receiver,ProcessState processState)
            throws MessagingException {

        final Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("name", receiver.getFullName());
        ctx.setVariable("student",master.getStudent());
        ctx.setVariable("processStep",processState.label);

        final MimeMessage mimeMessage = this.emailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject("Магистарска");
        message.setFrom("vtest7305@gmail.com");
        message.setTo(receiver.getEmail());

        final String htmlContent = this.templateEngine.process("MailTemplate", ctx);
        message.setText(htmlContent, true);

        this.emailSender.send(mimeMessage);

    }

    public void sendMailForProcess(Process process) {
        Master master = process.getMaster();
        ProcessState processState = process.getProcessState();
        List<Person> receivers = processStateHelperService.getEmailReceivers(process.getId());
        receivers.forEach(receiver-> {
            try {
                this.sendMail(master,receiver,processState);
            } catch (MessagingException e) {
                //TODO:SCHEDULE TASK ZA PRAKJANJE MAIL U 10min
                e.printStackTrace();
            }
        });
    }

    private void sendSimpleMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vtest7305@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    private void sendMailToStudentForNewMaster(Master master) {
        sendSimpleMessage(master.getStudent().getEmail(), "Магистарска", "Вашата магистарска беше креирана.");
    }

    private void sendMailToStudentForDraft(Master master) {
        sendSimpleMessage(master.getStudent().getEmail(), "Магистарска", "Потребно е да прикачите магистарска.");
    }

    private void sendMailToStudentForDraftChange(Master master) {
        Person student = master.getStudent();
        sendSimpleMessage(student.getEmail(), "Забелешки за магистарска", "На вашата магистарска имате забалешки.");
    }

    private void sendMailToStudentForMasterFinished(Master master) {
        Person student = master.getStudent();
        sendSimpleMessage(student.getEmail(), "Магистарска", "Звршена магистарска, потребно е да се одбрани!");
    }

    private void sendMailToMentorForDocumentValidation(Master master) {
        sendSimpleMessage(master.getMentor().getEmail(), "Потврда на документи за магистарска", "Потребна е ваша потрвда на магистерската.");
    }

    private void sendMailToStudentServiceForDocumentValidation(Master master) {
        List<Person> studentServicePeople = personService.getStudentServiceMembers();
        for (Person p :
                studentServicePeople) {
            sendSimpleMessage(p.getEmail(), "Потврда на документи за магистарска", "Потребна е ваша потрвда на магистерската.");
        }
    }

    private void sendMailToSecretaryForDocumentValidation(Master master) {
        List<Person> secretaries = personService.getAllSecretaries();
        for (Person p :
                secretaries) {
            sendSimpleMessage(p.getEmail(), "Потврда на документи за магистарска", "Потребна е ваша потрвда на магистерската.");
        }
    }

    private void sendMailToNNKForDocumentValidation(Master master) {
        List<Person> secretaries = personService.getAllNNKMembers();
        for (Person p :
                secretaries) {
            sendSimpleMessage(p.getEmail(), "Потврда на документи за магистарска", "Потребна е ваша потрвда на магистерската.");
        }
    }

    private void sendMailToMentorForDraftValidation(Master master) {
        sendSimpleMessage(master.getMentor().getEmail(), "Потврда на иницијална верзија на магистарска", "Потребна е ваша потрвда на магистерската.");
    }

    private void sendMailToSecretaryForDraftValidation(Master master) {
        List<Person> secretaries = personService.getAllSecretaries();
        for (Person p :
                secretaries) {
            sendSimpleMessage(p.getEmail(), "Потврда на иницијална верзија на магистарска", "Потребна е ваша потрвда на магистерската.");
        }
    }

    private void sendMailToNNKForDraftValidation(Master master) {
        List<Person> secretaries = personService.getAllNNKMembers();
        for (Person p :
                secretaries) {
            sendSimpleMessage(p.getEmail(), "Потврда на иницијална верзија на магистарска", "Потребна е ваша потрвда на магистерската.");
        }
    }

    private void sendMailToCommitteeForDraftValidation(Master master) {
        Person committeeOne = master.getCommitteeFirst();
        Person committeeTwo = master.getCommitteeSecond();
        sendSimpleMessage(committeeOne.getEmail(), "Валидација на магистарска", "Нова верзија на магистерската е достапна за преглед");
        sendSimpleMessage(committeeTwo.getEmail(), "Валидација на магистарска", "Нова верзија на магистерската е достапна за преглед");
    }

    private void sendMailToCommitteeForReportValidation(Master master) {
        Person committeeOne = master.getCommitteeFirst();
        Person committeeTwo = master.getCommitteeSecond();
        sendSimpleMessage(committeeOne.getEmail(), "Валидација на извештај", "Потребно е да валидирате извештајот на менторот.");
        sendSimpleMessage(committeeTwo.getEmail(), "Валидација на извештај", "Потребно е да валидирате извештајот на менторот.");
    }

    private void sendMailToSecretaryForReportValidation(Master master) {
        List<Person> secretaries = personService.getAllSecretaries();
        for (Person p :
                secretaries) {
            sendSimpleMessage(p.getEmail(), "Валидација на извештај", "Потребно е да валидирате извештајот на менторот.");
        }
    }

    private void sendMailToStudentServiceForReportValidation(Master master) {
        List<Person> studentServicePeople = personService.getStudentServiceMembers();
        for (Person p :
                studentServicePeople) {
            sendSimpleMessage(p.getEmail(), "Валидација на извештај", "Потребно е да валидирате извештајот на менторот.");
        }
    }

}
