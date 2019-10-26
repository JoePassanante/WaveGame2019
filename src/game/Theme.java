package game;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Class to retrieve images and clips from the themes directory. This class is cursed by the lack of friendly data
 * types in the JDK, the even worse javax audio system, and the limitations of generics, but it still manages to
 * provide a way to generate audio and visual themes without extra configuration.
 *
 * @author Joey Germain 11/23/18
 * @author Aaron Paterson 9/23/18
 */

public class Theme extends HashMap<String, Performer> implements Runnable {
    private Theme fallback;
    private String folder;

    public Theme(String fold, Theme fall) {
        folder = fold;
        fallback = fall;
    }

    @Override
    public void run() { // every time you look at this method it gets uglier. checked exceptions were a mistake.
        try(    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Stream<File> files = Files
                        .walk(Paths.get("src/themes/" + folder))
                        .filter(Files::isRegularFile)
                        .map(Path::toFile)
        ) {
            for(File f: files.toArray(File[]::new)) { // avoid handling checked exceptions twice ;-;
                String name = f.getName();
                int dot = name.indexOf(".");
                String before = name.substring(0, dot);
                String after = name.substring(dot);
                putIfAbsent(before, new Performer());
                BufferedImage bi = ImageIO.read(f);
                if(bi != null) {
                    ImageIO.write(bi, after.substring(1), baos);
                    get(before).setSight(baos);
                }
                else {
                    AudioSystem.write(AudioSystem.getAudioInputStream(f), AudioSystem.getAudioFileFormat(f).getType(), baos);
                    get(before).setSound(baos);
                }
                baos.reset();
            }
        } catch (IOException | IllegalArgumentException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public Performer get(Performer p) {
        return get(p.getClass());
    }

    public Performer get(Class cls) {
        Performer result = Optional.ofNullable(get(cls.getSimpleName())).orElseGet(Performer::new);
        Optional.ofNullable(cls.getSuperclass()).map(this::get).ifPresent(result::defer);
        return result;
    }

    public Performer get(String str) {
        Performer result = Optional.ofNullable(super.get(str)).orElseGet(Performer::new);
        Optional.ofNullable(fallback).map(f -> f.get(str)).ifPresent(result::defer);
        return result;
    }
}
