/**
 * Created by User on 08.08.2017.
 */
public class PaddleAI extends Paddle {

    public PaddleAI(int posX) {
        super(posX);
    }

    // Движение ракетки пк
    public void move(int bY) {

        int ac = (int) (Math.random() * (3 - 1)) + 1; // ускорение ракетки либо на 1 либо на 2

        if (posY >= 0 && posY >= bY) {
            posY -= (speed + ac);
        }


        if ((posY + HEIGHT) <= (Game.HEIGHT - 25) && (posY + HEIGHT) <= bY) {
            posY += (speed + ac);
        }


    }
}
