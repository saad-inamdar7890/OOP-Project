package Student_Result;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ResultEntry {

    private final StringProperty subject;
    private final StringProperty code;
    private final StringProperty grade;

    private final StringProperty credit;


    public ResultEntry(String subject, String code , String credit , String grade) {
        this.subject = new SimpleStringProperty(subject);
        this.code = new SimpleStringProperty(code);
        this.grade = new SimpleStringProperty(grade);
        this.credit = new SimpleStringProperty(credit);

    }

    public String getSubject() {
        return subject.get();
    }

    public StringProperty subjectProperty() {
        return subject;
    }


    public void setSubject(String subject) {
        this.subject.set(subject);
    }




    public void setcode(String code) {
        this.code.set(code);
    }


    public String getcode() {
        return code.get();
    }

    public StringProperty codeProperty() {
        return code;
    }



    public String getCredit() {
        return credit.get();
    }

    public StringProperty creditProperty() {
        return credit;
    }


    public void setCredit(String credit) {
        this.credit.set(credit);
    }




    public String getGrade() {
        return grade.get();
    }

    public StringProperty gradeProperty() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
    }
}
