package com.hxd.gobus.bean.list;

import com.hxd.gobus.bean.Todo;

import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/8/23 9:41
 */

public class TodoList {

    private int total;

    private List<Todo> untreatedInfos;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Todo> getUntreatedInfos() {
        return untreatedInfos;
    }

    public void setUntreatedInfos(List<Todo> untreatedInfos) {
        this.untreatedInfos = untreatedInfos;
    }
}
