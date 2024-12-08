import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        // Get screen dimensions for full-screen mode
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int boardWidth = (int) screenSize.getWidth();
        int boardHeight = (int) screenSize.getHeight();

        // Create the main game window
        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setResizable(false); // Prevent resizing
        frame.setUndecorated(true); // Remove window decorations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the FlappyBird game panel
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();

        // Request focus for keyboard input and display the frame
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}
