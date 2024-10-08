package ph.edu.dlsu.enlistment;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static ph.edu.dlsu.enlistment.Days.*;
import static ph.edu.dlsu.enlistment.Period.*;

class StudentTest {

    final static Schedule MTH_H0830 = new Schedule(MTH, H0830);

    static Student newStudent(){
        return new Student(1, Collections.emptyList(), Collections.emptyList());
    }

    final static Subject MTH101 = new Subject("MTH101", 3, false, Arrays.asList());
    final static Subject PHYSICS = new Subject("PHYSICS", 3, true, Arrays.asList(MTH101));
    final static Subject CSSWENG = new Subject("CSSWENG", 3, false, Arrays.asList());
    final static Subject STSWENG = new Subject("STSWENG", 3, false, Arrays.asList(CSSWENG));
    final static Subject LBYARCH = new Subject("LBYARCH", 1, true, Arrays.asList());

    static Subject[] completedSubjects = new Subject[]{MTH101, PHYSICS, CSSWENG, LBYARCH};


    // main line of logic (happy path)
    @Test
    void enlist_with_no_conflict_room_capacity_not_exceeded() {
        // Given a student no enlistments yet
        Student student = newStudent();
        // room is not max capacity
        Room room = new Room("Room1", 2);
        // and two section with no conflict
        Section sec1 = new Section("A", MTH_H0830, room); // Schedule(days, period)
        Section sec2 = new Section("B", new Schedule(TF, H0830), room);
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
    void enlist_with_schedule_conflict_room_capacity_not_exceeded() {
        // Given a student and two sections with the same schedule
        Student student = newStudent();

        // room is not max capacity
        Room room = new Room("Room1", 2);

        // and two sections with the same schedule
        Section sec1 = new Section("A", MTH_H0830, room);
        Section sec2 = new Section("B", MTH_H0830, room);

        // When the student enlist in both,
        student.enlist(sec1);
        // then an exception is thrown at the 2nd enlistment
        assertThrows(ScheduleConflictException.class, () -> student.enlist(sec2));
    }

    @Test
    void enlist_with_room_capacity_exceeded(){
        // Given two students and a sections with the same schedule

        Student student1 = newStudent();
        Student student2 = new Student(2, Collections.emptyList());

        // room capacity exceeds
        Room room = new Room("Room1", 1);

        // and two sections with the same schedule
        Section section = new Section("A", MTH_H0830, room);

        // When the student enlist in both,
        student1.enlist(section);
        // then an exception is thrown at the 2nd enlistment
        assertThrows(IllegalStateException.class, () -> student2.enlist(section));
    }

    @Test
    void cancel_enlisted_section(){
        // Given two students with an enlisted section
        Student student1 = newStudent();
        Student student2 = new Student(2, Collections.emptyList());

        // room capacity exceeds
        Room room = new Room("Room1", 1);

        // and two sections with the same schedule
        Section section = new Section("A", MTH_H0830, room);

        // student1 enlist in section A,
        student1.enlist(section);

        // check if the student1 successfully enlist on the section
        assertFalse(student1.getSections().isEmpty());

        // student1 cancel enlistment
        student1.cancelEnlist(section);

        // check again if the student1 successfully cancelled the enlistment
        assertTrue(student1.getSections().isEmpty());

        // student2 enlist in section A,
        student2.enlist(section);

        // check if the student2 successfully enlist on the section
        assertFalse(student2.getSections().isEmpty());

        // check if student1 is currently enrolled in that section
        var section_student1 = student1.getSections();
        assertEquals(0, section_student1.size());

        // check if student2 is currently enrolled in that section
        var section_student2 = student2.getSections();
        assertEquals(1, section_student2.size());

    }

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

}
