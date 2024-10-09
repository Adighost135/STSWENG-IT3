package ph.edu.dlsu.enlistment;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static ph.edu.dlsu.enlistment.Days.*;
import static ph.edu.dlsu.enlistment.Period.*;

class StudentTest {

    final static Schedule MTH_H0830 = new Schedule(MTH, H0830);

    static Student newStudent(){
        return new Student(1, Collections.emptyList(), Collections.emptyList(), Programs.BSCS);
    }

    final static Subject MTH101 = new Subject("MTH101", 3, false, Arrays.asList(), Programs.BSCS);
    final static Subject PHYSICS = new Subject("PHYSICS", 3, true, Arrays.asList(MTH101), Programs.BSEC);
    final static Subject CSSWENG = new Subject("CSSWENG", 3, false, Arrays.asList(), Programs.BSCS);
    final static Subject STSWENG = new Subject("STSWENG", 3, false, Arrays.asList(CSSWENG), Programs.BSCS);
    final static Subject LBYARCH = new Subject("LBYARCH", 1, true, Arrays.asList(), Programs.BSCS);

    static Subject[] completedSubjects = new Subject[]{MTH101, PHYSICS, CSSWENG, LBYARCH};


//    // main line of logic (happy path)
//    @Test
//    void enlist_with_no_conflict_room_capacity_not_exceeded() {
//        // Given a student no enlistments yet
//        Student student = newStudent();
//        // room is not max capacity
//        Room room = new Room("Room1", 2);
//        // and two section with no conflict
//        Section sec1 = new Section("A", MTH_H0830, room); // Schedule(days, period)
//        Section sec2 = new Section("B", new Schedule(TF, H0830), room);
//        // When the student enlists in both
//        student.enlist(sec1);
//        student.enlist(sec2);
//        // Then we should find those two sections
//        var sections = student.getSections();
//        assertAll(
//                () ->  assertTrue(sections.containsAll(List.of(sec1, sec2))),
//                // in the student and only two sections
//                () -> assertEquals(2, sections.size())
//        );
//    }
//
//    @Test
//    void enlist_with_schedule_conflict_room_capacity_not_exceeded() {
//        // Given a student and two sections with the same schedule
//        Student student = newStudent();
//
//        // room is not max capacity
//        Room room = new Room("Room1", 2);
//
//        // and two sections with the same schedule
//        Section sec1 = new Section("A", MTH_H0830, room);
//        Section sec2 = new Section("B", MTH_H0830, room);
//
//        // When the student enlist in both,
//        student.enlist(sec1);
//        // then an exception is thrown at the 2nd enlistment
//        assertThrows(ScheduleConflictException.class, () -> student.enlist(sec2));
//    }
//
//    @Test
//    void enlist_with_room_capacity_exceeded(){
//        // Given two students and a sections with the same schedule
//
//        Student student1 = newStudent();
//        Student student2 = new Student(2, Collections.emptyList());
//
//        // room capacity exceeds
//        Room room = new Room("Room1", 1);
//
//        // and two sections with the same schedule
//        Section section = new Section("A", MTH_H0830, room);
//
//        // When the student enlist in both,
//        student1.enlist(section);
//        // then an exception is thrown at the 2nd enlistment
//        assertThrows(IllegalStateException.class, () -> student2.enlist(section));
//    }
//
//    @Test
//    void cancel_enlisted_section(){
//        // Given two students with an enlisted section
//        Student student1 = newStudent();
//        Student student2 = new Student(2, Collections.emptyList());
//
//        // room capacity exceeds
//        Room room = new Room("Room1", 1);
//
//        // and two sections with the same schedule
//        Section section = new Section("A", MTH_H0830, room);
//
//        // student1 enlist in section A,
//        student1.enlist(section);
//
//        // check if the student1 successfully enlist on the section
//        assertFalse(student1.getSections().isEmpty());
//
//        // student1 cancel enlistment
//        student1.cancelEnlist(section);
//
//        // check again if the student1 successfully cancelled the enlistment
//        assertTrue(student1.getSections().isEmpty());
//
//        // student2 enlist in section A,
//        student2.enlist(section);
//
//        // check if the student2 successfully enlist on the section
//        assertFalse(student2.getSections().isEmpty());
//
//        // check if student1 is currently enrolled in that section
//        var section_student1 = student1.getSections();
//        assertEquals(0, section_student1.size());
//
//        // check if student2 is currently enrolled in that section
//        var section_student2 = student2.getSections();
//        assertEquals(1, section_student2.size());
//
//    }

