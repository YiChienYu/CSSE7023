package bms.util;

import java.util.ArrayList;

/**
 * Singleton class which manages all the timed items.
 * All classes that implement TimedItem must be registered with this manager,
 * which will allow their elapseOneMinute() method to be called at regular time
 * intervals.
 * Once a class is registered with the timed item manager by calling
 * registerTimedItem(TimedItem) ()} and passing itself, the manager will ensure
 * that its elapseOneMinute() method is called at regular intervals.
 */
public class TimedItemManager implements TimedItem{

    private static TimedItemManager singleton = null;
    private ArrayList<TimedItem> itemManager = new ArrayList<TimedItem>();

    private TimedItemManager(){ }

    /**
     * Returns the singleton instance of the timed item manager.
     *
     * @return singleton instance
     */
    public static TimedItemManager getInstance(){
        if(singleton == null){
            singleton = new TimedItemManager();
        }return singleton;
    }

    /**
     * Registers a timed item with the manager.
     *After calling this method, the manager will call the given timed item's
     * elapseOneMinute() method at regular intervals.
     *
     * @param timedItem a timed item to register with the manager
     */
    public void registerTimedItem(TimedItem timedItem){
        itemManager.add(timedItem);
    }

    @Override
    public void elapseOneMinute(){
        for(int i=0;i<itemManager.size();i++){
            itemManager.get(i).elapseOneMinute();
        }
    }
}
