import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 06.08.2017.
 */
public class Ball extends JPanel {

    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;

    public static final int POSX = Game.WIDTH / 2;
    public static final int POSY = Game.HEIGHT / 2;

    public int posX = POSX;
    public int posY = POSY;

    public int dirX = 1;
    public int dirY = 1;

    private int speed = 0;
    public boolean isBallIn = true;

    public int leRi = 1; // left, right out




    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(posX,posY,WIDTH,HEIGHT);
    }


    // Движение шара, проверка вылетел ли шар, и с какой стороны.
    public void bmove(int dir1, int dir2) {

        if(posY >= (Game.HEIGHT - 45) || posY <= 0 ) dirY *= -1;
        if(posX >= (Game.WIDTH - 20)){
            isBallIn = false;
            leRi = 1;
        }
        if(posX <= 0 ) {
            isBallIn = false;
            leRi = -1;
        }

        posX += speed * dirX * dir1;
        posY += speed * dirY * dir2;
    }


    public void setSpeed(int speed) {
        if (speed >= 0) this.speed = speed;
    }
}
