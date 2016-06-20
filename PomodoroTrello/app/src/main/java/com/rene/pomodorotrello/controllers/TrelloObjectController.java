package com.rene.pomodorotrello.controllers;

import com.rene.pomodorotrello.vo.TrelloObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rene on 6/20/16.
 */

public class TrelloObjectController {

    public List<String> getNamesFromList(List<? extends TrelloObject> objectList) {

        List<String> names = new ArrayList<>();

        for (TrelloObject object : objectList) {
            names.add(object.name);
        }

        return names;
    }

}
