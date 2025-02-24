public class WinningSounds extends Sound {
    private static final String itemCollectedSoundFilePath = "resources/audio/699501__valenspire__coin_drop_06.wav";
    private static final String levelFinishedSoundFilePath = "resources/audio/684986__supersouper__finish-sound-effect-videogame.wav";

    public WinningSounds() {
        super();
    }

    public void playItemCollectedSound() {
        play(itemCollectedSoundFilePath);
    }

    public void playLevelFinishedSound() {
        play(levelFinishedSoundFilePath);
    }
}