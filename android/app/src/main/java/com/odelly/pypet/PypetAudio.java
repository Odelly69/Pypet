package com.odelly.pypet;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;

/** Calm game audio. Audio is optional and must never prevent Pypet from starting. */
public final class PypetAudio {
    private static final int SAMPLE_RATE = 22050;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private AudioTrack track;
    private boolean musicEnabled = true;
    private boolean sfxEnabled = true;
    private boolean playing;
    private int step;
    private final int[] melody = {262,294,330,392,349,330,294,262};
    private final int[] harmony = {131,147,165,196,175,165,147,131};

    public void setEnabled(boolean enabled) { setMusicEnabled(enabled); }
    public boolean isEnabled() { return musicEnabled; }
    public void setMusicEnabled(boolean enabled) { musicEnabled = enabled; if (!enabled) stop(); }
    public boolean isMusicEnabled() { return musicEnabled; }
    public void setSfxEnabled(boolean enabled) { sfxEnabled = enabled; }
    public boolean isSfxEnabled() { return sfxEnabled; }
    public void start() { if (!musicEnabled || playing) return; playing = true; step = 0; playNext(); }
    public void stop() { playing = false; handler.removeCallbacksAndMessages(null); releaseTrack(); }
    public void petSound(int frequency) { if (sfxEnabled) playPetTone(frequency, 180, .08); }
    public void petSound(String species) { if (sfxEnabled) playPetTone(330, 160, .07); }
    private void playNext() { if (!playing || !musicEnabled) return; try { playChord(melody[step % melody.length], harmony[step % harmony.length], 330); step++; handler.postDelayed(this::playNext, 390); } catch (Throwable t) { playing=false; releaseTrack(); } }
    private void playChord(int high,int low,int durationMs){int samples=SAMPLE_RATE*durationMs/1000;short[] data=new short[samples];for(int i=0;i<samples;i++){double t=(double)i/SAMPLE_RATE;double env=Math.min(1.0,i/900.0)*Math.min(1.0,(samples-i)/1800.0);double wave=.58*Math.sin(2*Math.PI*high*t)+.25*Math.sin(2*Math.PI*high*2*t)+.22*Math.sin(2*Math.PI*low*t);data[i]=(short)(wave*2200*env);}playBuffer(data,.10f);}
    private void playPetTone(int frequency,int durationMs,double volume){int samples=SAMPLE_RATE*durationMs/1000;short[] data=new short[samples];for(int i=0;i<samples;i++){double t=(double)i/SAMPLE_RATE;double env=Math.min(1.0,i/700.0)*Math.min(1.0,(samples-i)/1400.0);data[i]=(short)(Math.sin(2*Math.PI*frequency*t)*2600*env*volume);}playBuffer(data,.15f);}
    private void playBuffer(short[] data,float volume){if(data==null||data.length==0||(!musicEnabled&&!sfxEnabled))return;releaseTrack();try{AudioAttributes a=new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();AudioFormat f=new AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(SAMPLE_RATE).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build();int min=AudioTrack.getMinBufferSize(SAMPLE_RATE,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT);int buffer=Math.max(min,data.length*2);track=new AudioTrack.Builder().setAudioAttributes(a).setAudioFormat(f).setBufferSizeInBytes(buffer).setTransferMode(AudioTrack.MODE_STATIC).build();if(track.getState()!=AudioTrack.STATE_INITIALIZED){releaseTrack();return;}track.write(data,0,data.length);track.setVolume(volume);track.play();}catch(Throwable ignored){releaseTrack();}}
    private void releaseTrack(){if(track!=null){try{track.stop();}catch(Throwable ignored){}try{track.release();}catch(Throwable ignored){}track=null;}}
}
