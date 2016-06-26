package com.rene.pomodorotrello.tracker;

import android.os.AsyncTask;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import static com.rene.pomodorotrello.application.PomodoroTrelloApplication.getContext;

/**
 * Created by rene on 6/25/16.
 */

public class MixpanelDelegate {

    private static MixpanelAPI mixpanelInstance;
    private static boolean isEnabled = false;

    public static void init() {
        mixpanelInstance = MixpanelAPI.getInstance(getContext(), Constants.MIXPANEL_TOKEN);
        isEnabled = mixpanelInstance != null;

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance();
        if (sharedPreferencesHelper.getValue(SharedPreferencesHelper.FIRST_RUN_KEY) == null) {
            sharedPreferencesHelper.saveValue(SharedPreferencesHelper.FIRST_RUN_KEY,
                    SharedPreferencesHelper.FIRST_RUN_FALSE_VALUE);

            getAdvertisingIdClient();
        }
    }

    private static void logInstall(String advertisingId) {
        if (isEnabled) {
            JSONObject properties = new JSONObject();
            try {
                //To track the user for this sample app I am using the Android ID
                //Ideally, we would be generating an ID in our server or using a combination of attributes
                properties.put(Constants.MIXPANEL_USER_ID, advertisingId);
            } catch (JSONException e) {
                //In the future we would be logging this in Fabric or Firebase
            }
            mixpanelInstance.registerSuperPropertiesOnce(properties);
            track(Constants.MIXPANEL_INSTALL_APP_EVENT, null);
        }
    }

    public static void track(String eventName, Map<String, String> params) {
        if (isEnabled) {
            try {
                JSONObject properties = null;
                if (params != null) {
                    properties = new JSONObject(params);
                }
                mixpanelInstance.track(eventName, properties);
            } catch (Exception e) {
                //In the future we would be logging this in Crashlytics
            }
        }
    }

    public static void flush() {
        if (isEnabled) {
            mixpanelInstance.flush();
        }
    }

    private static void getAdvertisingIdClient() {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String advertisingId = null;

                if (idInfo != null) {
                    advertisingId = idInfo.getId();
                }

                return advertisingId;
            }

            @Override
            protected void onPostExecute(String advertisingId) {
                logInstall(advertisingId);
            }

        };
        task.execute();
    }

}
