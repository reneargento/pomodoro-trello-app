package com.rene.pomodorotrello.controllers;

import android.content.Context;
import android.media.MediaPlayer;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.interfaces.DatabaseFetchOperation;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.Card;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by rene on 6/20/16.
 */

public class PomodoroController {

    public String getFormattedTimeFromMilliseconds(long millisUntilFinished) {
        return String.valueOf(String.format(Locale.getDefault(),
                Constants.POMODORO_FORMAT,
                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
    }

    public long getMillisecondsFromFormattedTime(String formattedTime) {

        long hours = Integer.parseInt(formattedTime.substring(0,2));
        long minutes = Integer.parseInt(formattedTime.substring(3,5));
        long seconds = Integer.parseInt(formattedTime.substring(6,8));

        long totalSeconds = seconds + (minutes * 60) + (hours * 60 * 60);

        return TimeUnit.SECONDS.toMillis(totalSeconds);
    }

    public void playSound(final Context context) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.tone);
                mediaPlayer.start();
            }
        }).start();
    }

    public void getOrCreateAllCardsFetched(List<Card> cards, DatabaseFetchOperation databaseFetchOperation) {

        if (cards.size() > 0) {
            CardDatabaseController cardDatabaseController = new CardDatabaseController();

            for (Card card : cards) {
                cardDatabaseController.getOrCreateCard(card, databaseFetchOperation);
            }
        }
    }

}
