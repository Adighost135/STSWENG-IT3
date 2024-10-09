package ph.edu.dlsu.enlistment;

import org.apache.commons.lang3.*;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.Objects;

class Section {
    private final String sectionId;
    private final Schedule schedule;
    private Room room;
    private final Subject subject;
    private int currEnlisted = 0;

    Section(String sectionId, Schedule schedule, Room room, Subject subject) {
        Objects.requireNonNull(sectionId);
        Objects.requireNonNull(schedule);
        Objects.requireNonNull(room);
        Objects.requireNonNull(subject);
        isBlank(sectionId);
        Validate.isTrue(isAlphanumeric(sectionId), "sectionId must be alphanumeric, was: "
                + sectionId);
        this.sectionId = sectionId;
        this.schedule = schedule;
        this.room = room;
        this.subject = subject;
        this.room.assignSection(this);
    }

    void checkConflict(Section other){
        if(this.schedule.equals(other.schedule)){
            throw new ScheduleConflictException("this section : " + this +
                    " and other section " + other +
                    " has the same schedule at  " + schedule);
        }
        if(this.subject.equals(other.subject)){
            throw new SubjectConflictException("this section : " + this +
                    " and other section " + other +
                    " has the same subject at  " + subject);
        }
    }

    public void changeRoom(Room newRoom) {
        try{
            newRoom.assignSection(this);
            room = newRoom;
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        }
    }

//    void checkPrerequisites(Collection<Subject> takenSubjects){
//        for (Subject prerequisite : prerequisites) {
//            if (!takenSubjects.contains(prerequisite)) {
//                throw new IllegalArgumentException("Student has not yet taken the prerequisite of " +
//                        "subject only taken subjects: " + takenSubjects + ". Required Prerequisite " +
//                        "subject/s: " + prerequisites);
//            }
//        }
//    }
    Subject getSubject(){
        return subject;
    }
    void enlistStudent(){
        room.isVacant(currEnlisted);
//        subject.checkPrerequisites(subject);
        currEnlisted += 1;
    }

    void cancelEnlistment(){
        currEnlisted -= 1;
    }

    @Override
    public String toString() {
        return sectionId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section section)) return false;

        return Objects.equals(sectionId, section.sectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sectionId);
    }

    public Schedule getSchedule(){
        return new Schedule(this.schedule.getDays(), this.schedule.getPeriod());
    };



}
