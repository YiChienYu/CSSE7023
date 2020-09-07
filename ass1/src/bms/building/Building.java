package bms.building;

import bms.util.FireDrill;
import bms.room.RoomType;
import bms.floor.Floor;
import bms.exceptions.*;
import java.util.List;
import java.util.ArrayList;

public class Building implements FireDrill {

    private String name;
    private ArrayList<Floor> floors;

    public Building(String name){
        this.name = name;
        this.floors = new ArrayList<Floor>();
    }

    public String getName(){
        return name;
    }

    public List<Floor> getFloors(){
        return ((ArrayList<Floor>) floors.clone());
    }

    public Floor getFloorByNumber(int floorNumber){
        for(int i=0; i<floors.size(); i++){
            if(floors.get(i).getFloorNumber() == floorNumber){
                return floors.get(i);
            }
        }return null;
    }

    public void addFloor(Floor newFloor) throws IllegalArgumentException,
            DuplicateFloorException, NoFloorBelowException,
            FloorTooSmallException{
        if(newFloor.getFloorNumber() <= 0
                || newFloor.getWidth() < Floor.getMinWidth()
                || newFloor.getLength() < Floor.getMinLength()){
            throw new IllegalArgumentException();
        }
        for(int i=0; i<floors.size(); i++){
            if(floors.get(i).getFloorNumber() == newFloor.getFloorNumber()){
                throw new DuplicateFloorException();
            }
        }
        if(newFloor.getFloorNumber() > 1){
            if(floors.size()!=newFloor.getFloorNumber()-1){
                throw new NoFloorBelowException();
            }
            else if(newFloor.calculateArea() >
                    floors.get(newFloor.getFloorNumber() -2).calculateArea()){
                throw new FloorTooSmallException();
            }
        }
        floors.add(newFloor);
    }

    @Override
    public void fireDrill(RoomType roomType) throws FireDrillException{
        if(floors.size() == 0){
            throw new FireDrillException();
        }
        for(int i=0; i < floors.size(); i++){
            if(floors.get(i).getRooms().size() == 0){
                throw new FireDrillException();
            }
            else{
                floors.get(i).fireDrill(roomType);
            }
        }
    }

    public void cancelFireDrill(){
        for(int i=0; i<floors.size(); i++){
            floors.get(i).cancelFireDrill();
        }
    }

    @Override
    public String toString(){
        return String.format( "Building: name=\"%s\", floors=%d", name,
                floors.size());
    }

}
