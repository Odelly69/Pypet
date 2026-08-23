package com.odelly.pypet;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;

/** Calm, synthesized audio: no sudden loud sounds and no haptic feedback. */
public final class PypetAudio {
    private static final int SAMPLE_RATE = 22050;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private AudioTrack track;
    private boolean enabled = true;
    private boolean playing;
    private int step;

    private final int[] melody = {262, 330, 392, 330, 294, 349, 440, 349, 262, 330, 392, 523, 440, 392, 330, 294};

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) stop();
    }

    public boolean isEnabled() { return enabled; }

    public void start() {
        if (!enabled || playing) return;
        playing = true;
        step = 0;
        playNext();
    }

    public void stop() {
        playing = false;
        handler.removeCallbacksAndMessages(null);
        if (track != null) {
            try { track.stop(); } catch (Exception ignored) {}
            track.release();
            track = null;
        }
    }

    public void petSound(int frequency) {
        if (!enabled) return;
        playTone(frequency, 180);
    }

    private void playNext() {
        if (!playing || !enabled) return;
        playTone(melody[step % melody.length], 240);
        step++;
        handler.postDelayed(this::playNext, 290);
    }

    private void playTone(int frequency, int durationMs) {
        if (!enabled) return;
        int samples = SAMPLE_RATE * durationMs / 1000;
        short[] data = new short[samples];
        for (int i = 0; i < samples; i++) {
            double envelope = Math.min(1.0, i / 500.0) * Math.min(1.0, (samples - i) / 1200.0);
            data[i] = (short) (Math.sin(2.0 * Math.PI * frequency * i / SAMPLE_RATE) * 2200 * envelope);
        }
        if (track != null) {
            try { track.stop(); } catch (Exception ignored) {}
            track.release();
        }
        track = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build())
                .setBufferSizeInBytes(data.length * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build();
        track.write(data, 0, data.length);
        track.setVolume(0.16f);
        track.play();
    }
}
