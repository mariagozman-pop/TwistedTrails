import javax.sound.sampled.*;
import java.io.InputStream;

public class BackgroundSound extends Sound {
    private String currentTrack;
    private static final String audioFilePathA = "resources/audio/717649__scpsea__robotic-music-player.wav";
    private static final String audioFilePathB = "resources/audio/277251__zetauri__zetauri_darkthrillerloop_j8.wav";
    public BackgroundSound() {
        super();
        currentTrack = audioFilePathA;
        playSoundLoop();
    }

    public void changeTrack() {
        stop(); // Stop the current background music

        if (currentTrack.equals(audioFilePathA)) {
            currentTrack = audioFilePathB;
        } else {
            currentTrack = audioFilePathA;
        }

        playSoundLoop(); // Play the new background music track in a loop
    }

    public void playSoundLoop() {
        try {
            InputStream inputStream = getClass().getResourceAsStream(currentTrack);
            assert inputStream != null;
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);

            AudioFormat audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);

            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.addLineListener(this);
            audioClip.open(audioStream);

            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            audioClip.start();
        } catch (Exception e) {
            System.err.println("!Error playing sound loop: " + e.getMessage());
        }
    }
}