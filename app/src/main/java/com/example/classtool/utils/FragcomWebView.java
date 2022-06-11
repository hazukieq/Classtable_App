package com.example.classtool.utils;



import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ZoomButtonsController;

import com.example.classtool.R;
import com.example.classtool.base.ComWebView;
import com.qmuiteam.qmui.widget.QMUIEmptyView;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragcomWebView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragcomWebView extends Fragment {

    private FrameLayout web_frame;
    private QMUIEmptyView web_error;
    protected ComWebView mWebView;
    private FrameLayout video_frame;

    public static final String EXTRA_URL="EXTRA_URL";

    private String mUrl;

    private boolean mIsPageFinished=false;
    public boolean NEED_CLEAR=false;

    public String open_url="https://voice.baidu.com/act/newpneumonia/newpneumonia";//"https://www.bilibili.com";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragcomWebView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UniversalWebView.
     */
    // TODO: Rename and change types and number of parameters
    public static FragcomWebView newInstance(String param1, String param2) {
       FragcomWebView fragment = new FragcomWebView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.layout_frag_comweb, container, false);
        web_frame=(FrameLayout) root.findViewById(R.id.web_frame);
        video_frame=(FrameLayout) root.findViewById(R.id.web_video_frame);
        web_error=new QMUIEmptyView(getActivity());
        web_error.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        Bundle returns=setArguments();
        if(returns!=null){
            String url=returns.getString(EXTRA_URL);
            if(url!=null&&url.length()>0){
                handleUrl(url);
            }
        }

        initWebView();
        return root;
    }

    //继承方法后，可自由加载网页链接
    public Bundle setArguments(){
        Bundle bun=new Bundle();
        bun.putString(EXTRA_URL,open_url);
        return bun;
    }


    protected void initWebView(){
        mWebView=new ComWebView(getActivity());
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        web_frame.addView(mWebView,params);

        mWebView.setWebChromeClient(getWebViewChromeClient());
        mWebView.setWebViewClient(getWebViewClient());
        //隐藏滚动条
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        mWebView.requestFocus(View.FOCUS_DOWN);
        setZoomControlGone(mWebView);
        configWebView(mWebView);
        mWebView.loadUrl(mUrl);

        onKeyDown();
    }


    private void onKeyDown(){
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i==KeyEvent.KEYCODE_BACK&&mWebView.canGoBack()){
                    if(keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                       // if((QMUIEmptyView) view.findViewById(R.id.web_error)==web_error){
                         //   mWebView.removeView(web_error);
                        //}
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    //可以通过改方法配置网页设置
    protected void configWebView(ComWebView webView){

    }


    private void handleUrl(String url){
        String decodeUrl;
        try{
            decodeUrl= URLDecoder.decode(url,"utf-8");
        }catch (UnsupportedEncodingException e){
            decodeUrl=url;
        }
        mUrl=decodeUrl;
    }

    protected WebChromeClient getWebViewChromeClient(){
        return new FragcomWebChromeClient(this);
    }

    protected WebViewClient getWebViewClient(){
        return new FragcomWebClient();
    }

    @Override
    public void onStop() {
        mWebView.getSettings().setJavaScriptEnabled(false);
        super.onStop();
    }

    @Override
    public void onResume() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        //销毁WebView组件，防止内存泄露
        if(mWebView!=null){
            mWebView.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            web_frame.removeView(mWebView);
            mWebView.destroy();
            mWebView=null;
        }
        super.onDestroy();
    }


    public static void setZoomControlGone(WebView webView){
        webView.getSettings().setDisplayZoomControls(false);
        @SuppressWarnings("rawtypes")
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController zoomButtonsController = new ZoomButtonsController(
                    webView);
            zoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(webView, zoomButtonsController);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static class FragcomWebChromeClient extends WebChromeClient{
        private FragcomWebView mFrag;
        CustomViewCallback customViewCallback;

        public FragcomWebChromeClient(FragcomWebView webFrag){
           mFrag=webFrag;
        }




    }

    protected class FragcomWebClient extends  WebViewClient{
        public FragcomWebClient mWebViewClient;

        public FragcomWebClient(){ }
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            //清空错误代码加载成功后之前保留的后台栈历史
            if(NEED_CLEAR) view.clearHistory();
        }

        //加载网页中出现错误时展示自定义的错误界面
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

            try{
                mWebView.stopLoading();
            }catch (Exception e){
                e.printStackTrace();
            }



            super.onReceivedError(view, request, error);
        }

    }



    private void showError(){
        mWebView.removeAllViews();
        mWebView.addView(web_error);
        web_error.setId(R.id.web_error);
        web_error.show(false, "还没有相关数据哦~", "详情：请检测网络是否正常连接", "点击重试", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.removeAllViews();
                NEED_CLEAR=true;
                mWebView.loadUrl(mUrl);
            }
        });
    }






    }

