import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener, MouseListener {
    // Screen dimensions
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int boardWidth = (int) screenSize.getWidth();
    int boardHeight = (int) screenSize.getHeight();

    // Offset adjustments for elements
    int offsetX = 0;
    int offsetY = 0;

    // Thresholds for transitions
    private int randomThreshold;
    private int transitionThreshold;

    // Game elements
    private BufferedImage blendedImage;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Pipe> pipes;
    private Random random = new Random();

    // Game images
    Image backgroundImg, birdImg, topPipeImg, bottomPipeImg, gameoverImg, playbuttonImg;
    Image nighttimeImg, leaderImg, scoreboardImg, silverImg, bronzeImg, goldImg, uddImg;

    // Game objects
    Bird bird;
    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = true;
    double score = 0;

    // Speeds and physics
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    // Medal and button objects
    Udd udd;
    Gold gold;
    Silver silver;
    Bronze bronze;
    Scoreb scoreb;
    Leader leader;
    Game game;
    Play play;

    // Constructor
    public FlappyBird() {
        // Panel setup
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        // Random threshold for transitions
        randomThreshold = (int) (Math.random() * (50 - 100 + 1)) + 100;
        transitionThreshold = 100;

        // Initialize buffered image
        blendedImage = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB);

        // Load images
        loadImages();

        // Initialize game objects
        initializeGameObjects();

        // Initialize pipe placement timer
        placePipeTimer = new Timer(1500, e -> placePipes());
        placePipeTimer.start();

        // Initialize game loop timer
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    // Load all necessary images
    private void loadImages() {
        backgroundImg = new ImageIcon(getClass().getResource("./fullscreenbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        gameoverImg = new ImageIcon(getClass().getResource("./logo.png")).getImage();
        playbuttonImg = new ImageIcon(getClass().getResource("./playbutton.png")).getImage();
        nighttimeImg = new ImageIcon(getClass().getResource("./fsnighttimebg.png")).getImage();
        leaderImg = new ImageIcon(getClass().getResource("./leaderboard.png")).getImage();
        scoreboardImg = new ImageIcon(getClass().getResource("./scoreboard.png")).getImage();
        bronzeImg = new ImageIcon(getClass().getResource("./bronze.png")).getImage();
        silverImg = new ImageIcon(getClass().getResource("./silver.png")).getImage();
        goldImg = new ImageIcon(getClass().getResource("./gold.png")).getImage();
        uddImg = new ImageIcon(getClass().getResource("./udd.png")).getImage();
    }

    // Initialize game-related objects
    private void initializeGameObjects() {
        bird = new Bird(birdImg);
        pipes = new ArrayList<>();

        udd = new Udd(uddImg);
        gold = new Gold(goldImg);
        silver = new Silver(silverImg);
        bronze = new Bronze(bronzeImg);
        scoreb = new Scoreb(scoreboardImg);
        leader = new Leader(leaderImg);
        game = new Game(gameoverImg);
        play = new Play(playbuttonImg);
    }

    // Place new pipes on the board
    private void placePipes() {
        int randomPipeY = (int) (random.nextInt(pipeHeight / 2) - pipeHeight / 5);
        int openingSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(topPipeImg, boardWidth, randomPipeY);
        Pipe bottomPipe = new Pipe(bottomPipeImg, boardWidth, randomPipeY + pipeHeight + openingSpace);

        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }

    // Main paint method
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Drawing routine
    private void draw(Graphics g) {
        drawBackground(g);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        drawScoreboard(g);
    }

    // Draw the background with blending effects
    private void drawBackground(Graphics g) {
        if (score >= randomThreshold) {
            float transitionFactor = Math.min(1, (float) (score - randomThreshold) / transitionThreshold);
            Image blended = blendImages(backgroundImg, nighttimeImg, transitionFactor);
            g.drawImage(blended, 0, 0, boardWidth, boardHeight, null);
        } else {
            g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
        }
    }

    // Helper to blend images
    private Image blendImages(Image img1, Image img2, float alpha) {
        BufferedImage blended = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = blended.createGraphics();
        g2d.drawImage(img1, 0, 0, boardWidth, boardHeight, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(img2, 0, 0, boardWidth, boardHeight, null);
        g2d.dispose();
        return blended;
    }

    // Draw the scoreboard and medals
    private void drawScoreboard(Graphics g) {
        if (gameOver) {
            g.drawImage(scoreboardImg, scoreb.x, scoreb.y, scoreb.width, scoreb.height, null);
            g.setColor(new Color(84, 56, 71));
            g.setFont(new Font("Dubai Medium", Font.BOLD, 52));
            g.drawString(String.valueOf((int) score), scoreb.x + 200, scoreb.y + 100);

            g.drawImage(gameoverImg, game.x, game.y, game.width, game.height, null);
            g.drawImage(playbuttonImg, play.x, play.y, play.width, play.height, null);

            if (score >= 10 && score <= 80) {
                g.drawImage(bronzeImg, bronze.x, bronze.y, bronze.width, bronze.height, null);
            } else if (score >= 81 && score <= 150) {
                g.drawImage(silverImg, silver.x, silver.y, silver.width, silver.height, null);
            } else if (score >= 151 && score <= 250) {
                g.drawImage(goldImg, gold.x, gold.y, gold.width, gold.height, null);
            } else if (score > 250) {
                g.drawImage(uddImg, udd.x, udd.y, udd.width, udd.height, null);
            }
        }
    }

    // Player leaderboard update and logic omitted for brevity

    @Override
    public void actionPerformed(ActionEvent e) {
        // Game loop updates
        if (!gameOver) {
            velocityY += gravity;
            bird.y += velocityY;

            for (Pipe pipe : pipes) {
                pipe.x += velocityX;
            }

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -11;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    // Helper classes omitted for brevity

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
