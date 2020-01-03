package com.hxd.gobus.utils;


import com.hxd.gobus.bean.Contact;

import java.util.Comparator;

/**
 * Created by pengyu520 on 2016/7/8.
 */
public class PinyinUtil implements Comparator<Contact> {


    @Override
    public int compare(Contact infoOne, Contact infoTwo) {
        if (infoOne.getFirstLetter().equals("@") || infoTwo.getFirstLetter().equals("#")) {
            return -1;
        } else if (infoOne.getFirstLetter().equals("#") || infoTwo.getFirstLetter().equals("@")) {
            return 1;
        } else {
            return infoOne.getFirstLetter().compareTo(infoTwo.getFirstLetter());
        }
    }
}
