package edu.cmu.cs.dennisc.media.javafx;

import javafx.scene.media.MediaException;
import org.lgna.common.resources.AudioResource;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class MediaFactory extends edu.cmu.cs.dennisc.media.MediaFactory {
    private static MediaFactory singleton;

    static {
        MediaFactory.singleton = new MediaFactory();
    }

    public static MediaFactory getSingleton() {
        return MediaFactory.singleton;
    }

    private MediaFactory() {
    }

    public AudioResource createAudioResource(File file) throws IOException {
        String contentType = AudioResource.getContentType(file);
        if (contentType != null) {
            final AudioResource rv = new AudioResource(file, contentType);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Player player = new Player(createJavaFXPlayer(rv), 1.0, 0.0, Double.NaN, rv);
                    player.realize();
                    rv.setDuration(player.getDuration());
                }
            };
            final boolean USE_THREAD_JUST_TO_BE_SORT_OF_SAFE = true;
            if (USE_THREAD_JUST_TO_BE_SORT_OF_SAFE) {
                new Thread(runnable).start();
            } else {
                runnable.run();
            }
            return rv;
        } else {
            throw new RuntimeException("content type not found for " + file);
        }
    }

    private MediaPlayer createJavaFXPlayer(AudioResource audioResource) {
        assert audioResource != null;

        byte[] data = audioResource.getData();

        File tempAudioFile = null;
        FileOutputStream fos = null;

        try {
            tempAudioFile = File.createTempFile("VACA", ".mp3", null);
            tempAudioFile.deleteOnExit();

            fos = new FileOutputStream(tempAudioFile);
            fos.write(data);
        } catch (IOException ioe) {
            System.out.println("Error creating temp file for AudioResource conversion");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.out.println("Error closing temp file for AudioResource conversion");
                }
            }
        }

        try {
            String source = audioResource.getTempFile().toURI().toURL().toString();

            return new MediaPlayer(new Media(source));
        } catch (MalformedURLException | NullPointerException | MediaException ex) {
            throw new RuntimeException(audioResource.toString(), ex);
        }
    }

    @Override
    public Player createPlayer(AudioResource audioResource, double volume, double startTime, double stopTime) {
        Player player = new Player(createJavaFXPlayer(audioResource), volume, startTime, stopTime, audioResource);
        if (Double.isNaN(audioResource.getDuration())) {
            player.realize();
            audioResource.setDuration(player.getDuration());
        }
        return player;
    }
}