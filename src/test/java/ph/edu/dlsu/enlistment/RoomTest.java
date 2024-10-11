package ph.edu.dlsu.enlistment;

import java.util.*;
import org.junit.jupiter.api.Test;
import static ph.edu.dlsu.enlistment.Days.*;
import static org.junit.jupiter.api.Assertions.*;
import static ph.edu.dlsu.enlistment.Programs.*;

class RoomTest {

    @Test
    void testNoConflictBetweenDifferentTimeFrames() {
        // Given: A room and two sections with different schedules
        Room room = new Room("G201", 40);
        Subject subject1 = new Subject("CS301", 3, false, Collections.emptyList(), BSCS);

        Schedule schedule1 = new Schedule(MTH, "10:00AM", "11:00AM");
        Schedule schedule2 = new Schedule(MTH, "11:00AM", "12:00PM");

        Section section1 = new Section("S1", schedule1, room, subject1);
        Section section2 = new Section("S2", schedule2, room, subject1);

        // When: Assigning both sections to the room
        room.assignSection(section1);

        // Then: No conflict should occur
        assertDoesNotThrow(() -> room.assignSection(section1));
        assertDoesNotThrow(() -> room.assignSection(section2));
    }

    @Test
    void testConflictBetweenOverlappingSections() {
        // Given: A room and two sections with overlapping schedules
        Room room = new Room("G201", 40);
        Subject subject1 = new Subject("CS301", 3, false, Collections.emptyList(), BSCS);

        Schedule schedule1 = new Schedule(MTH, "10:00AM", "11:00AM");
        Schedule schedule3 = new Schedule(MTH, "10:30AM", "11:30AM");

        Section section1 = new Section("S1", schedule1, room, subject1);
        Section section3 = new Section("S3", schedule3, room, subject1);

        // When: Assigning both sections to the room
        room.assignSection(section1);

        // Then: A conflict should occur and throw ScheduleConflictException
        Exception exception = assertThrows(ScheduleConflictException.class, () -> room.assignSection(section3));
        assertEquals("Schedule conflict between sections!", exception.getMessage());
    }

    @Test
    void testRoomCapacityExceeded() {
        // Given: A room with a capacity of 40 students
        Room room = new Room("G201", 40);

        // When: Checking if the room exceeds capacity
        room.isVacant(40); // Should not throw an exception

        // Then: Exceeding the capacity should throw RoomCapacityExceededException
        Exception exception = assertThrows(RoomCapacityExceededException.class, () -> room.isVacant(41));
        assertEquals("Room has max capacity of 40 students, but received 41", exception.getMessage());
    }
}