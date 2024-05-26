public class Result {

    private String subject;
    private double grade;

    public Result(String subject, double grade) {
        this.subject = subject;
        this.grade = grade;
    }

    // Getters and setters (omitted for brevity)

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
