package ph.edu.dlsu.enlistment;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static ph.edu.dlsu.enlistment.Days.*;
import static ph.edu.dlsu.enlistment.Programs.*;

class StudentTest {

    final static Schedule MTH_0830_0900 = new Schedule(MTH, "0830", "0900");

    static Student newStudent(){
        return new Student(1, Collections.emptyList(), Collections.emptyList(), BSCS);
    }

    final static Subject MTH101 = new Subject("MTH101", 3, false, Arrays.asList(), BSCS);
    final static Subject PHYSICS = new Subject("PHYSICS", 3, true, Arrays.asList(MTH101), BSEC);
    final static Subject CSSWENG = new Subject("CSSWENG", 3, false, Arrays.asList(), BSCS);
    final static Subject STSWENG = new Subject("STSWENG", 3, false, Arrays.asList(CSSWENG), BSCS);
    final static Subject LBYARCH = new Subject("LBYARCH", 1, true, Arrays.asList(), BSCS);

    static Subject[] completedSubjects = new Subject[]{MTH101, PHYSICS, CSSWENG, LBYARCH};


    // main line of logic (happy path)
    @Test
    void enlist_with_no_conflict_room_capacity_not_exceeded() {
        // Given a student with no enlistment, no completed subjects and degree program is BSCS
        Student student = newStudent();

        // room is not max capacity
        Room room = new Room("Room1", 2);

        // Given two different subjects with different subjectId, same unites, no laboratory,  no prerequisite, and same degree program
        Subject subject1 = new Subject("CS101", 3, false, Collections.emptyList(), BSCS);
        Subject subject2 = new Subject("MTH101", 3, false, Collections.emptyList(), BSCS);

        // and two section with no conflict
        Section sec1 = new Section("A", MTH_0830_0900, room, subject1);
        Section sec2 = new Section("B", new Schedule(TF, "0830", "1000"), room, subject2);
        // When the student enlists in both
        student.enlist(sec1);
        student.enlist(sec2);
        // Then we should find those two sections
        var sections = student.getSections();
        assertAll(
                () ->  assertTrue(sections.containsAll(List.of(sec1, sec2))),
                // in the student and only two sections
                () -> assertEquals(2, sections.size())
        );
    }

    @Test
    void enlist_with_schedule_conflict() {
        // Given a student with no enlistment, no completed subjects and degree program is BSCS
        Student student = newStudent();

        // Given two different rooms that is not max capacity
        Room room1 = new Room("Room1", 2);
        Room room2 = new Room("Room2", 2);

        // Given two subjects with different subject id, has 3 units, no laboratory, no prerequisites and degree program is BSCS
        Subject subject1 = new Subject("CS301", 3, false, Collections.emptyList(), BSCS);
        Subject subject2 = new Subject("CS302", 3, false, Collections.emptyList(), BSCS);

        // Given two sections with the same schedule and different rooms
        Section sec1 = new Section("A", MTH_0830_0900, room1, subject1);
        Section sec2 = new Section("B", MTH_0830_0900, room2, subject2);

        // When the student enlist in both,
        student.enlist(sec1);
        // then an exception is thrown at the 2nd enlistment
        assertThrows(ScheduleConflictException.class, () -> student.enlist(sec2));
    }

    @Test
    void enlist_with_room_capacity_exceeded(){
        // Given two students with no enlisted sections, no completed subjects, and same degree program
        Student student1 = newStudent();
        Student student2 = new Student(2, Collections.emptyList(), Collections.emptyList(), BSCS);

        // Given a room where capacity exceeds
        Room room = new Room("Room1", 1);

        // Given a subject with subjectId, units, no laboratory,no prerequisite, degree program BSCS
        Subject subject1 = new Subject("CS301", 3, false, Collections.emptyList(), BSCS);

        // Given a section with a default schedule, a room, and a subject
        Section section = new Section("A", MTH_0830_0900, room, subject1);

        // When the student enlist in both,
        student1.enlist(section);
        // Then an exception is thrown at the 2nd enlistment
        assertThrows(RoomCapacityExceededException.class, () -> student2.enlist(section));
    }

    @Test
    void cancel_enlisted_section(){
        // Given a student with no enlistment, no completed subjects, and degree program is BSCS
        Student student1 = newStudent();

        // Given a room with room name and normal capacity
        Room room = new Room("Room1", 1);

        // Given a subject with subjectId, units, no laboratory,no prerequisite, degree program BSCS
        Subject subject1 = new Subject("CS101", 3, false, Collections.emptyList(), BSCS);;

        // Given a section with the default schedule, room, and subject
        Section section = new Section("A", MTH_0830_0900, room, subject1);

        // When student1 enlist in section A,
        student1.enlist(section);

        // check if the student1 successfully enlist on the section
        assertFalse(student1.getSections().isEmpty());

        // When student1 cancel enlistment
        student1.cancelEnlist(section);

        // check again if the student1 successfully cancelled the enlistment
        assertTrue(student1.getSections().isEmpty());

    }

