package com.rene.pomodorotrello.dao;

import android.content.Context;
import android.util.Log;

import com.rene.pomodorotrello.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rene on 6/19/16.
 */

public class ObjectStreamHelper {

    private static ObjectStreamHelper objectStreamHelper;

    private ObjectStreamHelper() {
    }

    public static ObjectStreamHelper getInstance() {
        if (objectStreamHelper == null) {
            objectStreamHelper = new ObjectStreamHelper();
        }
        return objectStreamHelper;
    }

    public static final String SELECTED_LISTS_FILE_KEY = "selectedLists";

    public void saveMapObject(Context context, String fileKey, Map map) {

        File outputFile = new File(context.getFilesDir(), fileKey);
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(outputFile));
            outputStream.writeObject(map);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            Log.e(Constants.LOG_KEY, e.getMessage());
        }
    }

    public Map readMapObject(Context context, String fileKey) {

        Map map = null;
        File file = new File(context.getFilesDir(), fileKey);

        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            map = (HashMap) inputStream.readObject();
        } catch (Exception e) {
            Log.e(Constants.LOG_KEY, e.getMessage());
        }
        return map;
    }

}
