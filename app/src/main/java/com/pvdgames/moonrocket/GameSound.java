package com.pvdgames.moonrocket;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class GameSound {

    private SoundPool soundPool;
    private int sound[] = new int[10];

    private int turbineId = 0;

    public GameSound(Context ctx) {

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        sound[0] = soundPool.load(ctx, R.raw.soundexplosion, 1);
        sound[1] = soundPool.load(ctx, R.raw.soundturbine, 1);
    }

    public void explode() {

        soundPool.play(sound[0], 1f, 1f, 1, 0, 1f);
    }

    public void startTurbine() {

        if (turbineId != 0) {

            soundPool.stop(turbineId);
        }

        turbineId = soundPool.play(sound[1], 3f, 3f, 1, 99999, 1f);
    }

    public void stopTurbine() {

        if (turbineId != 0) {

            soundPool.stop(turbineId);
        }
    }
}
