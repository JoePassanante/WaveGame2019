package game;

import util.LambdaException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class Performer { // anything you can see or hear
    private byte[] sight, sound; // TODO: touch, taste, and smell interfaces :^)
    private BufferedImage image;

    public void tick() { // update this performer
        // TODO: animation loops, sound loops, etc.
    }

    public void defer(Performer def) { // replace data with def if this is missing it
        if (sight == null) {
            sight = def.sight;
            image = def.image;
        }
        if (sound == null) {
            sound = def.sound;
        }
    }

    public void refer(Performer ref) { // replace data with ref if ref is not missing it
        if (ref.sight != null) {
            sight = ref.sight;
            image = ref.image;
        }
        if (ref.sound != null) {
            sound = ref.sound;
        }
    }
    // ugly data stream getters and setters :(
    public void setSight(ByteArrayOutputStream baos) {
        sight = baos.toByteArray();
        image = LambdaException.<ImageInputStream,BufferedImage,IOException>wrap(ImageIO::read).apply(getSight());
    }

    public void setSound(ByteArrayOutputStream baos) {
        sound = baos.toByteArray();
    }

    public ImageInputStream getSight() {
        return Optional
            .ofNullable(sight)
            .map(ByteArrayInputStream::new)
            .map(LambdaException.wrap(ImageIO::createImageInputStream))
            .orElse(null);
    }

    public AudioInputStream getSound() {
        return Optional
            .ofNullable(sound)
            .map(ByteArrayInputStream::new)
            .map(LambdaException.<InputStream,AudioInputStream,Throwable>wrap(AudioSystem::getAudioInputStream))
            .orElse(null);
    }
    // screen space that this occupies
    public Rectangle getBounds() {
        return new Rectangle(image.getWidth(), image.getHeight());
    }
    // render image
    public void render(Graphics g) {
        render(g, getBounds());
    }
    // render in r
    public void render(Graphics g, Rectangle r) {
        if(sight != null) {
            g.drawImage(image, r.x, r.y, r.width, r.height, null);
        }
        else {
            g.drawRect(r.x, r.y, r.width, r.height);
        }
    }
    // render sound
    public void render(Clip c, int i) {
        if(sound != null) {
            LambdaException.<AudioInputStream,Throwable>wrapc(c::open).accept(getSound());
            c.loop(i);
        }
    }
}
