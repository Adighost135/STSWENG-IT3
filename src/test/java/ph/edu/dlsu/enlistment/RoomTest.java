package ph.edu.dlsu.enlistment;

import java.util.*;
import org.junit.jupiter.api.Test;
import static ph.edu.dlsu.enlistment.Days.*;
import static org.junit.jupiter.api.Assertions.*;
import static ph.edu.dlsu.enlistment.Programs.*;

class RoomTest {

    @Test
    void change_section_room(){
        // Given a subject with subject id, has 3 units, no laboratory, no prerequisites and degree program is BSCS
        Subject subject1 = new Subject("CS302", 3, false, Collections.emptyList(), BSCS);

        // Given two rooms with different room name and the same capacity
        Room room1 = new Room("Room1", 1);
        Room room2 = new Room("Room2", 1);

        // Given a schedule that follows the valid periods
        Schedule MTH_1000_1130 = new Schedule(MTH, "1000", "1130");

        // Given a section with a valid schedule, room, and subject with no prerequisite
        Section section1 = new Section("A", MTH_1000_1130, room1, subject1);

        // When a section changes room
        section1.changeRoom(room2);

    }

    @Test
    void schedule_conflict_of_section_in_a_room(){
        // Given two subjects with different subject id, has 3 units, no laboratory, no prerequisites and degree program is BSCS
        Subject subject1 = new Subject("CS301", 3, false, Collections.emptyList(), BSCS);
        Subject subject2 = new Subject("CS302", 3, false, Collections.emptyList(), BSCS);

        // Given a room that is normal capacity
        Room room1 = new Room("Room1", 1);

        // Given a schedule that follows the valid periods
        Schedule MTH_0900_1200 = new Schedule(MTH, "0900", "1200");

        // Given a section with a valid schedule, room, and subject with no prerequisite
        Section section1 = new Section("A", MTH_0900_1200, room1, subject1);

        // When a new section was created with the same schedule and room as section1
        // Then an exception is thrown at the newly created section due to schedule conflict with section1
        assertThrows(IllegalStateException.class, () -> {Section section2 = new Section("B", MTH_0900_1200, room1, subject2);});
    }

    @Test
    void change_section_room_with_conflict() {
        // Given a subject with subject id, has 3 units, no laboratory, no prerequisites and degree program is BSCS
        Subject subject1 = new Subject("CS301", 3, false, Collections.emptyList(), BSCS);

        // Given two rooms with different room name and the same capacity
        Room room1 = new Room("Room1", 1);
        Room room2 = new Room("Room1", 1);

        // Given a schedule that follows the valid periods
        Schedule MTH_1000_1130 = new Schedule(MTH, "1000", "1130");

        // Given two section with a valid schedule, room, and subject with no prerequisite
        Section section1 = new Section("A", MTH_1000_1130, room1, subject1);
        Section section2 = new Section("B", MTH_1000_1130, room2, subject1);

        // When a section change the room but has conflicting schedule with another section
        // Then an exception is thrown at section2 that is attempting to change with a conflicting room
        assertThrows(IllegalStateException.class, () -> section2.changeRoom(room1));
    }

    @Test
    void testRoomCapacityExceeded() {
        // Given: A room with a capacity of 40 students
        Room room = new Room("G201", 40);

        // When: Checking if the room exceeds capacity
        room.isVacant(30); // Should not throw an exception

        // Then: Exceeding the capacity should throw RoomCapacityExceededException
        assertThrows(RoomCapacityExceededException.class, () -> room.isVacant(41));
        
    }
}