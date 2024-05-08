package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

public class MoonLanderGame extends Game {

    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    private Rocket rocket;
    private GameObject landscape;

    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private GameObject platform;

    private boolean isGameStopped;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
        showGrid(false);
    }


    private void createGame() {
        createGameObjects();
        drawScene();
        setTurnTimer(50);
        isUpPressed = false;
        isLeftPressed = false;
        isRightPressed = false;
        isGameStopped = false;
        score = 1000;
    }

    @Override
    public void onKeyPress(Key key) {
        if(Key.RIGHT == key) {
            isRightPressed = true;
            isLeftPressed = false;
        }
        if(Key.LEFT == key) {
            isLeftPressed = true;
            isRightPressed = false;
        }
        if(Key.UP == key) {
            isUpPressed = true;
        }
        if (Key.SPACE == key && isGameStopped == true) {
            createGame();
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if(Key.UP == key) {
            isUpPressed = false;
        }
        else if(Key.LEFT == key) {
            isLeftPressed = false;
        }
        else if(Key.RIGHT == key) {
            isRightPressed = false;
        }
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if(x > WIDTH - 1 || x < 0 || y < 0 || y > HEIGHT - 1) {
            return ;
        }
        super.setCellColor(x, y, color);
    }


    private void drawScene() {
        for(int i = 0;i < WIDTH;i++) {
            for(int j = 0;j < HEIGHT;j++) {
                setCellColor(i, j, Color.BLACK);
            }
        }
        rocket.draw(this);
        landscape.draw(this);
    }

    private void createGameObjects() {
        rocket = new Rocket(WIDTH / 2.0, 0);
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(23, MoonLanderGame.HEIGHT-1, ShapeMatrix.PLATFORM);
    }


    @Override
    public void onTurn(int step) {
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        check();
        if (score > 0) {
            score--;
        }
        setScore(score);
        drawScene();
    }

    private void check() {
        if (rocket.isCollision(platform) && rocket.isStopped()) {
            win();
        } else if (rocket.isCollision(landscape)) {
            gameOver();
        }
    }

    private void win() {
        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.AQUA, "Обновляйся", Color.BLACK, 50);
        stopTurnTimer();
    }

    private void gameOver() {
        rocket.crash();
        isGameStopped = true;
        showMessageDialog(Color.BLUE, "Молодец", Color.BEIGE, 60);
        stopTurnTimer();
        score = 0;
    }


}