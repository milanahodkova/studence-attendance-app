package entities;

/**
 * Represents a subject in the university system.
 */
public class Subject {
    private String subjectName;

    /**
     * Constructs a new subject object with the specified name.
     *
     * @param subjectName The name of the subject
     */
    public Subject(String subjectName){
        this.subjectName = subjectName;
    }
    public String getSubjectName() {
        return subjectName;
    }
    @Override
    public String toString() {
        return this.subjectName;
    }
}
