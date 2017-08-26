import javax.swing.*;
import java.awt.*;

public class Game extends JPanel implements Runnable {

    private boolean running = false;
    private Thread thread;
    private Input input = new Input();
    private NetInput netInput = new NetInput();

    private int dir1 = 1;
    private int dir2 = 1;
    private int score1 = 0;
    private int score2 = 0;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;


    public static boolean isHost = false;
    public static boolean isClient = false;
    public static boolean netStat = false;
    public static boolean gstat = false;


    Paddle pad1;
    Paddle pad2;
    PaddleAI paddleAI;
    Ball ball;
    Menu menu;
    Server server;
    Client client;

    private static JFrame frame;


    public Game() {
        pad1 = new Paddle(Paddle.P1_POSX);
        pad2 = new Paddle(Paddle.P2_POSX);
        paddleAI = new PaddleAI(Paddle.P2_POSX);
        ball = new Ball();
        menu = new Menu(WIDTH, HEIGHT);

        this.addKeyListener(input);
        this.setFocusable(true);
        this.requestFocus();
    }

    public void paint(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (gstat) {
            pad1.paint(g);

            if (menu.plNumb == 1) {
                paddleAI.paint(g);
            } else  {
                pad2.paint(g);
            }
            ball.paint(g);
            g.drawString(String.valueOf(score1) + " : " + String.valueOf(score2), 400, 20);
            g.dispose();
        } else {
            menu.paint(g);
            g.dispose();
        }
    }


    public void render() {
        repaint();
    }


    // Обновление "логики" игры
    public void update() {

        input.update();

        //Перезапуск игры, если нажали esc: сброс счета, и кол-ва игроков
        if (input.esc) {

            restart();
            score1 = 0;
            score2 = 0;
            menu.plNumb = 0;
            gstat = false;

        }

        if (menu.plNumb == 3) {
            if (input.space) ball.setSpeed(6);
            if (input.w && !input.s) pad1.move(-1);
            if (input.s && !input.w) pad1.move(1);
            netUpdate();
        } else if (menu.plNumb == 1) {
            if (input.space) ball.setSpeed(6);
            if (input.w && !input.s) pad1.move(-1);
            if (input.s && !input.w) pad1.move(1);
            paddleAI.move(ball.posY);
        } else if (menu.plNumb == 2) {
            if (input.space) ball.setSpeed(6);
            if (input.w && !input.s) pad1.move(-1);
            if (input.s && !input.w) pad1.move(1);
            if (input.up && !input.down) pad2.move(-1);
            if (input.down && !input.up) pad2.move(1);
        } else if (menu.plNumb == 4) {
            if (input.up && !input.down) pad2.move(-1);
            if (input.down && !input.up) pad2.move(1);
            netUpdate();
        }

        Collide();

        ball.bmove(dir1, dir2);
        // Если мяч вылетел, перезапускаем раунд и прибавляем очки нужному игроку
        if (!ball.isBallIn) {
            restart();
            if (ball.leRi == 1) score1++;
            if (ball.leRi == -1) score2++;
        }

    }


    // Возвращаем все на исходные позиции
    public void restart() {

        pad1.posY = Paddle.POSY;
        pad2.posY = Paddle.POSY;
        ball.posX = Ball.POSX;
        ball.posY = Ball.POSY;
        paddleAI.posY = Paddle.POSY;

        removeAll();
        revalidate();
        repaint();

        ball.isBallIn = true;
        ball.setSpeed(0);


    }


    public void netUpdate() {
        netInput.update();
        if (menu.plNumb == 3) {
            if (netInput.up) pad2.move(-1);
            if (netInput.down) pad2.move(1);
        } else if (menu.plNumb == 4) {
            if (netInput.space) ball.setSpeed(6);
            if (netInput.up) pad1.move(-1);
            if (netInput.down) pad1.move(1);
        }

    }

    //Обновление меню
    public void mupdate() {
        input.update();
        if (input.up) menu.update(-1);
        if (input.down) menu.update(1);
        if (input.enter) menu.update(0);

    }


    //Проверка столкновения, шарика и ракеток
    public void Collide() {
        int bX = ball.posX;
        int bY = ball.posY;
        int p1X = pad1.posX;
        int p1Y = pad1.posY;
        int p2X = pad2.posX;
        int p2Y = pad2.posY;
        int pAiX = paddleAI.posX;
        int pAiY = paddleAI.posY;

        if (((p1X + pad1.WIDTH) == bX) && (p1Y <= (bY + ball.HEIGHT)) && ((p1Y + pad1.HEIGHT) >= (bY - ball.HEIGHT))) {
            dir1 *= -1;
        }

        if (menu.plNumb == 2 || menu.plNumb == 3 || menu.plNumb == 4) {
            if ((p2X == (bX + ball.WIDTH)) && (p2Y <= (bY + ball.HEIGHT)) && ((p2Y + pad1.HEIGHT) >= (bY - ball.HEIGHT))) {
                dir1 *= -1;
            }
        } else if (menu.plNumb == 1) {
            if ((pAiX == (bX + ball.WIDTH)) && (pAiY <= (bY + ball.HEIGHT)) && ((pAiY + pad1.HEIGHT) >= (bY - ball.HEIGHT))) {
                dir1 *= -1;
            }
        }

    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }

    public synchronized void stop() {


        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public void run() {

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        long targetFPS = 60; // кол-во кадров в секунду
        long targetTime = 1_000_000_000 / targetFPS; // часто вызова  render()
        final double ns = 1_000_000_000.0 / 60.0; // частота вызова update()
        final double ns2 = 1_000_000_000.0 / 9.0; // частота обновления меню

        double delta = 0;
        double mdelta = 0;
        int frames = 0;
        int updates = 0;


        while (running) {
            long currTime = System.nanoTime();
            delta += (currTime - lastTime) / ns;
            mdelta += (currTime - lastTime) / ns2;
            lastTime = currTime;

            // Если игра не запущена, и нет игроков, обновляем только меню с заданной частотой
            if (!gstat && menu.plNumb == 0) {

                while (mdelta >= 1) {
                    mupdate();
                    render();
                    mdelta--;
                }
                // Если игра заупущена, то обновляем update() и render(), c заданной частотой
            } else {

                //Запуск сетевой игры
                if (isHost && !netStat) {
                    server = new Server();
                    server.start();
                    netStat = true;

                } else if (isClient && !netStat) {
                    client = new Client();
                    client.start();
                    netStat = true;

                }

                while (delta >= 1) {
                    update();
                    updates++;
                    delta--;

                }

                render();
                frames++;


                // подсчет обновление и кол-ва кадров в секунду
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    frame.setTitle("Ping Pong   ||  " + "ups: " + updates + "  fps: " + frames);
                    updates = 0;
                    frames = 0;
                }

            }

            // Пока не пришло время обновлять что-либо , приостанавливаем поток для экономии
            long totalTime = System.nanoTime() - currTime;
            if (totalTime < targetTime) {
                try {
                    Thread.sleep((targetTime - totalTime) / 1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    public static void main(String[] args) {
        Game game = new Game();
        frame = new JFrame();
        frame.setResizable(false);
        frame.setSize(WIDTH, HEIGHT);
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        frame.add(game);
        frame.pack();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }

}