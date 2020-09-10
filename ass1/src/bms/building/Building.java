package bms.building;

import bms.util.FireDrill;

import java.lang.IllegalArgumentException;

import bms.room.RoomType;
import bms.floor.Floor;
import bms.exceptions.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a building of floors, which in turn, contain rooms.
 * <p>
 * A building needs to manage and keep track of the floors that make up the
 * building.
 * <p>
 * A building can be evacuated, which causes all rooms on all floors within the
 * building to be evacuated.
 */
public class Building implements FireDrill {
    private String name;
    private ArrayList<Floor> floors;

    /**
     * Creates a new empty building with no rooms.
     *
     * @param name name of this building, eg. "General Purpose South"
     */
    public Building(String name) {
        this.name = name;
        this.floors = new ArrayList<Floor>();
    }

    /**
     * Returns the name of the building.
     *
     * @return name of this building
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a new list containing all the floors in this building.
     * <p>
     * Adding or removing floors from this list should not affect the building's
     * internal list of floors.
     *
     * @return new list containing all floors in the building
     */
    public List<Floor> getFloors() {
        return new ArrayList<Floor>(floors);
    }

    /**
     * Searches for the floor with the specified floor number.
     * <p>
     * Returns the corresponding Floor object, or null if the floor was not
     * found.
     *
     * @param floorNumber floor number of floor to search for
     * @return floor with the given number if found; null if not found
     */
    public Floor getFloorByNumber(int floorNumber) {
        for (int i = 0; i < floors.size(); i++) {
            if (floors.get(i).getFloorNumber() == floorNumber) {
                return floors.get(i);
            }
        }
        return null;
    }

    /**
     * Adds a floor to the building.
     * <p>
     * If the given arguments are invalid, the floor already exists, there is no
     * floor below, or the floor below does not have enough area to support this
     * floor, an exception should be thrown and no action should be taken.
     *
     * @param newFloor object representing the new floor
     * @throws IllegalArgumentException if floor number is <= 0, width
     *                                  < Floor.getMinWidth(), or length
     *                                  < Floor.getMinLength()
     * @throws DuplicateFloorException  if a floor at this level already exists
     *                                  in the building
     * @throws NoFloorBelowException    if this is at level 2 or above and there
     *                                  is no floor below to support this new
     *                                  floor
     * @throws FloorTooSmallException   if this is at level 2 or above and the
     *                                  floor below is not big enough to support
     *                                  this new floor
     */
    public void addFloor(Floor newFloor) throws IllegalArgumentException,
            DuplicateFloorException, NoFloorBelowException,
            FloorTooSmallException {
        if (newFloor.getFloorNumber() <= 0
                || newFloor.getWidth() < Floor.getMinWidth()
                || newFloor.getLength() < Floor.getMinLength()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < floors.size(); i++) {
            if (floors.get(i).getFloorNumber() == newFloor.getFloorNumber()) {
                throw new DuplicateFloorException();
            }
        }
        if (newFloor.getFloorNumber() > 1) {
            if (floors.size() != newFloor.getFloorNumber() - 1) {
                throw new NoFloorBelowException();
            } else if (newFloor.calculateArea() >
                    floors.get(newFloor.getFloorNumber() - 2).calculateArea()) {
                throw new FloorTooSmallException();
            }
        }
        floors.add(newFloor);
    }

    @Override
    public void fireDrill(RoomType roomType) throws FireDrillException {
        if (floors.size() == 0) {
            throw new FireDrillException();
        }

        // check each floors' situation and set fire drill
        for (int i = 0; i < floors.size(); i++) {
            if (floors.get(i).getRooms().size() == 0) {
                throw new FireDrillException();
            } else {
                floors.get(i).fireDrill(roomType);
            }
        }
    }

    /**
     * Cancels any ongoing fire drill in the building.
     * <p>
     * All rooms must have their fire alarm cancelled regardless of room type.
     */
    public void cancelFireDrill() {
        for (int i = 0; i < floors.size(); i++) {
            floors.get(i).cancelFireDrill();
        }
    }

    /**
     * Returns the human-readable string representation of this building.
     *
     * @return string representation of this building
     */
    @Override
    public String toString() {
        return String.format("Building: name=\"%s\", floors=%d", name,
                floors.size());
    }

}
