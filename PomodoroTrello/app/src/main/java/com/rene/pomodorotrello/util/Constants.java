package com.rene.pomodorotrello.util;

/**
 * Created by rene on 6/14/16.
 */

public class Constants {

    public final static String BASE_URL = "https://api.trello.com/1/";
    public final static String PUBLIC_KEY = "_EKkCwzCd2_vigduk5_BG6mwq5w";
    public final static String KEY = "591b11d0ec3b5b8d7b37d42d37185957";
    public final static String LOG_KEY = "PomodoroTrello";
    public final static String TRELLO_KEY = "trello";
    public final static String ERROR_KEY = "error";

    public final static String MIXPANEL_TOKEN = "d66097529dff5199ae693c71130efc7b";
    public final static String MIXPANEL_USER_ID = "AdvertisingId";
    public final static String MIXPANEL_INSTALL_APP_EVENT = "Install App";
    public final static String MIXPANEL_ACCESSED_CONFIG_EVENT = "Accessed Config Screen";
    public final static String MIXPANEL_ACCESSED_TASKS_EVENT = "Accessed Tasks Screen";
    public final static String MIXPANEL_ACCESSED_POMODORO_EVENT = "Accessed Pomodoro Screen";
    public final static String MIXPANEL_COMPLETED_POMODORO_EVENT = "Completed a Pomodoro";

    public static final String POMODORO_FORMAT = "%02d:%02d:%02d";
    public final static long MILLISECOND = 1000;

    //25 minutes
    public final static long POMODORO_DEFAULT_TIME = 1000 * 60 * 60 * 25;

    //15 minutes
    public final static long LONG_BREAK_DEFAULT_TIME = 1000 * 60 * 60 * 15;

    //5 minutes
    public final static long SHORT_BREAK_DEFAULT_TIME = 1000 * 60 * 60 * 5;

    public final static int TO_DO_ID = 0;
    public final static int DOING_ID = 1;
    public final static int DONE_ID = 2;

}
