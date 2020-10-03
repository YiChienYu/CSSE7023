package bms.building;

import bms.exceptions.FileFormatException;

import java.io.IOException;
import java.util.List;

/**
 * Class which manages the initialisation and saving of buildings by reading and
 * writing data to a file.
 */
public class BuildingInitialiser {
    /**
     * Loads a list of buildings from a save file with the given filename.
     *
     * @param filename path of the file from which to load a list of buildings
     * @return a list containing all the buildings loaded from the file
     * @throws IOException if an IOException is encountered when calling any
     * IO methods
     * @throws FileFormatException if the file format of the given file
     * is invalid according to the rules above
     */
    public static List<Building> loadBuildings(String filename) throws
            IOException, FileFormatException {

    }
}
