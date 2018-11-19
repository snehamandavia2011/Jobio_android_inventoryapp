package utility;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

import asyncmanager.asyncMessageList;

/**
 * Created by SAI on 6/20/2016.
 */
public class MessageLoader {
    Timer timer;
    TimerTask timerTask;
    Context mContext;

    public MessageLoader(Context mContext) {
        this.mContext = mContext;
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        //timer.schedule(timerTask, 0, 1000 * 30);
        timer.schedule(timerTask, 0, 1000 * 60 * 3);
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                new asyncMessageList(mContext);
            }
        };
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
