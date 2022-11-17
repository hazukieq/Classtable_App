package com.hazukie.scheduleviews.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.custom.CnWebView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebFrag extends Fragment {
    private CnWebView mWebView;
    private String mUrl="";
    private FrameLayout web_frame;
    public String open_url="";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public WebFrag() {
        // Required empty public constructor
    }

    public WebFrag(String str){
        this.open_url=str;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WebFrag.
     */

    public static WebFrag newInstance(String param1, String param2) {
        WebFrag fragment = new WebFrag();
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
        View root= inflater.inflate(R.layout.fragment_web, container, false);
        web_frame=root.findViewById(R.id.web_frag_frame);
        if(open_url!=null&&open_url.length()>0) handleUrl(open_url);
        initWebView();
        return root;
    }

    protected void initWebView(){
        mWebView=new CnWebView(getActivity());
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        web_frame.addView(mWebView,params);

        mWebView.setWebChromeClient(getWebViewChromeClient());
        mWebView.setWebViewClient(getWebViewClient());
        //隐藏滚动条
        //mWebView.setVerticalScrollBarEnabled(false);
        mWebView.requestFocus(View.FOCUS_DOWN);
        //setZoomControlGone(  mWebView);
        configWebView(mWebView);
        mWebView.loadUrl(mUrl);
    }


    private void onKeyDown(){
        mWebView.setOnKeyListener((view, i, keyEvent) -> {
            if(i== KeyEvent.KEYCODE_BACK&&mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }
            return false;
        });
    }

    //可以通过改方法配置网页设置
    protected void configWebView(CnWebView webView){

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
        return new UniwebChromeClient(this);
    }

    protected WebViewClient getWebViewClient(){
        return new UniwebClient();
    }

    @Override
    public void onStop() {
        if(mWebView!=null) mWebView.getSettings().setJavaScriptEnabled(false);
        super.onStop();
    }

    @Override
    public void onResume() {
        if(mWebView!=null) mWebView.getSettings().setJavaScriptEnabled(true);
        super.onResume();
    }

    /*@Override
    public void onDestroy() {
        //销毁WebView组件，防止内存泄露
        if(mWebView!=null){
            mWebView.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            web_frame.removeView(mWebView);
            mWebView.destroy();
            mWebView=null;
        }
        super.onDestroy();
    }*/



    public static class UniwebChromeClient extends WebChromeClient {
        private WebFrag mFrag;

        public UniwebChromeClient(WebFrag webFrag){
            mFrag=webFrag;
        }
    }

    protected class UniwebClient extends WebViewClient {

        public UniwebClient(){ }
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            //清空错误代码加载成功后之前保留的后台栈历史
            view.clearHistory();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(!view.getSettings().getLoadsImagesAutomatically()){
                view.getSettings().setLoadsImagesAutomatically(true);
            }
        }

    }
}