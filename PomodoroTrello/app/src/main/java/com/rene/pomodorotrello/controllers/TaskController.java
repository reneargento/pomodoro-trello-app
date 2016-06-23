package com.rene.pomodorotrello.controllers;

import android.content.Context;

import com.rene.pomodorotrello.dao.ObjectStreamHelper;
import com.rene.pomodorotrello.dao.SharedPreferencesHelper;
import com.rene.pomodorotrello.interfaces.ItemRetriever;
import com.rene.pomodorotrello.interfaces.TrelloAPI;
import com.rene.pomodorotrello.util.Constants;
import com.rene.pomodorotrello.vo.Card;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by rene on 6/19/16.
 */

public class TaskController extends TrelloObjectController {

    public void getCardsFromList(Context context, final ItemRetriever itemRetriever, int listId) {
        switch (listId) {
            case Constants.TO_DO_ID:
                getToDoCards(context, itemRetriever);
                break;
            case Constants.DOING_ID:
                getDoingCards(context, itemRetriever);
                break;
            case Constants.DONE_ID:
                getDoneCards(context, itemRetriever);
                break;
        }
    }

    private void getToDoCards(Context context, final ItemRetriever itemRetriever) {

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        String toDoListName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_TODO_LIST_KEY);

        if (toDoListName != null) {
            getCards(context, itemRetriever, toDoListName);
        }
    }

    private void getDoingCards(Context context, final ItemRetriever itemRetriever) {

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        String doingListName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_DOING_LIST_KEY);

        if (doingListName != null) {
            getCards(context, itemRetriever, doingListName);
        }
    }

    private void getDoneCards(Context context, final ItemRetriever itemRetriever) {

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        String doneListName = sharedPreferencesHelper.getValue(SharedPreferencesHelper.SELECTED_DONE_LIST_KEY);

        if (doneListName != null) {
            getCards(context, itemRetriever, doneListName);
        }
    }

    @SuppressWarnings("unchecked")
    private void getCards(Context context, final ItemRetriever itemRetriever, String listName) {

        if (listName != null) {
            ObjectStreamHelper objectStreamHelper = ObjectStreamHelper.getInstance();
            Map<String, String> selectedLists = (Map) objectStreamHelper.readMapObject(context,
                    ObjectStreamHelper.SELECTED_LISTS_FILE_KEY);

            String listId = selectedLists.get(listName);

            if (listId != null) {

                Retrofit retrofit = RetrofitController.getInstance();

                String token = SharedPreferencesHelper.getInstance(context).getValue(SharedPreferencesHelper.TOKEN_KEY);

                if (token != null) {
                    TrelloAPI trelloAPI = retrofit.create(TrelloAPI.class);

                    Call<List<Card>> cardListCall = trelloAPI.getListCards(listId, Constants.KEY, token);

                    cardListCall.enqueue(new Callback<List<Card>>() {
                        @Override
                        public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                            List<Card> cardList = response.body();

                            itemRetriever.retrieveItems(cardList);
                        }

                        @Override
                        public void onFailure(Call<List<Card>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }

            }
        }
    }

    //Requires WRITE permission -> makes an API call to move a task from one board to another
    // PUT /1/cards/[card id]/idList
    public void moveTask(int fromBoard, int toBoard) {

    }

    //Requires WRITE permission -> makes an API call to add a comment to a task
    //POST /1/cards/[card id or shortlink]/actions/comments
    public void addCommentToTask(String cardId, String comment) {

    }

    public String generateCommentForFinishedTask(int pomodoros, String totalTimeSpentString) {
        int firstColonIndex = totalTimeSpentString.indexOf(":");
        int startIndex = 0;
        if (totalTimeSpentString.charAt(0) == '0') {
            startIndex = 1;
        }

        String formattedHour = totalTimeSpentString.substring(startIndex,firstColonIndex)
                + "h" + totalTimeSpentString.substring(firstColonIndex);

        return pomodoros + " pomodoros, " + formattedHour + " total spent";
    }

}
