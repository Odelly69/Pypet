package com.odelly.pypet;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;

/** Calm game audio: fuller musical phrases and species-aware pet sounds without sudden effects. */
public final class PypetAudio {
    private static final int SAMPLE_RATE = 22050;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private AudioTrack track;
    private boolean enabled = true;
    private boolean playing;
    private int step;

    // Gentle 32-step phrase with melody + harmony + bass for a fuller musical feel.
    private final int[] melody = {262,294,330,392,349,330,294,262, 262,330,392,440,392,349,330,294,
            330,392,523,440,392,349,330,294, 262,294,330,392,440,392,330,262};
    private final int[] harmony = {131,147,165,196,175,165,147,131, 131,165,196,220,196,175,165,147,
            165,196,262,220,196,175,165,147, 131,147,165,196,220,196,165,131};

    public void setEnabled(boolean enabled) { this.enabled = enabled; if (!enabled) stop(); }
    public boolean isEnabled() { return enabled; }
    public void start() { if (!enabled || playing) return; playing = true; step = 0; playNext(); }
    public void stop() { playing = false; handler.removeCallbacksAndMessages(null); releaseTrack(); }

    /** Generic gentle pet vocalization/interaction sound. */
    public void petSound(int frequency) { if (enabled) playPetTone(frequency, 220, 0.10); }

    /** Species-aware sound profiles: frog croak, bird chirp, snake hiss-like breath, aquatic blip, mammal chirp. */
    public void petSound(String species) {
        if (!enabled) return;
        String s = species == null ? "" : species.toLowerCase();
        if (s.contains("frog")) playPetTone(180, 360, 0.13);
        else if (s.contains("bird")) playPetTone(880, 140, 0.10);
        else if (s.contains("snake")) playNoise(300);
        else if (s.contains("fish") || s.contains("shark")) playPetTone(420, 130, 0.08);
        else playPetTone(330, 180, 0.10);
    }

    private void playNext() {
        if (!playing || !enabled) return;
        playChord(melody[step % melody.length], harmony[step % harmony.length], 330);
        step++;
        handler.postDelayed(this::playNext, 390);
    }

    private void playChord(int high, int low, int durationMs) {
        int samples = SAMPLE_RATE * durationMs / 1000;
        short[] data = new short[samples];
        for (int i = 0; i < samples; i++) {
            double t = (double)i / SAMPLE_RATE;
            double attack = Math.min(1.0, i / 900.0);
            double release = Math.min(1.0, (samples - i) / 1800.0);
            double env = attack * release;
            double wave = 0.58 * Math.sin(2*Math.PI*high*t)
                    + 0.25 * Math.sin(2*Math.PI*(high*2)*t)
                    + 0.22 * Math.sin(2*Math.PI*low*t);
            data[i] = (short)(wave * 2600 * env);
        }
        playBuffer(data, 0.13f);
    }

    private void playPetTone(int frequency, int durationMs, double volume) {
        int samples = SAMPLE_RATE * durationMs / 1000;
        short[] data = new short[samples];
        for (int i=0;i<samples;i++) {
            double t=(double)i/SAMPLE_RATE;
            double env=Math.min(1.0,i/700.0)*Math.min(1.0,(samples-i)/1400.0);
            double wobble=1.0+0.025*Math.sin(2*Math.PI*5*t);
            data[i]=(short)(Math.sin(2*Math.PI*frequency*wobble*t)*3000*env*volume);
        }
        playBuffer(data, 0.20f);
    }

    private void playNoise(int durationMs) {
        int samples=SAMPLE_RATE*durationMs/1000; short[] data=new short[samples]; long seed=17;
        for(int i=0;i<samples;i++){seed=seed*1103515245+12345; double n=((seed>>>16)&0x7fff)/32767.0*2-1; double env=Math.min(1.0,i/1000.0)*Math.min(1.0,(samples-i)/1600.0); data[i]=(short)(n*1700*env);}
        playBuffer(data,0.07f);
    }

    private void playBuffer(short[] data, float volume) {
        if (!enabled) return;
        releaseTrack();
        track = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                .setAudioFormat(new AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(SAMPLE_RATE).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                .setBufferSizeInBytes(data.length*2).setTransferMode(AudioTrack.MODE_STATIC).build();
        track.write(data,0,data.length); track.setVolume(volume); track.play();
    }
    private void releaseTrack(){ if(track!=null){try{track.stop();}catch(Exception ignored){} track.release(); track=null;} }
}