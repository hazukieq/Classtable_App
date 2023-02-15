package com.hazukie.scheduleviews.iJBridges;

import com.hazukie.scheduleviews.net.FileIvoker;
import com.hazukie.scheduleviews.net.Infost;
import com.hazukie.scheduleviews.net.Vert2Pdf;
import com.hazukie.scheduleviews.net.Vert2Png;


public class IJBridgeUtil {

    //auto register variants here, do not change any code !
    public static void init(){
        JBridgeCmdHandler jBridgeCmdHandler=JBridgeCmdHandler.getInstance();
        IJBridgeCmd Vert2Pdf$$ijbridgeCmdApt= new Vert2Pdf();
        jBridgeCmdHandler.registerCmd("vert2Pdf",Vert2Pdf$$ijbridgeCmdApt);



        IJBridgeCmd Vert2Png$$ijbridgeCmdApt= new Vert2Png();
        jBridgeCmdHandler.registerCmd("vert2Png",Vert2Png$$ijbridgeCmdApt);


        IJBridgeCmd FileIvoker$$ijbridgeCmdApt= new FileIvoker();
        jBridgeCmdHandler.registerCmd("fileInvoker",FileIvoker$$ijbridgeCmdApt);

        IJBridgeCmd Infost$$ijbridgeCmdApt= new Infost();
        jBridgeCmdHandler.registerCmd("infost",Infost$$ijbridgeCmdApt);
    }

}
