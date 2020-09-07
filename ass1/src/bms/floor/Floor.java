package bms.floor;

import bms.exceptions.*;
import bms.room.*;
import bms.util.FireDrill;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a floor of a building.
 * <p>
 * All floors have a floor number (ground floor is floor 1), a list of rooms,
 * and a width and length.
 * <p>
 * A floor can be evacuated, which causes all rooms on the floor to be
 * evacuated.
 */
public class Floor implements FireDrill {
    private int floorNumber;
    private double width;
    private double length;
    private double area = width * length;
    private ArrayList<Room> rooms;

    /**
     * Creates a new floor with the given floor number.
     *
     * @param floorNumber a unique floor number, corresponds to how many floors
     *                    above ground floor (inclusive)
     * @param width the width of the floor in metres
     * @param length the length of the floor in metres
     */
    public Floor(int floorNumber, double width, double length){
        this.floorNumber = floorNumber;
        this.width = width;
        this.length = length;
        this.rooms = new ArrayList<Room>();
    }

    /**
     * Returns the floor number of this floor.
     *
     * @return floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Returns the minimum width for all floors.
     *
     * @return 5
     */
    public static int getMinWidth() {
        return 5;
    }

    /**
     * Returns the minimum length for all floors.
     * @return 5
     */
    public static int getMinLength() {
        return 5;
    }

    /**
     * Returns a new list containing all the rooms on this floor.
     * <p>
     * Adding or removing rooms from this list should not affect the floor's
     * internal list of rooms.
     *
     * @return new list containing all rooms on the floor
     */
    public List<Room> getRooms() {
        return (ArrayList<Room>) rooms.clone();
    }

    /**
     * Returns width of the floor.
     *
     * @return floor width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns length of the floor.
     *
     * @return floor length
     */
    public double getLength() {
        return length;
    }

    /**
     * Search for the room with the specified room number.
     * <p>
     * Returns the corresponding Room object, or null if the room was not found.
     *
     * @param roomNumber room number of room to search for
     * @return room with the given number if found; null if not found
     */
    public Room getRoomByNumber(int roomNumber) {
        for (int i = 0; i < rooms.size(); i++) {
            if (roomNumber == rooms.get(i).getRoomNumber()) {
                return rooms.get(i);
            }
        }
        return null;
    }

    /**
     * Calculates the area of the floor in square metres.
     * <p>
     * The area should be calculated as getWidth() multiplied by getLength().
     * <p>
     * For example, a floor with a length of 20.5 and width of 35.2, would be
     * 721.6 square metres.
     *
     * @return area of the floor in square metres
     */
    public double calculateArea() {
        return this.getWidth()*this.getLength();
    }

    /**
     * Calculates the area of the floor which is currently occupied by all the
     * rooms on the floor.
     *
     * @return area of the floor that is currently occupied, in square metres
     */
    public float occupiedArea() {
        float totalArea = 0;
        for (int i = 0; i < rooms.size(); i++) {
                totalArea += (float) rooms.get(i).getArea();
        }
        return totalArea;
    }

    /**
     * Adds a room to the floor.
     * <p>
     * The dimensions of the room are managed automatically. The length and
     * width of the room do not need to be specified, only the required space.
     * <p>
     *
     * @param newRoom object representing the new room
     * @throws DuplicateRoomException if the room number on this floor is
     * already taken
     * @throws InsufficientSpaceException if there is insufficient space
     * available on the floor to be able to add the room
     */
    public void addRoom(Room newRoom) throws DuplicateRoomException,
            InsufficientSpaceException {
        if (newRoom.getArea() < Room.getMinArea()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber() == newRoom.getRoomNumber()) {
                throw new DuplicateRoomException();
            }
        }
        if (newRoom.getArea() - this.area < 0) {
            throw new InsufficientSpaceException();
        }
        rooms.add(newRoom);
        area -= newRoom.getArea();
    }

    @Override
    public void fireDrill(RoomType roomType) {
        for (int i = 0; i < rooms.size(); i++) {
            if (roomType == null) {
                rooms.get(i).setFireDrill(true);
            } else if (roomType == rooms.get(i).getType()) {
                rooms.get(i).setFireDrill(true);
            }
        }
    }

    /**
     * Cancels any ongoing fire drill in rooms on the floor.
     * <p>
     * All rooms must have their fire alarm cancelled regardless of room type.
     */
    public void cancelFireDrill(){
        for (int i = 0; i < rooms.size(); i++) {
            rooms.get(i).setFireDrill(false);
        }
    }

    /**
     * Returns the human-readable string representation of this floor.
     *
     * @return string representation of this floor
     */
    @Override
    public String toString() {
        return String.format("Floor #%d: width=%.2fm, length=%.2fm, rooms=%d",
                floorNumber, width, length, rooms.size());
    }
}
