package com.rene.pomodorotrello.vo;

import java.util.List;

/**
 * Created by rene on 6/14/16.
 */

public class Board {

    public String id;
    public String name;
    public String desc;
    public List<BoardList> lists;

    /**
     *
     * "id": "4eea4ffc91e31d1746000046",
     "name": "Example Board",
     "desc": "This board is used in the API examples",
     "lists": [{
     "id": "4eea4ffc91e31d174600004a",
     "name": "To Do Soon"
     }, {
     "id": "4eea4ffc91e31d174600004b",
     "name": "Doing"
     }, {
     "id": "4eea4ffc91e31d174600004c",
     "name": "Done"
     }]
     */

}
