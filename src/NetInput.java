import java.awt.event.KeyEvent;

/**
 * Created by User on 19.08.2017.
 */
public class NetInput extends Input {
    public static int rMoveDir = 0; // 1 up, 0 stop, -1 down, 2 space. received
    public static int sMoveDir = 0; // 1 up, 0 stop, -1 down, 2 space. sent

    public void update() {

        if (rMoveDir == 1) {
            up = true;
            w = true;
            down = false;
            s = false;
            space = false;
        } else if (rMoveDir == -1) {
            space = false;
            down = true;
            s = true;
            up = false;
            w = false;

        } else if (rMoveDir == 2) {
            space = true;
            up = false;
            down = false;
            w = false;
            s = true;
        } else {
            up = false;
            down = false;
            w = false;
            s = true;
            space = false;
        }

    }


}
