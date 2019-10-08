package game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Just a list of themes in the game.
 *
 * @author Joey Germain 11/23/18
 */

public class Theme extends HashMap<String, Image> {
    private String folder;
    private Theme fallback;

    public static String fileNameWithoutExtension(String name) {
        return name.substring(0,name.lastIndexOf("."));
    }

    public Theme(String fold, Theme fall) {
        folder = fold;
        fallback = fall;
    }

    public void initialize() {
        try(Stream<Path> paths = Files.walk(Paths.get("src/themes/" + folder))) {
            paths.filter(Files::isRegularFile).forEach( p ->
                put(fileNameWithoutExtension(p.getFileName().toString()), read(p))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Image get(Animatable a) {
        return get(a.getClass().getSimpleName());
    }

    @Override
    public Image get(Object s) {
        Image img = super.get(s);
        if(img == null && fallback != null) {
            img = fallback.get(s);
        }
        return img;
    }

    public Image read(Path p)  {
        try {
            return ImageIO.read(p.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
