package ph.edu.dlsu.enlistment;

import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isAlphanumeric;

class Room {
    private final String roomName;
    private final int capacity;
    private final Collection<Section> assignedSections = new HashSet<>();

    Room (String roomName, int capacity){
        Objects.requireNonNull(roomName);
        Validate.isTrue(isAlphanumeric(roomName), "roomName must be alphanumeric, was: "
                + roomName);
        Validate.isTrue(capacity > 0, "capacity must be greater than zero, was: "
                + capacity);
        this.roomName = roomName;
        this.capacity = capacity;
    }

    void isVacant(int currEnlisted){
        if(currEnlisted >= capacity){
            throw new RoomCapacityExceededException("Room has max capacity of " + capacity +
                    " students, but received " + currEnlisted + " people");
        }
    }

    void assignSection(Section newSection) {
        Objects.requireNonNull(newSection, "Section cannot be null");
        // Check for schedule conflicts with already assigned sections
        for (Section existingSection : assignedSections) {
            if (existingSection.schedule.equals(newSection.schedule)) {
                throw new IllegalStateException("Cannot assign section " + newSection + " to room " + roomName +
                        " as the schedule conflicts with section " + existingSection);
            }
        }
        // If no conflicts, add the section
        assignedSections.add(newSection);
    }

    @Override
    public String toString(){
        return roomName;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;

        return Objects.equals(roomName, room.roomName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roomName);
    }
}


