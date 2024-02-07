package entities;

/**
 * Represents a student in the university system.
 */
public class Student {
    private String FIO;
    private Group group;
    private String presence;

    /**
     * Constructs a new student object with the specified name and group.
     *
     * @param FIO   The full name of the student
     * @param group The group the student belongs to
     */
    public Student(String FIO, Group group){
        this.FIO = FIO;
        this.group = group;
        this.presence = " ";
    }

    /**
     * Constructs a new student object with the specified name, group, and attendance record.
     *
     * @param FIO      The full name of the student
     * @param group    The group the student belongs to
     * @param presence The attendance record for the student
     */
    public Student(String FIO, Group group, String presence) {
        this.FIO = FIO;
        this.group = group;
        this.presence = presence;
    }

    public Group getGroup() {
        return group;
    }

    public String getFIO() {
        return FIO;
    }

    public String getPresence() {
        return presence;
    }
}
