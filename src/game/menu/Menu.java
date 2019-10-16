package game.menu;

import game.*;
import game.Handler;

import java.awt.*;
import java.util.ArrayList;

/**
 * The main menu
 * 
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 9/11/19
 *
 */

public class Menu extends GameMode {
	private int timer;
	private ArrayList<Color> colorPick = new ArrayList<>();
	private int colorIndex;
	private Handler handler;
	public Handler getHandler(){
	    return handler;
    }

    private GameState games;
	private GameState help;
    public GameState getGames() {
        return games;
    }
    public GameState getHelp() {
        return help;
    }
	public Menu(Client c) {
	    handler = c.getHandler();
	    games = new Games(this, c);
	    help = new Help(this);
        setState(games);
		timer = 10;
		addColors();
	}

	//using the java color picker, which colors you will add to the scene
	public void addColors() {
		colorPick.add(Color.blue);
		colorPick.add(Color.white);
		colorPick.add(Color.green);
		colorPick.add(Color.red);
		colorPick.add(Color.cyan);
		colorPick.add(Color.magenta);
		colorPick.add(Color.yellow);
	}

	private boolean play = false;
	public void tick() {
	    super.tick();
	    if(!play) {
	        AudioUtil.playGameClip(true);
            AudioUtil.closeGameClip();
            AudioUtil.playMenuClip(true, false);
            play = true;
        }
		timer -= 1;
		if (timer <= 0) {
			colorIndex = handler.getRandom().nextInt(6);
            handler.add( new MenuFireworks(
                handler.getRandom().nextInt(handler.getGameDimension().width) - 25,
                1080, 100, 100, 0, -4,
                colorPick.get(colorIndex), handler, true
            ));
			timer = 300;
		}
        handler.tick();
	}

	public void render(Graphics g) {
	    Image img = getHandler().getTheme().get(this);
        if(img != null) {
            g.drawImage(img, 0, 0, (int) getHandler().getGameDimension().getWidth(), (int) getHandler().getGameDimension().getHeight(), null);
        }
        super.render(g);
        handler.render(g);
	}

    public void setMenuMusic(boolean a) {
        // Toggle menu theme between Space Jam and Africa
        // Restart menu music
        AudioUtil.closeMenuClip();
        AudioUtil.playMenuClip(true, a);
    }
}