    @Test
    void subject_not_same(){
        // Given a student with no enlistment, no completed subjects and degree program is BSCS
        Student student1 = newStudent();

        // Given a room with normal capacity
        Room room = new Room("Room1", 1);

        // Given two different subjects with different subjectId, same unites, no laboratory,  no prerequisite, and same degree program
        Subject subject1 = new Subject("CS101", 3, false, Collections.emptyList(), BSCS);
        Subject subject2 = new Subject("MTH101", 3, false, Collections.emptyList(), BSCS);

        // Given two different sections with different schedule and different subjects
        Section section1 = new Section("A", MTH_0830_0900, room, subject1);
        Section section2 = new Section("B", new Schedule(TF, "0830", "0900"), room, subject2);

        // When  student enlists in both sections with different subjects
        // Then no issue
        student1.enlist(section1);
        student1.enlist(section2);
    }

    @Test
    void subject_same(){
        // Given a student with no enlistments, no completed subjects and degree program is BSCS
        Student student1 = newStudent();

        // Given a room capacity is normal
        Room room = new Room("Room1", 1);

        // Given two same subjects with no prerequisite and no laboratory
        Subject subject1 = new Subject("CS101", 3, false, Collections.emptyList(), BSCS);
        Subject subject2 = new Subject("CS101", 3, false, Collections.emptyList(), BSCS);

        // Given two sections with different schedules and same subjects
        Section section1 = new Section("A", MTH_0830_0900, room, subject1);
        Section section2 = new Section("B", new Schedule(TF, "0830", "0900"), room, subject2);

        // When the student enlist in both subjects
        student1.enlist(section1);

        // then an exception is thrown at the 2nd enlistment
        assertThrows(SubjectConflictException.class, () -> student1.enlist(section2));
    }
    @Test
    void taken_prerequisite_subjects(){
        // Given a student with completed subjects but no current enlisted sections yet and degree program is BSCS
        Student student1 = new Student(1, Collections.emptyList(), Arrays.asList(completedSubjects), BSCS);

        // Given a room capacity normal
        Room room = new Room("Room1", 1);

        // Given a subject with a prerequisite
        Subject subject1 = new Subject("STSWENG", 3, false, List.of(CSSWENG), BSCS);

        // Given a section with a subject that has a prerequisite
        Section section1 = new Section("A", MTH_0830_0900, room, subject1);

        // When student enlist with the prerequisite subject
        student1.enlist(section1);
    }

    @Test
    void has_not_taken_prerequisite_subjects(){
        // Given a student with completed subjects but no current enlisted sections yet and degree program is BSCS
        Student student1 = new Student(1, Collections.emptyList(), Arrays.asList(completedSubjects), BSCS);

        // Given a room capacity normal
        Room room = new Room("Room1", 1);

        // Given a subject with a prerequisite
        Subject subject1 = new Subject("CS301", 3, false, List.of(STSWENG), BSCS);

        // Given a section with a subject that has a prerequisite
        Section section1 = new Section("A", MTH_0830_0900, room, subject1);

        // When student enlist with no prerequisite subject,
        // Then an exception is thrown
        assertThrows(NoPrerequisiteException.class, () -> student1.enlist(section1));
    }