    @Test
    void subject_not_same(){
        //
        Student student1 = newStudent();

        //
        Room room = new Room("Room1", 1);

        //
        Subject subject1 = new Subject("CS101", 3, false, Collections.emptyList());

        //
        Subject subject2 = new Subject("MTH101", 3, false, Collections.emptyList());

        //
        Section section1 = new Section("A", MTH_H0830, room, subject1);

        //
        Section section2 = new Section("B", new Schedule(TF, H0830), room, subject2);

        //
        student1.enlist(section1);

        //
        student1.enlist(section2);

        //
        assertFalse(student1.getSections().isEmpty());

        // check for subject conflict


    }

    @Test
    void subject_same(){
        //
        Student student1 = newStudent();

        //
        Room room = new Room("Room1", 1);

        //
        Subject subject1 = new Subject("CS101", 3, false, Collections.emptyList());

        //
        Subject subject2 = new Subject("CS101", 3, false, Collections.emptyList());

        //
        Section section1 = new Section("A", MTH_H0830, room, subject1);

        //
        Section section2 = new Section("B", new Schedule(TF, H0830), room, subject2);

        //
        student1.enlist(section1);

        // check for subject conflict
        assertThrows(SubjectConflictException.class, () -> student1.enlist(section2));
    }
    @Test
    void taken_prerequisite_subjects(){
        //
        Student student1 = new Student(1, Collections.emptyList(), Arrays.asList(completedSubjects));

        // check if student1 has the completed subjects
        var subject_student1 = student1.getCompletedSubjects();
        assertEquals(4, subject_student1.size());

        //
        Room room = new Room("Room1", 1);

        //
        Subject subject1 = new Subject("STSWENG", 3, false, List.of(CSSWENG));

        //
        Section section1 = new Section("A", MTH_H0830, room, subject1);

        //
        student1.enlist(section1);
    }

    @Test
    void has_not_taken_prerequisite_subjects(){
        //
        Student student1 = new Student(1, Collections.emptyList(), Arrays.asList(completedSubjects));

        // check if student1 has the completed subjects
        var subject_student1 = student1.getCompletedSubjects();
        assertEquals(4, subject_student1.size());

        //
        Room room = new Room("Room1", 1);

        //
        Subject subject1 = new Subject("CS301", 3, false, List.of(STSWENG));

        //
        Section section1 = new Section("A", MTH_H0830, room, subject1);

        //
        student1.enlist(section1);
    }

