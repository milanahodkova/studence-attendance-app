package entities;

/**
 * Represents a group of students in the university system.
 */
public class Group {
    private String groupNumber;

    /**
     * Constructs a new group object with the specified group number.
     *
     * @param groupNumber The number of the group
     */
    public Group(String groupNumber){
        this.groupNumber=groupNumber;
    }
    public String getGroupNumber(){
        return groupNumber;
    }
    @Override
    public String toString() {
        return this.groupNumber;
    }
}

