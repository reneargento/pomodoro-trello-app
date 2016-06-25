package com.rene.pomodorotrello.ui.tasks;

import com.rene.pomodorotrello.vo.Card;

import java.util.List;

/**
 * Created by rene on 6/24/16.
 */

interface TasksFragmentView {

    void setWarningTextViewConnectText();
    void setWarningTextViewVisible();
    void setWarningTextGravityCenter();
    void setListRecyclerViewVisibilityGone();
    void setListCard(List<Card> cardList);

}