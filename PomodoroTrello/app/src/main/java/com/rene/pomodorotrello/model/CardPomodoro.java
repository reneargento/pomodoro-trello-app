package com.rene.pomodorotrello.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rene on 6/20/16.
 */

public class CardPomodoro extends RealmObject{

    public final static String ID_KEY = "id";
    public final static String NAME_KEY = "name";
    public final static String TOTAL_MILLISECONDS_SPENT_KEY = "totalMillisecondsSpent";
    public final static String POMODOROS_KEY = "pomodoros";

    @PrimaryKey
    public String id;

    public String name;
    public long totalMillisecondsSpent;
    public int pomodoros;

}
