package bms.room;

import java.util.ArrayList;
import bms.sensors.Sensor;
import bms.exceptions.*;
import java.util.List;

public class Room{
    private int roomNumber;
    private RoomType type;
    private double area;
    private boolean fireDrillState = false;
    private ArrayList<Sensor> sensors;

    public Room(int roomNumber, RoomType type, double area){
        this.roomNumber = roomNumber;
        this.type = type;
        this.area = area;
        this.sensors = new ArrayList<Sensor>();
    }

    public int getRoomNumber(){
        return roomNumber;
    }

    public double getArea(){
        return area;
    }

    public static int getMinArea(){
        return 5;
    }

    public RoomType getType(){
        return type;
    }

    public boolean fireDrillOngoing(){
        return fireDrillState;
    }

    public List<Sensor> getSensors(){

        return (ArrayList<Sensor>) sensors.clone();
    }

    public void setFireDrill(boolean fireDrill){
        fireDrillState = fireDrill;
    }

    public Sensor getSensor(String sensorType){
        for(int i=0;i< sensors.size();i++){
            if(sensors.get(i).getClass().getSimpleName() == sensorType){
                return sensors.get(i);
            }
        }return null;
    }

    public void addSensor(Sensor sensor) throws DuplicateSensorException{
        if(this.getSensor(sensor.getClass().getSimpleName()) != null){
            throw new DuplicateSensorException();
        }else{
            if(sensors.size() == 0){
                sensors.add(sensor);
            }else{
                for(int i=0; i< sensors.size();i++){
                    if(sensor.getClass().getSimpleName().compareTo
                            (sensors.get(i).getClass().getCanonicalName()) < 0){
                        sensors.add(i, sensor);
                        break;
                    }else{
                        if(i==sensors.size()-1){
                            sensors.add(sensor);
                            break;
                        }else{
                            continue;
                            }
                        }
                    }
                }
            }
        }

    @Override
    public String toString(){
        return String.format("Room #%d: type=%s, area=%.2fm^2, sensors=%d",
                roomNumber, type.toString(), area, sensors.size());
    }
}
