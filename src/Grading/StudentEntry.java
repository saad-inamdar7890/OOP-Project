package Grading;

public class StudentEntry {
    private String studentId;
    private String studentName;
    private String marks;
    private String grade;

    public StudentEntry(String studentId, String studentName, String marks, String grade) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.marks = marks;
        this.grade = grade;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
