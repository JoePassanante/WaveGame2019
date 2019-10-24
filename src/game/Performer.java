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

public class Performer {
    private byte[] sight, sound; // TODO: touch, taste, and smell interfaces :^)
    private BufferedImage image;

    private void frame() {
        image = LambdaException.<ImageInputStream,BufferedImage,IOException>wrap(ImageIO::read).apply(getSight());
    }

    public void defer(Performer def) {
        if(def != null) {
            if (sight == null) {
                sight = def.sight;
                image = def.image;
            }
            if(image == null) {
                frame();
            }
            if (sound == null) {
                sound = def.sound;
            }
        }
    }

    public void refer(Performer ref) {
        if(ref != null) {
            if (ref.sight != null) {
                sight = ref.sight;
                image = ref.image;
            }
            if(image == null) {
                frame();
            }
            if (ref.sound != null) {
                sound = ref.sound;
            }
        }
    }

    public void setSight(ByteArrayOutputStream baos) {
        sight = baos.toByteArray();
        frame();
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

    public Rectangle getBounds() {
        return new Rectangle();
    }

    public void render(Graphics g) {
        render(g, getBounds());
    }

    public void render(Graphics g, Rectangle r) {
        if(sight != null) {
            g.drawImage(image, r.x, r.y, r.width, r.height, null);
        }
        else {
            g.fillRect(r.x, r.y, r.width, r.height);
        }
    }

    public void render(Clip c, int i) {
        if(sound != null) {
            LambdaException.<AudioInputStream,Throwable>wrapc(c::open).accept(getSound());
            c.loop(i);
        }
    }
}
