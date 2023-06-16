package edu.cmu.cs.dennisc.media.javafx;

import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import org.lgna.common.resources.AudioResource;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.beans.value.ChangeListener;

public class Player extends edu.cmu.cs.dennisc.media.Player {
    private final MediaPlayer player;
    private final double volumeLevel;
    private final double startTime;
    private final double stopTime;
    private final AudioResource audioResource;

    Player(MediaPlayer player, double volumeLevel, double startTime, double stopTime, AudioResource resourceReference) {
        this.player = player;
        this.volumeLevel = volumeLevel;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.audioResource = resourceReference;
    }

    @Override
    public void realize() {
        if (this.player.getStatus() == MediaPlayer.Status.UNKNOWN) {
            PrintUtilities.println("realize: ", this.player.getStatus());

            ChangeListener<MediaPlayer.Status> listener = (ov, oldVal, newVal) -> {
                synchronized (this) {
                    if (newVal != MediaPlayer.Status.UNKNOWN) {
                        this.notify();
                    }
                }
            };

            player.statusProperty().addListener(listener);

            synchronized (listener) {
                if (player.getStatus() == MediaPlayer.Status.UNKNOWN) {
                    try {
                        listener.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }

            player.statusProperty().removeListener(listener);
        }
    }

    @Override
    public void start() {
        this.realize();
        if (!Double.isNaN(this.startTime)) {
            this.player.setStartTime(Duration.seconds(this.startTime));
        }
        if (!Double.isNaN(this.stopTime)) {
            this.player.setStopTime(Duration.seconds(this.stopTime));
        }
        if (!EpsilonUtilities.isWithinReasonableEpsilon(this.volumeLevel, 1.0)) {
            double defaultVolumeLevel = this.player.getVolume();

            double v = this.volumeLevel * defaultVolumeLevel;
            v = Math.max(v, 0.0);
            v = Math.min(v, 1.0);
            this.player.setVolume(v);
        }
        this.player.play();
    }

    @Override
    public void stop() {
        this.player.stop();
        this.player.dispose();
    }

    @Override
    public double getDuration() {
        return this.player.getTotalDuration().toSeconds();
    }

    private static final double CONSIDERED_TO_BE_STARTED_THRESHOLD = 0.1;

    @Override
    public double getTimeRemaining() {
        double duration = this.player.getTotalDuration().toSeconds();
        double stop = this.player.getStopTime().toSeconds();
        double curr = this.player.getCurrentTime().toSeconds();

        double end = Math.min(duration, stop);
        double rv = end - curr;

        MediaPlayer.Status state = this.player.getStatus();
        if (state != MediaPlayer.Status.PLAYING && curr > (this.startTime + CONSIDERED_TO_BE_STARTED_THRESHOLD)) {
            rv = 0.0;
        }
        return rv;
    }

    @Override
    public void playUntilStop() {

        ChangeListener<MediaPlayer.Status> listener = (ov, oldVal, newVal) -> {
            synchronized (this) {
                if (newVal == MediaPlayer.Status.STOPPED || newVal == MediaPlayer.Status.HALTED) {
                    this.notify();
                }
            }
        };

        player.statusProperty().addListener(listener);

        this.start();

        synchronized (listener) {
            if (player.getStatus() != MediaPlayer.Status.STOPPED && player.getStatus() != MediaPlayer.Status.HALTED) {
                try {
                    listener.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }

        player.statusProperty().removeListener(listener);
    }

    public double getVolumeLevel() {
        return this.volumeLevel;
    }

    public double getStartTime() {
        return this.startTime;
    }

    public double getStopTime() {
        return this.stopTime;
    }

    public AudioResource getAudioResource() {
        return this.audioResource;
    }
}