    @Test
    void enlist_more_than_24_units(){

        Student student1 = newStudent();

        //make 9 sections with its respective schedule, subject, and rooms that will sum up to 24 units
        Subject subject1 = new Subject("CS301", 3, false, Collections.emptyList());
        Subject subject2 = new Subject("CS302", 3, false, Collections.emptyList());
        Subject subject3 = new Subject("CS303", 3, false, Collections.emptyList());
        Subject subject4 = new Subject("CS304", 3, false, Collections.emptyList());
        Subject subject5 = new Subject("CS305", 3, false, Collections.emptyList());
        Subject subject6 = new Subject("CS306", 3, false, Collections.emptyList());
        Subject subject7 = new Subject("CS307", 3, false, Collections.emptyList());
        Subject subject8 = new Subject("CS308", 3, false, Collections.emptyList());
        Subject subject9 = new Subject("CS309", 3, false, Collections.emptyList());

        Room room1 = new Room("Room1", 1);
        Room room2 = new Room("Room2", 1);
        Room room3 = new Room("Room3", 1);
        Room room4 = new Room("Room4", 1);
        Room room5 = new Room("Room5", 1);
        Room room6 = new Room("Room6", 1);
        Room room7 = new Room("Room7", 1);
        Room room8 = new Room("Room8", 1);
        Room room9 = new Room("Room9", 1);

        Schedule MTH_H1000 = new Schedule(MTH, H1000);
        Schedule MTH_H1130 = new Schedule(MTH, H1130);
        Schedule MTH_H1300 = new Schedule(MTH, H1300);
        Schedule MTH_H1430 = new Schedule(MTH, H1430);
        Schedule MTH_H1600 = new Schedule(MTH, H1600);
        Schedule TF_H0830 = new Schedule(TF, H0830);
        Schedule TF_H1000 = new Schedule(TF, H1000);
        Schedule TF_H1130 = new Schedule(TF, H1130);
        Schedule TF_H1300 = new Schedule(TF, H1300);

        Section section1 = new Section("A", MTH_H1000, room1, subject1);
        Section section2 = new Section("B", MTH_H1130, room2, subject2);
        Section section3 = new Section("C", MTH_H1300, room3, subject3);
        Section section4 = new Section("D", MTH_H1430, room4, subject4);
        Section section5 = new Section("E", MTH_H1600, room5, subject5);
        Section section6 = new Section("F", TF_H0830, room6, subject6);
        Section section7 = new Section("G", TF_H1000, room7, subject7);
        Section section8 = new Section("H", TF_H1130, room8, subject8);
        Section section9 = new Section("I", TF_H1300, room9, subject9);

        student1.enlist(section1);
        student1.enlist(section2);
        student1.enlist(section3);
        student1.enlist(section4);
        student1.enlist(section5);
        student1.enlist(section6);
        student1.enlist(section7);
        student1.enlist(section8);

        //enlist student in another section that will exceed the maximum units
        assertThrows(IllegalStateException.class, () -> student1.enlist(section9));

    }

    @Test
    void schedule_conflict_of_section_in_a_room(){
        Subject subject1 = new Subject("CS301", 3, false, Collections.emptyList());
        Subject subject2 = new Subject("CS302", 3, false, Collections.emptyList());

        Room room1 = new Room("Room1", 1);

        Schedule MTH_H1000 = new Schedule(MTH, H1000);

        Section section1 = new Section("A", MTH_H1000, room1, subject1);

        //enlist student in another section that will exceed the maximum units
        assertThrows(IllegalStateException.class, () -> {Section section2 = new Section("B", MTH_H1000, room1, subject2);});
    }

    @Test
    void change_section_room(){

        Subject subject1 = new Subject("CS302", 3, false, Collections.emptyList());

        Room room1 = new Room("Room1", 1);
        Room room2 = new Room("Room2", 1);

        Schedule MTH_H1000 = new Schedule(MTH, H1000);

        Section section1 = new Section("A", MTH_H1000, room1, subject1);

        //change the room of a section
        section1.changeRoom(room2);

    }

    @Test
    void change_section_room_with_conflict(){
        Subject subject1 = new Subject("CS301", 3, false, Collections.emptyList());

        Room room1 = new Room("Room1", 1);
        Room room2 = new Room("Room1", 1);

        Schedule MTH_H1000 = new Schedule(MTH, H1000);

        Section section1 = new Section("A", MTH_H1000, room1, subject1);
        Section section2 = new Section("B", MTH_H1000, room2, subject1);

        //change the room of a section but has conflicting schedule with another section
        assertThrows(IllegalStateException.class, () -> section2.changeRoom(room1));

    @Test
    void taking_subject_outside_degree_program(){

        Student student1 = new Student(1, Collections.emptyList(), Arrays.asList(completedSubjects), Programs.BSCS);

        Room room = new Room("Room1", 2);

        Section section1 = new Section("A", MTH_H0830, room, PHYSICS);

        try {
            student1.enlist(section1);
        } catch (IllegalArgumentException ex) {
            assertEquals("cannot enlist in section outside degree program: A", ex.getMessage());
        }
    }

}

