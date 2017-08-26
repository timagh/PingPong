import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Input implements KeyListener {

    private boolean[] keys = new boolean[120];
    public boolean up, down, left, right, w, s, a, d, space, enter, esc;

    public void update() {

        up = keys[KeyEvent.VK_UP];
        w = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_DOWN];
        s = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_LEFT];
        a = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_RIGHT];
        d = keys[KeyEvent.VK_D];
        space = keys[KeyEvent.VK_SPACE];
        enter = keys[KeyEvent.VK_ENTER];
        esc = keys[KeyEvent.VK_ESCAPE];


        if ((up || w) && (!down && !s) ) {
            NetInput.sMoveDir = 1;
        } else if ((down || s) && (!up && !w)) {
            NetInput.sMoveDir = -1;
        } else if (space) {
            NetInput.sMoveDir = 2;
        } else {
            NetInput.sMoveDir = 0;
        }


    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;

    }

}
