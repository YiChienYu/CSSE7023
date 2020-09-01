package bms.floor;
import bms.exceptions.*;
import bms.room.*;
import bms.util.FireDrill;
import java.util.ArrayList;
import java.util.List;

public class Floor implements FireDrill{
    private int floorNumber;
    private double width;
    private double length;
    private double area = width*length;
    private ArrayList<Room> rooms;

    public Floor(int floorNumber, double width, double length){
        this.floorNumber = floorNumber;
        this.width = width;
        this.length = length;
        this.rooms = new ArrayList<Room>();
    }

    public int getFloorNumber(){
        return floorNumber;
    }

    public static int getMinWidth(){
        return 5;
    }

    public static int getMinLength(){
        return 5;
    }

    public List<Room> getRooms(){

        return (ArrayList<Room>) rooms.clone();
    }

    public double getWidth(){
        return width;
    }

    public double getLength(){
        return length;
    }

    public Room getRoomByNumber(int roomNumber){
        for(int i = 0; i<rooms.size();i++){
            if(roomNumber == rooms.get(i).getRoomNumber()){
                return rooms.get(i);
            }
        }return null;
    }

    public double calculateArea(){
        return width*length;
    }

    public float occupiedArea(){
        float totalArea = 0;
        for(int i = 0; i<rooms.size();i++){
                totalArea+= (float) rooms.get(i).getArea();
        }return totalArea;
    }

    public void addRoom(Room newRoom) throws DuplicateRoomException, InsufficientSpaceException{
        if(newRoom.getArea() < Room.getMinArea()){
            throw new IllegalArgumentException();
        }
        for(int i = 0; i<rooms.size();i++){
            if(rooms.get(i).getRoomNumber() == newRoom.getRoomNumber()){
                throw new DuplicateRoomException();
            }
        }

        if(newRoom.getArea() - this.area < 0){
            throw new InsufficientSpaceException();
        }

        rooms.add(newRoom);
        area -= newRoom.getArea();
    }

    @Override
    public void fireDrill(RoomType roomType) throws FireDrillException {
        for(int i = 0; i<rooms.size();i++) {
            if (roomType == null) {
                rooms.get(i).setFireDrill(true);
            } else if (roomType.equals(rooms.get(i).getClass().getSimpleName())) {
                rooms.get(i).setFireDrill(true);
            }
        }
    }

    public void cancelFireDrill(){
        for(int i = 0; i < rooms.size(); i++){
            rooms.get(i).setFireDrill(false);
        }
    }

    @Override
    public String toString(){
        return String.format("Floor #%d: width=%.2fm, length=%.2fm, rooms=%d", floorNumber, width, length, rooms.size());
    }
}
