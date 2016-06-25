package com.rene.pomodorotrello.interfaces;

import com.rene.pomodorotrello.vo.Card;

import java.util.List;

/**
 * Created by rene on 6/24/16.
 */

public interface GetCardsCallback {

    void onSuccessGetCards(List<Card> cardsList);
    void onFailureGetCards();

}
