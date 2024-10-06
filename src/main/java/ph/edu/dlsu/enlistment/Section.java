package ph.edu.dlsu.enlistment;

import org.apache.commons.lang3.*;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.Objects;

class Section {
    private final String sectionId;
    private final Schedule schedule;
    private final Room roomName;

    Section(String sectionId, Schedule schedule, Room roomName) {
        Objects.requireNonNull(sectionId);
        Objects.requireNonNull(schedule);
        Objects.requireNonNull(roomName);
        isBlank(sectionId);
        Validate.isTrue(isAlphanumeric(sectionId), "sectionId must be alphanumeric, was: "
                + sectionId);
        this.sectionId = sectionId;
        this.schedule = schedule;
        this.roomName = roomName;
    }

//    boolean hasConflict(Section other){ delete
//        return this.schedule.equals(other.schedule);
//    }

    void checkConflict(Section other){
        if(this.schedule.equals(other.schedule)){
            throw new ScheduleConflictException("this section : " + this +
                    " and other section " + other +
                    " has the same schedule at  " + schedule);
        }
    }

    void enlistStudent(){
        if (!roomName.isVacant()){
            throw new IllegalStateException("Room capacity is already full for room " + roomName);
        }
        roomName.addStudent();
    }

    void cancelEnlistment(){
        roomName.removeStudent();
    }

//    Schedule getSchedule(){
//        return schedule; delete
//    }

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






}
