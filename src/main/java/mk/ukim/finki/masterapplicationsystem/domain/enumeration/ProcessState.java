package mk.ukim.finki.masterapplicationsystem.domain.enumeration;

public enum ProcessState {

    APPLICATION("Пријава"),
    DOCUMENT_APPLICATION("Прикачување на документација"),
    INITIAL_MENTOR_REVIEW("Проверка од ментор"),
    STUDENT_SERVICE_REVIEW("Проверка од студентска служба"),
    INITIAL_NNK_REVIEW("Проверка од ННК"),
    INITIAL_SECRETARY_REVIEW("Секретар валидира"),
    STUDENT_DRAFT("Студент прикачува драфт"),
    DRAFT_MENTOR_REVIEW("Ментор валидира"),
    DRAFT_SECRETARY_REVIEW("Секретар валидира"),
    DRAFT_NNK_REVIEW("ННК валидира"),
    SECOND_DRAFT_SECRETARY_REVIEW("Секретар валидира"),
    DRAFT_COMMITTEE_REVIEW("Проверка на драфт"),
    STUDENT_CHANGES_DRAFT("Студент менува драфт верзија"),
    MENTOR_REPORT("Ментор затвора циклус на поправки и прикачува извештај"),
    REPORT_REVIEW("Валидирање на извештај"),
    REPORT_SECRETARY_REVIEW("Секретар валидира извештај"),
    REPORT_STUDENT_SERVICE("Студентса служба валидира извештај"),
    APPLICATION_FINISHED("Процесот на апликација е завршен. Се чека одбрана на магистерската"),
    FINISHED("Одбранета магистерска");

    public final String label;

    ProcessState(String label) {
        this.label = label;
    }
}