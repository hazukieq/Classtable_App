package com.hazukie.scheduleviews.iJBridges;

import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

public class JBridgeInvokeDispatcher {

    private static final class InstanceHolder {
        static final JBridgeInvokeDispatcher instance = new JBridgeInvokeDispatcher();
    }

    public static synchronized JBridgeInvokeDispatcher getInstance() {
        return InstanceHolder.instance;
    }

    public void sendCmd(FragmentActivity context, WebView webView, JBridgeObject jBridgeObject){
        if(checkCmdArgs(jBridgeObject))
            execCmd(context,webView,jBridgeObject);
        else System.out.println("Exception: JBridgeObject is null!");

    }

    private boolean checkCmdArgs(JBridgeObject args){
        //这里需要做参数验证，保证command和params不为空
        if(args!=null){
            String cmd= args.command;
            String prms=args.params;
            return cmd != null && prms != null && cmd.length() > 0 && prms.length() > 0;
        }else return false;
    }

    private void execCmd(FragmentActivity context, WebView webView, JBridgeObject jBridgeObject){
        JBridgeCmdHandler.getInstance().handleBridgeInvoke(context,webView,jBridgeObject.command, jBridgeObject.params);
    }
}
