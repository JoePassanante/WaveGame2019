package game.menu;

public class GameMode extends Menu {
    public GameMode(Menu m) {
        super(m);
    }

        /*
        Font f;

        f = new Font("Amoebic", Font.BOLD, 130);
        // Waves One button
        buttons.add(new MenuButton.TextButton(602, 300, this, p -> {
            getEntities().clear();
            setMaxTick(600);
            setClipped(true);
            getState().push(new GameOver(this));
            getState().push(new Waves(this));
            setMaxTick(60);
            getState().push(Transition.Modulo.droplets(this).apply(this, getState().peek()));
        }, "One", f));
        // Waves Two button
        buttons.add(new MenuButton.TextButton(1052, 300, this, p -> {
            getEntities().clear();
            setMaxTick(600);
            setClipped(true);
            getState().push(new GameOver(this));
            getState().push(new Waves(this));
        }, "Two", f));
         */

    /*
    // Player Customization button
    buttons.add(new MenuButton.TextButton(850, 900, this, p -> {
        setMaxTick(60);
        getState().push(new Avatar(this));
        getState().push(Transition.Slide.horizontal(this).apply(this, getState().peek()));
    }, "Player customization", f));
     */
    @Override
    public void start() {
        super.start();


    }
}
