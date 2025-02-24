import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class Sound implements LineListener {
    protected boolean isPlaybackCompleted;
    protected Clip audioClip;

    @Override
    public void update(LineEvent event) {
        if (LineEvent.Type.START == event.getType()) {
            System.out.println("Playback started.");
        } else if (LineEvent.Type.STOP == event.getType()) {
            isPlaybackCompleted = true;
            System.out.println("Playback completed.");
        }
    }

    public void play(String audioFilePath) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(audioFilePath);
            if (inputStream != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
                AudioFormat audioFormat = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);

                audioClip = (Clip) AudioSystem.getLine(info);
                audioClip.addLineListener(this);
                audioClip.open(audioStream);
                audioClip.start();

                Thread.sleep(1000);

                audioClip.close();
                audioStream.close();
            } else {
                // Handle the case where the file is not found gracefully
                System.err.println("!File not found: " + audioFilePath);
            }
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            // Handle specific exceptions
            System.err.println("!Error playing audio: " + e.getMessage());
        } catch (InterruptedException e) {
            // Handle InterruptedException
            Thread.currentThread().interrupt();
            System.err.println("!Audio playback interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("!Unexpected audio error: " + e.getMessage());
        }
    }

    public void stop() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
            isPlaybackCompleted = true;
        }
    }
}