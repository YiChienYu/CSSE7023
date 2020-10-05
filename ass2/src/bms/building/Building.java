package bms.building;

import bms.exceptions.DuplicateFloorException;
import bms.exceptions.FireDrillException;
import bms.exceptions.FloorTooSmallException;
import bms.exceptions.NoFloorBelowException;
import bms.floor.Floor;
import bms.room.RoomType;
import bms.util.Encodable;
import bms.util.FireDrill;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a building of floors, which in turn, contain rooms.
 * <p>
 * A building needs to manage and keep track of the floors that make up the
 * building.
 * <p>
 * A building can be evacuated, which causes all rooms on all floors within
 * the building to be evacuated.
 * @ass1
 */
public class Building implements FireDrill, Encodable {

    /**
     * The name of the building.
     */
    private String name;

    /**
     * List of floors tracked by the building.
     */
    private List<Floor> floors;

    /**
     * Creates a new empty building with no rooms.
     *
     * @param name name of this building, eg. "General Purpose South"
     * @ass1
     */
    public Building(String name) {
        this.name = name;
        this.floors = new ArrayList<>();
    }

    /**
     * Returns the name of the building.
     *
     * @return name of this building
     * @ass1
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a new list containing all the floors in this building.
     * <p>
     * Adding or removing floors from this list should not affect the
     * building's internal list of floors.
     *
     * @return new list containing all floors in the building
     * @ass1
     */
    public List<Floor> getFloors() {
        return new ArrayList<>(this.floors);
    }

    /**
     * Searches for the floor with the specified floor number.
     * <p>
     * Returns the corresponding Floor object, or null if the floor was not
     * found.
     *
     * @param floorNumber floor number of floor to search for
     * @return floor with the given number if found; null if not found
     * @ass1
     */
    public Floor getFloorByNumber(int floorNumber) {
        for (Floor floor : this.floors) {
            if (floor.getFloorNumber() == floorNumber) {
                return floor;
            }
        }
        return null;
    }

    /**
     * Adds a floor to the building.
     * <p>
     * If the given arguments are invalid, the floor already exists,
     * there is no floor below, or the floor below does not have enough area
     * to support this floor, an exception should be thrown and no action
     * should be taken.
     *
     * @param newFloor object representing the new floor
     * @throws IllegalArgumentException if floor number is &lt;= 0,
     * width &lt; Floor.getMinWidth(), or length &lt; Floor.getMinLength()
     * @throws DuplicateFloorException if a floor at this level already exists
     * in the building
     * @throws NoFloorBelowException if this is at level 2 or above and there
     * is no floor below to support this new floor
     * @throws FloorTooSmallException if this is at level 2 or above and
     * the floor below is not big enough to support this new floor
     *
     * @ass1
     */
    public void addFloor(Floor newFloor) throws
            IllegalArgumentException, DuplicateFloorException,
            NoFloorBelowException, FloorTooSmallException {

        int newFloorNumber = newFloor.getFloorNumber();
        if (newFloorNumber < 1) {
            throw new IllegalArgumentException(
                    "Floor number must be 1 or higher.");
        } else if (newFloor.getWidth() < Floor.getMinWidth())  {
            throw new IllegalArgumentException(
                    "Width cannot be less than " + Floor.getMinWidth());
        } else if (newFloor.getLength() < Floor.getMinLength()) {
            throw new IllegalArgumentException(
                    "Length cannot be less than " + Floor.getMinLength());
        }
        if (this.getFloorByNumber(newFloorNumber) != null) {
            throw new DuplicateFloorException(
                    "This floor level already exists in the building.");
        }

        Floor floorBelow = this.getFloorByNumber(newFloorNumber - 1);
        if (newFloorNumber >= 2 && floorBelow == null) {
            throw new NoFloorBelowException("There is no floor below to "
                    + "support this new floor.");
        }
        if (newFloorNumber >= 2 && (newFloor.getWidth() > floorBelow.getWidth()
                || newFloor.getLength() > floorBelow.getLength())) {
            throw new FloorTooSmallException("The floor below does not "
                    + "have enough area to support this floor. ");
        }

        // No problems, so add floor to the list of floors
        floors.add(newFloor);
    }

    /**
     * Start a fire drill in all rooms of the given type in the building.
     * Only rooms of the given type must start a fire alarm.
     * Rooms other than the given type must not start a fire alarm.
     * * <p>
     * If the room type given is null, then <b>all</b> rooms in the building
     * must start a fire drill.
     * <p>
     * If there are no rooms (of any type) in the building, a
     * FireDrillException must be thrown. Note that floors may be in the
     * building, but the floors may not contain rooms yet.
     *
     * @param roomType the type of room to carry out fire drills on; null if
     *                 fire drills are to be carried out in all rooms
     * @throws FireDrillException if there are no floors in the building, or
     * there are floors but no rooms in the building
     * @ass1
     */
    public void fireDrill(RoomType roomType) throws FireDrillException {
        if (this.floors.size() < 1) {
            throw new FireDrillException("Cannot conduct fire drill because "
                    + "there are no floors in the building yet!");
        }
        boolean hasRooms = false;
        for (Floor floor : this.floors) {
            if (!floor.getRooms().isEmpty()) {
                hasRooms = true;
            }
        }
        if (!hasRooms) {
            throw new FireDrillException("Cannot conduct fire drill because "
                    + "there are no rooms in the building yet!");
        } else {
            for (Floor floor : this.floors) {
                floor.fireDrill(roomType);
            }
        }
    }

