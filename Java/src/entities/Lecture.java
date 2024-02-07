package entities;

import java.util.Date;

/**
 * Represents a lecture in the university system.
 */
public class Lecture {
    private Subject subject;
    private Date date;

    /**
     * Constructs a new lecture object with the specified subject and date.
     *
     * @param subject The subject of the lecture
     * @param date    The date of the lecture
     */
    public Lecture(Subject subject, Date date) {
        this.subject = subject;
        this.date = date;
    }
    public Date getDate() {
        return date;
    }
    public Subject getSubject() {
        return subject;
    }
}

