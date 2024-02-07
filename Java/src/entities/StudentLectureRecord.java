package entities;

/**
 * Represents a record of a student's presence at a lecture.
 */
public class StudentLectureRecord {
    private int studentId;
    private int lectureId;
    private String presence;

    /**
     * Constructs a new student lecture record object with the specified student ID,
     * lecture ID, and presence status.
     *
     * @param studentId The ID of the student
     * @param lectureId The ID of the lecture
     * @param presence The presence status of the student at the lecture
     */
    public StudentLectureRecord(int studentId, int lectureId, String presence) {
        this.studentId = studentId;
        this.lectureId = lectureId;
        this.presence = presence;
    }
    public int getStudentId() {
        return studentId;
    }
    public int getLectureId() {
        return lectureId;
    }
    public String getPresence() {
        return presence;
    }
}
