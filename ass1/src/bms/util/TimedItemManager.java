package bms.util;
import java.util.ArrayList;

public class TimedItemManager implements TimedItem{

    private static TimedItemManager singleton = null;
    private ArrayList<TimedItem> itemManager = new ArrayList<TimedItem>();

    private TimedItemManager(){ }

    public void registerTimedItem(TimedItem timedItem){
        itemManager.add(timedItem);
        timedItem.elapseOneMinute();
    }


    public static TimedItemManager getInstance(){
        if(singleton == null){
            singleton = new TimedItemManager();
        }return singleton;
    }

    @Override
    public void elapseOneMinute(){
        for(int i=0;i<itemManager.size();i++){
            itemManager.get(i).elapseOneMinute();
        }
    }
}
