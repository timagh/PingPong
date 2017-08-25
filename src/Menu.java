
import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {

    private int WIDTH;
    private int HEIGHT;

    private String[] options = {"One Player", "Two Players", "Host Server", "Join Server", "Help", "Quit"};
    private String[] help = {"Use W & S for left Paddle", "Up & Down for right Paddle", "One player vs `AI` ",
            "Two players on one PC", "Use Space to start ball move", "Use Esc to exit to main menu"};

    private int currselect = 0;
    public int plNumb = 0; //0- no players,1 - one player, 2 - two players, 3- host, 4-client.

    public Menu(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (plNumb != -1) {
            for (int i = 0; i < options.length; i++) {
                if (i == currselect) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.GRAY);
                }
                g.setFont(new Font("Arial", Font.PLAIN, 72));
                g.drawString(options[i], (Game.WIDTH / 2 - 180), 90 + i * 90);
            }
        } else if (plNumb == -1) {
            for (int i = 0; i < help.length; i++) {
                g.setColor(Color.gray);
                g.setFont(new Font("Arial", Font.PLAIN, 42));
                g.drawString(help[i], (Game.WIDTH / 2 - 260), 90 + i * 80);
            }
        }


        g.dispose();
    }

    // Обновление меню, и действия при нажатии enter на пункте
    public void update(int code) {

        if (code == -1) {
            currselect--;
            if (currselect < 0) {
                currselect = options.length - 1;
            }
        }
        if (code == 1) {
            currselect++;
            if (currselect >= options.length) {
                currselect = 0;
            }
        }

        if (code == 0) {
            if (currselect == 0) {
                Game.gstat = true;
                plNumb = 1;
            } else if (currselect == 1) {
                Game.gstat = true;
                plNumb = 2;
            } else if (currselect == 2) {
                Game.isHost = true;
                Game.gstat = true;
                plNumb = 3;
            } else if (currselect == 3) {
                Game.isClient = true;
                Game.gstat = true;
                plNumb = 4;
            } else if (currselect == 4) {
                plNumb = -1;
            } else if (currselect == 5) {
                System.exit(0);
            }
        }


    }

}





























