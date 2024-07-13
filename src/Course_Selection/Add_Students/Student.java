package Course_Selection.Add_Students;

public class Student {
    private String name;
    private String id;
    private String marks;

    public Student(String name, String id, String marks) {
        this.name = name;
        this.id = id;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getMarks() {
        return marks;
    }
}
