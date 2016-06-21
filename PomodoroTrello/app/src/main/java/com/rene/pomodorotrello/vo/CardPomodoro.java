package com.rene.pomodorotrello.vo;

import io.realm.RealmObject;

/**
 * Created by rene on 6/20/16.
 */

public class CardPomodoro extends RealmObject{

    public String id;
    public String name;
    public long totalMillisecondsSpent;
    public int pomodoros;

}
