package models;

public class Student {
    private int studentId;
    private String name;
    private String department;
    private String contact;

    public Student() {}

    public Student(int studentId, String name, String department, String contact) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.contact = contact;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    @Override
    public String toString() {
        return name; // Useful for ComboBoxes if needed
    }
}
