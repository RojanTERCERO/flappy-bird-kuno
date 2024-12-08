import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
    public static void playSound(String FlappyBirdTrack) {
        try {
            // Load the audio file
            File soundFile = new File(FlappyBirdTrack);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(soundFile));

            // Play the audio
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
