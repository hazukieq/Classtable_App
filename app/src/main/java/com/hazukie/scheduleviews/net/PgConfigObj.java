package com.hazukie.scheduleviews.net;

import java.util.ArrayList;
import java.util.List;

public class PgConfigObj {
    String loadState,tile;
    List<String> list;

    /**
     * 网页加载配置文件
     * @param tile 文件加载记录
     * @param list 所有文件记录
     */
    PgConfigObj(String loadState,String tile,List<String> list){
        this.loadState=loadState;
        this.tile=tile;
        this.list=list;
    }

    public static PgConfigObj noneState(){
        return new PgConfigObj("none","（未打开文件）",new ArrayList<>());
    }

    public static PgConfigObj existState(String tile,List<String> lis){
        return new PgConfigObj("exist",tile,lis);
    }
}