    @Test
    void enlist_more_than_24_units(){
        // Given a student with no enlistment, no completed subjects, and degree program is BSCS
        Student student1 = newStudent();

        // Given 9 subjects with its respective subjectId, units, no laboratories, no prerequsites and degree program BSCS
        Subject subject1 = new Subject("CS301", 3, false, Collections.emptyList(), BSCS);
        Subject subject2 = new Subject("CS302", 3, false, Collections.emptyList(), BSCS);
        Subject subject3 = new Subject("CS303", 3, false, Collections.emptyList(), BSCS);
        Subject subject4 = new Subject("CS304", 3, false, Collections.emptyList(), BSCS);
        Subject subject5 = new Subject("CS305", 3, false, Collections.emptyList(), BSCS);
        Subject subject6 = new Subject("CS306", 3, false, Collections.emptyList(), BSCS);
        Subject subject7 = new Subject("CS307", 3, false, Collections.emptyList(), BSCS);
        Subject subject8 = new Subject("CS308", 3, false, Collections.emptyList(), BSCS);
        Subject subject9 = new Subject("CS309", 3, false, Collections.emptyList(), BSCS);

        // Given 9 rooms with normal capacities
        Room room1 = new Room("Room1", 1);
        Room room2 = new Room("Room2", 1);
        Room room3 = new Room("Room3", 1);
        Room room4 = new Room("Room4", 1);
        Room room5 = new Room("Room5", 1);
        Room room6 = new Room("Room6", 1);
        Room room7 = new Room("Room7", 1);
        Room room8 = new Room("Room8", 1);
        Room room9 = new Room("Room9", 1);

        // Given 9 schedules with valid periods
        Schedule MTH_1000_1100 = new Schedule(MTH, "1000", "1100");
        Schedule MTH_1130_1230 = new Schedule(MTH, "1130", "1230");
        Schedule MTH_1300_1400 = new Schedule(MTH, "1300", "1400");
        Schedule MTH_1430_1600 = new Schedule(MTH, "1430", "1600");
        Schedule MTH_1630_1730 = new Schedule(MTH, "1630", "1730");
        Schedule TF_0830_0930 = new Schedule(TF, "0830", "0930");
        Schedule TF_1000_1130 = new Schedule(TF, "1000", "1130");
        Schedule TF_1200_1330 = new Schedule(TF, "1200", "1330");
        Schedule TF_1430_1600 = new Schedule(TF, "1430", "1600");

        // Given 9 sections with a valid schedules, rooms, and subjects with no prerequisites
        Section section1 = new Section("A", MTH_1000_1100, room1, subject1);
        Section section2 = new Section("B", MTH_1130_1230, room2, subject2);
        Section section3 = new Section("C", MTH_1300_1400, room3, subject3);
        Section section4 = new Section("D", MTH_1430_1600, room4, subject4);
        Section section5 = new Section("E", MTH_1630_1730, room5, subject5);
        Section section6 = new Section("F", TF_0830_0930, room6, subject6);
        Section section7 = new Section("G", TF_1000_1130, room7, subject7);
        Section section8 = new Section("H", TF_1200_1330, room8, subject8);
        Section section9 = new Section("I", TF_1430_1600, room9, subject9);

        // When student enlists in all of the sections
        student1.enlist(section1);
        student1.enlist(section2);
        student1.enlist(section3);
        student1.enlist(section4);
        student1.enlist(section5);
        student1.enlist(section6);
        student1.enlist(section7);
        student1.enlist(section8);

        // Then an exception is thrown at the 9th enlistment due to the reaching the maximum units
        assertThrows(IllegalStateException.class, () -> student1.enlist(section9));

    }

    @Test
    void taking_subject_outside_degree_program(){
        // Given a student with no enlistment, with completed subjects, and degree program is BSCS
        Student student1 = new Student(1, Collections.emptyList(), Arrays.asList(completedSubjects), BSCS);

        // Given a room with room name and capacity is 2
        Room room = new Room("Room1", 2);

        // Given a section with a valid schedule, room, and subject with no prerequisite
        Section section1 = new Section("A", MTH_0830_0900, room, PHYSICS);

        // When a section change the room but has conflicting schedule with another section
        // Then an exception is thrown at section2 that is attempting to change with a conflicting room
        assertThrows(IllegalArgumentException.class, () -> student1.enlist(section1));

    }

    @Test
    void view_student_assessment(){
        // Given a student with completed subjects but no current enlisted sections yet and degree program is BSEC
        Student student1 = new Student(1, Collections.emptyList(), Arrays.asList(PHYSICS), BSEC);

        // Given a room capacity normal
        Room room = new Room("Room1", 1);

        // Given a schedule
        Schedule MTH_1630_1730 = new Schedule(MTH, "1630", "1730");
        Schedule TF_0830_0930 = new Schedule(TF, "0830", "0930");

        // Given a subject with a prerequisite
        Subject subject1 = new Subject("ADVPHYSICS", 3, false, List.of(PHYSICS), BSEC);
        Subject subject2 = new Subject("LBADVPHYSICS", 1, true, List.of(PHYSICS), BSEC);
        Subject subject3 = new Subject("THERMODYNAMICS", 3, true, List.of(PHYSICS), BSEC);

        // Given a section with a subject that has a prerequisite
        Section section1 = new Section("A", MTH_0830_0900, room, subject1);
        Section section2 = new Section("B", MTH_1630_1730, room, subject2);
        Section section3 = new Section("C", TF_0830_0930, room, subject3);

        student1.enlist(section1);
        student1.enlist(section2);
        student1.enlist(section3);

        // When student enlist with no prerequisite subject,
        BigDecimal student_assessment = student1.studentTotalAssessment();

        // total units: (3 + 1 + 3) * 2000
        // laboratory: (1 + 1) * 1000
        // misc. fee: 3000
        // VAT: 12% or .12
        // Set the expectedTotal based on the given enlisted subjects units and if there's laboratory
        BigDecimal expectedTotal = new BigDecimal(21280.00).setScale(2, RoundingMode.HALF_UP);

        // Then an exception is thrown
        assertEquals(expectedTotal, student_assessment);
    }

}

