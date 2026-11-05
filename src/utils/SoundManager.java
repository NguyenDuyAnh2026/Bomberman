package utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public final class SoundManager {

    private static MediaPlayer backgroundPlayer;
    private static AudioClip bombSound;

    private SoundManager() {
        // Không cho tạo object SoundManager
    }

    // ================= BACKGROUND MUSIC =================

    public static void startBackgroundMusic() {
        // Nếu nhạc đã được tạo thì chỉ cần phát tiếp
        if (backgroundPlayer != null) {
            if (backgroundPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                backgroundPlayer.play();
            }
            return;
        }

        var musicUrl = SoundManager.class.getResource("/sounds/music.mp3");

        if (musicUrl == null) {
            System.out.println("Cannot find background music!");
            return;
        }

        Media media = new Media(musicUrl.toExternalForm());
        backgroundPlayer = new MediaPlayer(media);

        // Lặp nhạc vô hạn
        backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // Âm lượng nhạc nền: 0.0 -> 1.0
        backgroundPlayer.setVolume(0.3);

        backgroundPlayer.play();
    }

    public static void stopBackgroundMusic() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
            backgroundPlayer.dispose();
            backgroundPlayer = null;
        }
    }

    // ================= BOMB SOUND =================

    public static void playBomb() {
        if (bombSound == null) {
            var soundUrl = SoundManager.class.getResource("/sounds/soundOfBom.wav");

            if (soundUrl == null) {
                System.out.println("Cannot find bomb sound!");
                return;
            }

            bombSound = new AudioClip(soundUrl.toExternalForm());
            bombSound.setVolume(0.4);
        }

        bombSound.play();
    }

    // Dành cho pickup sau này
    public static void playPickup() {
        var soundUrl = SoundManager.class.getResource("/sounds/pickup.wav");

        if (soundUrl == null) {
            System.out.println("Cannot find pickup sound!");
            return;
        }

        AudioClip pickupSound = new AudioClip(soundUrl.toExternalForm());
        pickupSound.setVolume(0.4);
        pickupSound.play();
    }
}