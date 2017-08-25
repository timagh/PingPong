import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 05.08.2017.
 */
public class Paddle extends JPanel {

    public static final int WIDTH = 20;
    public static final int HEIGHT = 200;

    public static final int POSY = 180;
    public static final int P1_POSX = 50;
    public static final int P2_POSX = Game.WIDTH - 80;

    protected int posX;
    protected int posY = POSY;
    protected int speed = 3;


    public Paddle(int posX) {
        this.posX = posX;
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(posX, posY, WIDTH, HEIGHT);
    }


    public void move(int dir) {

        if (dir == -1) {
            if (posY <= 0) {
                dir = 0;
                posY += speed * dir;
            } else {
                posY += speed * dir;
            }

        }

        if (dir == 1) {
            if ((posY + HEIGHT) >= (Game.HEIGHT - 25)) {
                dir = 0;
                posY += speed * dir;
            } else {
                posY += speed * dir;
            }
        }

    }


}
