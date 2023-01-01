package com.hazukie.scheduleviews.net;

import java.util.List;

public class MsgPasser {
    String cmd;
    List<String> args;

    public MsgPasser(String cmd,List<String> args){
        this.cmd=cmd;
        this.args=args;
    }
}