    /**
     * Cancels any ongoing fire drill in the building.
     * <p>
     * All rooms must have their fire alarm cancelled regardless of room type.
     *
     * @ass1
     */
    public void cancelFireDrill() {
        for (Floor floor : this.floors) {
            floor.cancelFireDrill();
        }
    }

    /**
     * Returns the human-readable string representation of this building.
     * <p>
     * The format of the string to return is
     * "Building: name="'buildingName'", floors='numFloors'"
     * without the single quotes, where 'buildingName' is the building's name,
     * and 'numFloors' is the number of floors in the building.
     * <p>
     * For example:
     * "Building: name="GP South", floors=7"
     *
     * @return string representation of this building
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("Building: name=\"%s\", floors=%d",
                this.name, this.floors.size());
    }

    /**
     * Renovate the given floor by changing the width and length.
     * <p>
     * If the floor is increasing in size, all floors below need to have
     * sufficient dimensions to support the changes to this floor.
     * <p>
     * If the floor is decreasing in size, all floors above need to have
     * dimensions small enough so that this reduced floor can
     * still support them.
     * <p>
     * If the floor is decreasing in size, it is essential that it will remain
     * large enough to be able to accomodate all the rooms on the floor
     * (with their respective areas).
     *
     * @param floorNumber the floor which is to be renovated
     * @param newWidth the new width dimension for the floor
     * @param newLength the new length dimension for the floor
     * @throws IllegalArgumentException if the given floor does not exist
     * (i.e. getFloorByNumber() is null), or if newWidth < Floor.getMinWidth(),
     * or newLength < Floor.getMinLength()
     * @throws FloorTooSmallException if the floor below is too small to support
     * increased dimensions, if the floor above is too large to be supported
     * by decreased dimensions, or if the total size of the current rooms
     * could not be supported by decreased dimensions
     */
    public void renovateFloor(int floorNumber, double newWidth,
                              double newLength) throws IllegalArgumentException,
                              FloorTooSmallException {
        if (this.getFloorByNumber(floorNumber) == null ||
                newWidth < Floor.getMinWidth() ||
                newLength < Floor.getMinLength()) {
            throw new IllegalArgumentException();
        }

        Floor floor = this.getFloorByNumber(floorNumber);
        int indexOfFloor = 0;

        for (int i = 0; i < floors.size(); i++) {
            if (floors.get(i).getFloorNumber() == floorNumber) {
                indexOfFloor = i;
            }
        }

        if ((newWidth * newLength) > floor.calculateArea()) {
            for (int i = 0; i < indexOfFloor; i++){
                if (floors.get(i).calculateArea() < (newWidth * newLength)) {
                    throw new FloorTooSmallException();
                }
            }
        } else if ((newWidth * newLength) < floor.calculateArea()) {
            if ((newWidth * newLength) < floor.occupiedArea()) {
                throw new FloorTooSmallException();
            }
            for (int i = indexOfFloor + 1; i < floors.size(); i++) {
                if (floors.get(i).calculateArea() > (newWidth * newLength)) {
                    throw new FloorTooSmallException();
                }
            }
        }
        floor.changeDimensions(newWidth, newLength);
    }

    /**
     * Returns true if and only if this building is equal to
     * the other given building.
     *
     * @param obj other object to compare equality
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Building) {
            Building building = (Building) obj;

            if (this.name != building.name ||
                    this.floors.size() != building.floors.size()) {
                return false;
            }

            for (int i = 0; i < floors.size(); i++) {
                if (building.getFloorByNumber(floors.get(i).getFloorNumber()) ==
                        null) {
                    return false;
                }

                Floor floor = building.getFloorByNumber(floors.get(i).
                        getFloorNumber());

                if (!floor.equals(floors.get(i))) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Returns the hash code of this building.
     *
     * @return hash code of this building
     */
    @Override
    public int hashCode() {
        int floorHashCode = 0;

        for (Floor f : floors) {
            floorHashCode += f.hashCode();
        }

        return name.hashCode() + floors.size() + floorHashCode;
    }

    /**
     * Returns the machine-readable string representation of this building and
     * all of its floors, rooms and sensors.
     *
     * @return encoded string representation of this building
     */
    @Override
    public String encode() {
        String begin = String.format("%s", name) + System.lineSeparator() +
                String.format("%s", floors.size());
        String encodedFloors = "";

        for (int i = 0; i < floors.size(); i++) {
            encodedFloors += floors.get(i).encode();

            if (i != floors.size() - 1) {
                encodedFloors += System.lineSeparator();
            }
        }

        return begin + System.lineSeparator() + encodedFloors;
    }
}
