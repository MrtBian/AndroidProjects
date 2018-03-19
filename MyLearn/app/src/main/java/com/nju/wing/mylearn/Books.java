package com.nju.wing.mylearn;

import java.util.ArrayList;

/**
 * Created by Wing on 2017/7/28.
 */

public class Books {
    private   ArrayList<CharSequence> bookList;
    Books(){
        bookList = new ArrayList<>();
        bookList.add("CET4");
        bookList.add("CET6");
        bookList.add("TOEFL");
        bookList.add("GRE");
    }
    ArrayList<CharSequence> reBookList(){
        return bookList;
    }
}
