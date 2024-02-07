package database;

import entities.*;
import gui.ComboBoxes;
import gui.MainWindow;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class manages the database and provides methods to retrieve groups,
 * lectures, and students from the database.
 *
 * @author M.O. Hodkova
 * @version 1.0
 */
public class DatabaseManager {
    private static Connection connection;
    private static final ArrayList<Group> groups = new ArrayList<>();
    private static final ArrayList<Subject> subjects = new ArrayList<>();
    private static final ArrayList<Lecture> lectures = new ArrayList<>();
    private static final ArrayList<Student> students = new ArrayList<>();

    /**
     * Sets up a connection to the database.
     * @throws SQLException if there is an error working with the database
     * @throws ClassNotFoundException if the database driver class is not found
     */
    public static void setUpConnection() throws SQLException, ClassNotFoundException {
        String PASSWORD = "1122";
        String USERNAME = "root";
        String URL = "jdbc:mysql://localhost:3306/attendance?useUnicode=true&useJDBCCompliantTimezoneShift=" +
                "true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * Returns a list of all groups in the database.
     * @return a list of all groups
     */
    public static List<Group> getGroups() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select number from attendance.group");
            groups.clear();
            while (resultSet.next()) {
                groups.add(new Group(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return groups;
    }

    /**
     * Returns a list of lectures for a given subject and group.
     * @param subject the subject for which to retrieve lectures
     * @param group the group for which to retrieve lectures
     * @return a list of lectures
     */
    public static ArrayList<Lecture> getLecturesBySubjectAndGroup(Subject subject, Group group) {
        ArrayList<Lecture> lecturesByGroup = new ArrayList<>();
        try {
            if (getStudentsFromGroup(group.getGroupNumber()).size() == 0) return null;
            String firstStudent = getStudentsFromGroup(group.getGroupNumber()).get(0).getFIO();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select date from attendance.lecture" +
                    " where id in ( select lecture_id from attendance.student_lecture where student_id = " +
                    "(select id from attendance.student where name = '" + firstStudent + "')) and subject_id =" +
                    " ( select id from attendance.subject where name ='" + subject.getSubjectName() + "') ");
            while (resultSet.next()) {
                lecturesByGroup.add(new Lecture(subject, resultSet.getDate(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lecturesByGroup;
    }

    /**
     * Returns a list of lectures for a given subject.
     * @param subject the subject for which to retrieve lectures
     * @return a list of lectures
     */
    public static ArrayList<Lecture> getLecturesBySubject(Subject subject) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select date from attendance.lecture" +
                    " where subject_id = ( select id from attendance.subject where name ='"
                    + subject.getSubjectName() + "')");
            lectures.clear();
            while (resultSet.next()) {
                lectures.add(new Lecture(subject, resultSet.getDate(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lectures;
    }

    /**
     * Adds a lecture to the database and updates the lectures list.
     * @param lecture the lecture to add
     * @throws SQLException if there is an error working with the database
     */
    public static void addLecture(Lecture lecture) {
        try {
            lectures.add(lecture);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from attendance.subject" +
                    " where name = '" + lecture.getSubject().getSubjectName() + "'");

            PreparedStatement pstmt2 = connection.prepareStatement("insert into attendance.lecture (subject_id, date)" +
                    " values (?,?)");
            while (resultSet.next()) {
                pstmt2.setString(1, resultSet.getString(1));
            }
            pstmt2.setString(2, new Date(lecture.getDate().getTime()).toString());
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a student lecture attendance record to the database.
     * @param student the student attending the lecture
     * @param lecture the lecture attended by the student
     * @throws SQLException if there is an error working with the database
     */
    public static void addStudentLectureAttendance(Student student, Lecture lecture) {
        try {
            int studentId = 0;
            int lectureId = 0;
            Statement statement = connection.createStatement();
            PreparedStatement pstmt;

            ResultSet resultSet1 = statement.executeQuery("select id from attendance.student " +
                    "where group_id = (select id from attendance.group " +
                    "where number = '" + student.getGroup().getGroupNumber()
                    + "') and name = '" + student.getFIO() + "'");
            while (resultSet1.next()) {
                studentId = resultSet1.getInt(1);
            }
            ResultSet resultSet2 = statement.executeQuery("select id from attendance.lecture " +
                    "where date = '" + new Date(lecture.getDate().getTime()) + "' and subject_id = " +
                    "(select id from subject where name = '" + lecture.getSubject().getSubjectName() + "') ");

            while (resultSet2.next()) {
                lectureId = resultSet2.getInt(1);
            }

            ArrayList<StudentLectureRecord> studentLectureRecords = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("select * from attendance.student_lecture");
            while (resultSet.next()) {
                studentLectureRecords.add(new StudentLectureRecord(resultSet.getInt(1),
                        resultSet.getInt(2), resultSet.getString(3)));
            }

            for (StudentLectureRecord item : studentLectureRecords) {
                if (item.getStudentId() == studentId && item.getLectureId() == lectureId) {
                    pstmt = connection.prepareStatement("update student_lecture " +
                            "set presence = '" + student.getPresence() + "' " +
                            "where student_id = '" + studentId + "' and lecture_id = '" + lectureId + "'");
                    pstmt.executeUpdate();
                    return;
                }
            }

            pstmt = connection.prepareStatement("insert into attendance.`student_lecture` (student_id, " +
                    "lecture_id, presence) values (?,?,?)");
            pstmt.setString(1, Integer.toString(studentId));
            pstmt.setString(2, Integer.toString(lectureId));
            pstmt.setString(3, student.getPresence());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a group to the database and updates the groups list.
     * @param group the group to add
     * @throws SQLException if there is an error working with the database
     */
    public static void addGroup(Group group) {
        try {
            groups.add(group);
            PreparedStatement pstmt = connection.prepareStatement("insert into attendance.`group` (number) values (?)");
            pstmt.setString(1, group.getGroupNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a group from the database and updates the groups list.
     * @param group the group to delete
     * @throws SQLException if there is an error working with the database
     */
    public static void deleteGroup(Group group) {
        try {
            groups.remove(group);
            PreparedStatement pstmt = connection.prepareStatement("delete from attendance.`group` where number = (?)");
            pstmt.setString(1, group.getGroupNumber());
            pstmt.executeUpdate();
            System.out.println("Db delete" + group.getGroupNumber());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a list of subjects from the database.
     * @return a list of subjects
     * @throws SQLException if there is an error working with the database
     */
    public static List<Subject> getSubjects() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select name from attendance.subject");
            subjects.clear();
            while (resultSet.next()) {
                subjects.add(new Subject(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return subjects;
    }

    /**
     * Adds a subject to the database and updates the subjects list.
     * @param subject the subject to add
     * @throws SQLException if there is an error working with the database
     */
    public static void addSubject(Subject subject) {
        try {
            subjects.add(subject);
            PreparedStatement pstmt = connection.prepareStatement("insert into attendance.`subject` (name) values (?)");
            pstmt.setString(1, subject.getSubjectName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a subject from the database and updates the subjects list.
     * @param subject the subject to delete
     * @throws SQLException if there is an error working with the database
     */
    public static void deleteSubject(Subject subject) {
        try {
            subjects.remove(subject);
            PreparedStatement pstmt = connection.prepareStatement("delete from attendance.`subject` where name = (?)");
            pstmt.setString(1, subject.getSubjectName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a student to the database and updates the students list.
     * @param student the student to add
     * @throws SQLException if there is an error working with the database
     */
    public static void addStudent(Student student) {
        try {
            students.add(student);
            Statement statement = connection.createStatement();
            PreparedStatement pstmt = connection.prepareStatement("insert into attendance.`student` (name,group_id)" +
                    " values (?,?)");
            ResultSet resultSet = statement.executeQuery("select id from attendance.`group` " +
                    "where number = '" + student.getGroup().getGroupNumber() + "'");
            pstmt.setString(1, student.getFIO());
            while (resultSet.next()) {
                pstmt.setString(2, resultSet.getString(1));
            }
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if a student is already in the database.
     * @param student the student to check
     * @return true if the student is in the database, false otherwise
     * @throws SQLException if there is an error working with the database
     */
    public static boolean isStudentAdded(Student student) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from attendance.student" +
                    " where name = '" + student.getFIO() + "'");
            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Deletes a student from the database and updates the students list.
     * @param student the student to delete
     * @throws SQLException if there is an error working with the database
     */
    public static void deleteStudent(Student student) {
        try {
            students.remove(student);
            PreparedStatement pstmt = connection.prepareStatement("delete from attendance.student" +
                    " where name = (?)");
            pstmt.setString(1, student.getFIO());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a lecture from the database.
     * @param lecture the lecture to delete
     * @throws SQLException if there is an error working with the database
     */
    public static void deleteLecture(Lecture lecture) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("delete from attendance.lecture " +
                    "where subject_id = (select id from attendance.subject where name = '"
                    + lecture.getSubject().getSubjectName() + "')" +
                    "and date  ='" + new Date(lecture.getDate().getTime()) + "' ");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a list of students from a group.
     * @param groupName the name of the group to retrieve students from
     * @return a list of students from the specified group
     * @throws SQLException if there is an error working with the database
     */
    public static List<Student> getStudentsFromGroup(String groupName) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select name from attendance.student " +
                    "where group_id = (select id from attendance.group where number = '" + groupName + "')");
            students.clear();
            while (resultSet.next()) {
                students.add(new Student(resultSet.getString(1), getGroups()
                        .get(ComboBoxes.getComboBoxGroups().getSelectedIndex())));
            }
            Collections.sort(students, new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    return o1.getFIO().compareTo(o2.getFIO());
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
    }

    /**
     * Retrieves a student lecture record for a specified lecture and student.
     * @param lecture the lecture to retrieve the record for
     * @param student the student to retrieve the record for
     * @return the student lecture record for the specified lecture and student, or null if not found
     * @throws SQLException if there is an error working with the database
     */
    public static StudentLectureRecord getStudentLecture(Lecture lecture, Student student) {
        StudentLectureRecord studentLectureRecord = null;
        try {
            int studentId = 0;
            int lectureId = 0;
            Statement statement = connection.createStatement();

            ResultSet resultSet1 = statement.executeQuery("select id from attendance.student " +
                    "where group_id = (select id from attendance.group " +
                    "where number = '" + student.getGroup().getGroupNumber() + "') and name = '"
                    + student.getFIO() + "'");
            while (resultSet1.next()) {
                studentId = resultSet1.getInt(1);
            }
            ResultSet resultSet2 = statement.executeQuery("select id from attendance.lecture " +
                    "where date = '" + new Date(lecture.getDate().getTime()) + "' and subject_id = " +
                    "(select id from subject where name = '" + lecture.getSubject().getSubjectName() + "') ");

            while (resultSet2.next()) {
                lectureId = resultSet2.getInt(1);
            }
            ResultSet resultSet = statement.executeQuery("select * from attendance.student_lecture" +
                    " where lecture_id = '" + lectureId + "' and student_id = '" + studentId + "'");
            while (resultSet.next()) {
                studentLectureRecord = new StudentLectureRecord(resultSet.getInt(1),
                        resultSet.getInt(2), resultSet.getString(3));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return studentLectureRecord;
    }

    /**
     * Calculates the hours of skipped lectures for a specified student and subject.
     * @param student the student to calculate the skipped hours for
     * @param subject the subject to calculate the skipped hours for
     * @return the hours of skipped lectures for the specified student and subject
     * @throws SQLException if there is an error working with the database
     */
    public static int getHoursOfSkippedLectures(Student student, Subject subject) {
        int skippedHours = 0;
        ArrayList<Lecture> lectures = getLecturesBySubjectAndGroup(subject, student.getGroup());
        for (Lecture item : lectures) {
            switch (getStudentLecture(item, student).getPresence()) {
                case "2":
                    skippedHours += 2;
                    break;
                case "1":
                    skippedHours += 1;
            }
        }
        return skippedHours;
    }

    /**
     * Calculates the hours of lectures for a specified subject and group.
     * @param subject the subject to calculate the lecture hours for
     * @param group the group to calculate the lecture hours for
     * @return the hours of lectures for the specified subject and group
     * @throws SQLException if there is an error working with the database
     */
    public static int getHoursOfLectures(Subject subject, Group group) {
        ArrayList<Lecture> lectures = getLecturesBySubjectAndGroup(subject, group);
        return lectures.size() * 2;
    }
}
