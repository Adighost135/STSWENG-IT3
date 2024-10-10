package ph.edu.dlsu.enlistment;

import org.apache.commons.lang3.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.util.*;

class Subject {
    private final String subjectId;
    private final int units;
    private final boolean isLaboratory;
    private final Collection<Subject> prerequisites = new HashSet<>();
    private final Programs degreeProgram;


    Subject(String subjectId, int units, boolean isLaboratory, Collection<Subject> prerequisites, Programs degreeProgram){
        Objects.requireNonNull(subjectId);
        // Objects.requireNonNull(units);
        // Objects.requireNonNull(isLaboratory);
        Validate.isTrue(isAlphanumeric(subjectId), "subjectId must be alphanumeric, was: " +subjectId);
        if (units < 0 || units > 4) {
            throw new IllegalStateException("Units can't be negative or above 4. was: " + units);
        }
        this.subjectId = subjectId;
        this.units = units;
        this.isLaboratory = isLaboratory;
        this.prerequisites.addAll(prerequisites);
        this.degreeProgram = degreeProgram;
    }


    /* If any prerequisite is not found in the 
    takenSubjects collection, the method immediately 
    returns false, indicating that not all prerequisites 
    have been met.
    */

    void checkPrerequisites(Collection<Subject> takenSubjects) {
        for (Subject prerequisite : prerequisites) {
            if (!takenSubjects.contains(prerequisite)) {
                throw new NoPrerequisiteException("Student has not yet taken the prerequisite of " +
                        "subject only taken subjects: " + takenSubjects + ". Required Prerequisite " +
                        "subject/s: " + prerequisites);
            }
        }
    }

    boolean checkDegreeProgram(Programs program){
        return degreeProgram == program;
    }

    int getUnits() {
        return units;
    }

    boolean isLaboratory(){
        return isLaboratory;
    }

    @Override
    public String toString() {
        return subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return subjectId.equals(subject.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId);
    }

}
