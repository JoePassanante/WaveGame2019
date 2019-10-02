package mainGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

/**
 * Just a list of themes in the game.
 *
 * @author Joey Germain 11/23/18
 */

public class Theme extends EnumMap<ID, Image> {
    private String folder;

    public Theme(String f) {
        super(ID.class);
        folder = f;
    }

    public void put(ID id, String s) {
        try {
            put(id, ImageIO.read(new File("./" + folder + "/" + s)));
        } catch (IOException ioe) {
            System.out.println("Failed to read " + s);
        }
    }

    public void initialize() {
        for(ID id: ID.values()) {
            try {
                put( id, ImageIO.read(new File("./" + folder + "/" + id.name())));
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }
}
