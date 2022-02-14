package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Process;

public interface EmailService {
    void sendMailForProcess(Process process);
}
