package com.hazukie.cskheui.Crialoghue;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hazukie.cskheui.LetxtView.LetxtView;
import com.hazukie.cskheui.R;

public class Crialoghue extends Dialog {
    private static final String TAG ="CskehUi-Criloghue_logged>>" ;
    private Button confirmBtn,cancelBtn;
    private LinearLayout pnnn;
    private TextView mTitleView;
    private String mTitle="";
    private int dialogFontSize=14;
    private int mGravity= Gravity.CENTER;
    private LinearLayout rootLay;

    public Crialoghue(@NonNull Context context) {
        super(context);
        inits();
    }

    public Crialoghue(@NonNull Context context, ImgBuilder imgBuilder) {
        this(context);
        initImgbuilder(context,imgBuilder);
    }

    public Crialoghue(@NonNull Context context, HeditBuilder heditBuilder) {
        this(context);
        initHeditbuilder(heditBuilder);
    }

    public Crialoghue(@NonNull Context context, LeditBuilder leditBuilder) {
        this(context);
        initLeditbuilder(leditBuilder);
    }

    public Crialoghue(@NonNull Context context, TxtBuilder txtBuilder) {
        this(context);
        initTxtbuilder(txtBuilder);
    }

    public Crialoghue(@NonNull Context context, CustomBuilder customBuilder) {
        this(context);
        initCustombuilder(customBuilder);
    }

    public Crialoghue(@NonNull Context context, LetxtBuilder letxtBuilder) {
        this(context);
        initLetxtbuilder(letxtBuilder);
    }



    public Crialoghue(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        inits();
    }

    protected Crialoghue(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        inits();
    }


    public Crialoghue setPnANnVisis(boolean isVisi){
        if(isVisi){
           if(pnnn!=null)pnnn.setVisibility(View.GONE);
        }else{
            if(pnnn != null)pnnn.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public Crialoghue setFontSize(int size){
        this.dialogFontSize=size;
        return this;
    }



    private void inits(){
        setContentView(R.layout.__crialogue_root);
        getWindow().setDimAmount(0.16f);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmBtn=findViewById(R.id.dialog_cofirm);
        cancelBtn=findViewById(R.id.dialog_cancel);
        mTitleView=findViewById(R.id.dialog_ext);
        rootLay=findViewById(R.id.diaglog_linearLay);
        pnnn=findViewById(R.id.crialoh_root_pnnn);
        mTitleView.setTextSize(dialogFontSize);

    }

    public void addView(View view){
        rootLay.addView(view,1);
    }



    private void initImgbuilder(Context context,ImgBuilder imgBuilder){
        if(imgBuilder.scrollView!=null&&imgBuilder.scrollView.getChildAt(0)!=null){
            ImageView imgView=(ImageView) imgBuilder.scrollView.getChildAt(0);
            addView(imgBuilder.scrollView);
            if(imgBuilder.scaleMode!=null)imgView.setScaleType(imgBuilder.scaleMode);
            if(imgBuilder.backgroundColor!=0) imgView.setBackgroundColor(imgBuilder.backgroundColor);

            if(imgBuilder.drawableId!=0||imgBuilder.drawable!=null||imgBuilder.bitmap!=null){
                if(imgBuilder.drawableId!=0) imgView.setImageDrawable(context.getDrawable(imgBuilder.drawableId));
                else if(imgBuilder.drawable!=null) imgView.setImageDrawable(imgBuilder.drawable);
                else if(imgBuilder.bitmap!=null) imgView.setImageBitmap(imgBuilder.bitmap);
            }

            mTitleView.setText(imgBuilder.title);
            getWindow().setGravity(imgBuilder.dialoglocation);

            if(imgBuilder.interceptView!=null) imgBuilder.interceptView.doIntercept(this,imgView);

            confirmBtn.setOnClickListener(v22->{
                if(imgBuilder.confirmListener!=null)imgBuilder.confirmListener.doConfirm(this,imgView);
                else dismiss();
            });

            cancelBtn.setOnClickListener(v33->{
                if(imgBuilder.cancelListener!=null)imgBuilder.cancelListener.doCancel(this,imgView);
                else dismiss();
            });

        }

    }

    private void initHeditbuilder(HeditBuilder heditBuilder){
        if(heditBuilder.attachedView!=null&&heditBuilder.attachedView.getChildAt(0)!=null){
            addView(heditBuilder.attachedView);

            EditText edit= (EditText) heditBuilder.attachedView.getChildAt(0);
            TextView infos= (TextView) heditBuilder.attachedView.getChildAt(1);

            edit.setHint(heditBuilder.hintext);
            edit.setText(heditBuilder.content);

            if(heditBuilder.isMultiEdit){
                edit.setMaxLines(40);
                edit.setSingleLine(false);
            }
            else {
                edit.setMaxLines(1);
                edit.setSingleLine(true);
                //edit.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            if(heditBuilder.isHtmlMode) infos.setText(Html.fromHtml(heditBuilder.bottomContent));
            else infos.setText(heditBuilder.bottomContent);

            if(heditBuilder.isBottomVisible) infos.setVisibility(View.VISIBLE);
            else infos.setVisibility(View.GONE);

            mTitleView.setText(heditBuilder.title);
            getWindow().setGravity(heditBuilder.dialoglocation);

            if(heditBuilder.interceptViewz!=null)heditBuilder.interceptViewz.doIntercept(this,heditBuilder.attachedView);

            confirmBtn.setOnClickListener(v22->{
                if(heditBuilder.confirmListener!=null)heditBuilder.confirmListener.doConfirm(this,edit);
                else dismiss();
            });

            cancelBtn.setOnClickListener(v33->{
                if(heditBuilder.cancelListener!=null)heditBuilder.cancelListener.doCancel(this,edit);
                else dismiss();
            });
        }
    }

    private void initLeditbuilder(LeditBuilder leditBuilder){
        if(leditBuilder.attachedView!=null){
            addView(leditBuilder.attachedView);
            EditText edit=leditBuilder.attachedView.findViewById(R.id.__crialoh_leditlay_ledit);
            TextView infos=leditBuilder.attachedView.findViewById(R.id.__crialoh_leditlay_linfos);
            TextView leftTile=leditBuilder.attachedView.findViewById(R.id.__crialoh_leditlay_leside);

            edit.setText(leditBuilder.content);
            leftTile.setText(leditBuilder.leftTile);

            if(leditBuilder.isHtmlMode) infos.setText(Html.fromHtml(leditBuilder.bottomContent));
            else infos.setText(leditBuilder.bottomContent);

            if(leditBuilder.isBottomVisible) infos.setVisibility(View.VISIBLE);
            else infos.setVisibility(View.GONE);

            mTitleView.setText(leditBuilder.title);
            getWindow().setGravity(leditBuilder.dialoglocation);

            if(leditBuilder.interceptViewz!=null)leditBuilder.interceptViewz.doIntercept(this,leditBuilder.attachedView);

            confirmBtn.setOnClickListener(v22->{
                if(leditBuilder.confirmListener!=null)leditBuilder.confirmListener.doConfirm(this,edit);
                else dismiss();
            });

            cancelBtn.setOnClickListener(v33->{
                if(leditBuilder.cancelListener!=null)leditBuilder.cancelListener.doCancel(this,edit);
                else dismiss();
            });
        }
    }

    private void initTxtbuilder(TxtBuilder txtBuilder){
        if(txtBuilder.defaultTxt!=null){
            addView(txtBuilder.defaultTxt);

            if(!txtBuilder.isHtmlMode){
                txtBuilder.defaultTxt.setText(txtBuilder.contents);
            }
            else{
                txtBuilder.defaultTxt.setText(Html.fromHtml(txtBuilder.contents));
            }

            if(txtBuilder.fontGravity!=0)txtBuilder.defaultTxt.setGravity(txtBuilder.fontGravity);
            if(txtBuilder.fontColor!=0)txtBuilder.defaultTxt.setTextColor(txtBuilder.fontColor);
            if(txtBuilder.fontSize!=0)txtBuilder.defaultTxt.setTextSize(txtBuilder.fontSize);
            if(txtBuilder.fontPadding!=null&&txtBuilder.fontPadding.length==4) txtBuilder.defaultTxt.setPadding(txtBuilder.fontPadding[0],txtBuilder.fontPadding[1],txtBuilder.fontPadding[2],txtBuilder.fontPadding[3]);

            mTitleView.setText(txtBuilder.title);
            getWindow().setGravity(txtBuilder.dialoglocation);

            if(txtBuilder.interceptView!=null)txtBuilder.interceptView.doIntercept(this,txtBuilder.defaultTxt);

            confirmBtn.setOnClickListener(v22->{
                if(txtBuilder.confirmListener!=null)txtBuilder.confirmListener.doConfirm(this,txtBuilder.defaultTxt);
                else dismiss();
            });

            cancelBtn.setOnClickListener(v33->{
                if(txtBuilder.cancelListener!=null)txtBuilder.cancelListener.doCancel(this,txtBuilder.defaultTxt);
                else dismiss();
            });


        }
    }

    private void initCustombuilder(CustomBuilder customBuilder){
        if(customBuilder.attachedView!=null){
            addView(customBuilder.attachedView);
            if(customBuilder.interceptViewz!=null)customBuilder.interceptViewz.doIntercept(this,customBuilder.attachedView);

            mTitleView.setText(customBuilder.title);
            getWindow().setGravity(customBuilder.dialoglocation);

            confirmBtn.setOnClickListener(v22->{
                if(customBuilder.confirmListenerz!=null)customBuilder.confirmListenerz.doConfirm(this,customBuilder.attachedView);
                else dismiss();
            });

            cancelBtn.setOnClickListener(v33->{
                if(customBuilder.cancelListenerz!=null)customBuilder.cancelListenerz.doCancel(this,customBuilder.attachedView);
                else dismiss();
            });
        }

    }

    private void initLetxtbuilder(LetxtBuilder letxtBuilder) {
        LetxtView letxtView=new LetxtView.Builder()
                .addLeftile(letxtBuilder.leftile)
                .addRightile(letxtBuilder.rightext)
                .create(getContext());
        addView(letxtView);

        if(letxtBuilder.interceptViewz!=null) letxtBuilder.interceptViewz.doIntercept(this,letxtView);

        mTitleView.setText(letxtBuilder.title);
        getWindow().setGravity(letxtBuilder.dialoglocation);

        confirmBtn.setOnClickListener(v22->{
            if(letxtBuilder.confirmListenerz!=null)letxtBuilder.confirmListenerz.doConfirm(this,letxtView);
            else dismiss();
        });

        cancelBtn.setOnClickListener(v33->{
            if(letxtBuilder.cancelListenerz!=null)letxtBuilder.cancelListenerz.doCancel(this,letxtView);
            else dismiss();
        });
    }

    public static class TxtBuilder {
        private String title="";
        private int fontSize=0,fontColor= 0,fontGravity=0;
        private int[] fontPadding=new int[]{24,8,24,8};
        private int dialoglocation=Gravity.CENTER;
        private Clicks.ConfirmListener confirmListener;
        private Clicks.CancelListener cancelListener;
        private Clicks.InterceptView interceptView;

        private String contents="";
        private TextView defaultTxt;
        private boolean isHtmlMode=false;

        static TextView defaultText(Context context){
            TextView default_txt=new TextView(context);
            default_txt.setPadding(24,8,24,8);
            default_txt.setGravity(Gravity.LEFT);
            default_txt.setTextSize(16);
            default_txt.setTextColor(context.getResources().getColor(R.color.__text_gray));
            return default_txt;
        }



        public TxtBuilder addTitle(String title){
            this.title=title;
            return this;
        }

        public TxtBuilder addContent(String content){
            this.contents=content;
            return this;
        }

        public TxtBuilder addInterceptor(Clicks.InterceptView interceptView){
            this.interceptView=interceptView;
            return this;
        }

        public TxtBuilder addFontGravity(int fontGravity){
            this.fontGravity=fontGravity;
            return this;
        }

        /**
         *
         * @param fontPadding [left,top,right,bottom]
         * @return int[]
         */
        public TxtBuilder addFontPadding(int[] fontPadding){
            this.fontPadding=fontPadding;
            return this;
        }

        public TxtBuilder addFontColor(int fontColor){
            this.fontColor=fontColor;
            return this;
        }

        public TxtBuilder addFontSize(int fontSize){
            this.fontSize=fontSize;
            return this;
        }

        public TxtBuilder addDialogGravity(int DialogGravity){
            this.dialoglocation=DialogGravity;
            return this;
        }

        public TxtBuilder onConfirm(Clicks.ConfirmListener confirmListener){
            this.confirmListener=confirmListener;
            return this;
        }

        public TxtBuilder onCancel(Clicks.CancelListener cancelListener){
            this.cancelListener=cancelListener;
            return this;
        }

        public TxtBuilder addHtmlMode(boolean isHtmlMode){
            this.isHtmlMode=isHtmlMode;
            return this;
        }

        public Crialoghue build(Context context){
            defaultTxt=defaultText(context);
            return new Crialoghue(context,this);
        }
    }

    public static class ImgBuilder {
        private String title="";
        private int dialoglocation= Gravity.CENTER;
        private int drawableId=0;
        private Bitmap bitmap;
        private Drawable drawable;
        private ImageView.ScaleType scaleMode;
        private int backgroundColor=0;

        private Clicks.ConfirmListener confirmListener;
        private Clicks.CancelListener cancelListener;
        private Clicks.InterceptView interceptView;
        private ScrollView scrollView;

        static ScrollView getImgView(Context context){
            ScrollView scrollView=new ScrollView(context);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
            params.weight=3;
            scrollView.setBackgroundColor(context.getResources().getColor(R.color.__dim_bg));
            ImageView img=new ImageView(context);
            img.setPadding(12,12,12,12);
            scrollView.addView(img,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            img.setAdjustViewBounds(true);
            return scrollView;
        }

        public ImgBuilder addDrawableSrc(int drawableId){
            this.drawableId=drawableId;
            return this;
        }

        public ImgBuilder addDrawableSrc(Drawable drawable){
            this.drawable=drawable;
            return this;
        }

        public ImgBuilder addDrawableSrc(Bitmap bitmap){
            this.bitmap=bitmap;
            return this;
        }


        public ImgBuilder addImgBackground(int backgroundColor){
            this.backgroundColor=backgroundColor;
            return this;
        }

        public ImgBuilder addImgScaleMode(ImageView.ScaleType mode){
            this.scaleMode=mode;
            return this;
        }

        public ImgBuilder addTitle(String title){
            this.title=title;
            return this;
        }

        public ImgBuilder addDialogGravity(int location){
            this.dialoglocation=location;
            return this;
        }

        public ImgBuilder addInterceptor(Clicks.InterceptView interceptView){
            this.interceptView=interceptView;
            return this;
        }
        public ImgBuilder onConfirm(Clicks.ConfirmListener confirmListener){
            this.confirmListener=confirmListener;
            return this;
        }

        public ImgBuilder onCancel(Clicks.CancelListener cancelListener){
            this.cancelListener=cancelListener;
            return this;
        }
        public Crialoghue build(Context context){
            this.scrollView=getImgView(context);
            return new Crialoghue(context,this);
        }
    }


    public static class HeditBuilder {
        private String title="";
        private int dialoglocation= Gravity.CENTER;
        private String hintext="";
        private String content="";
        private boolean isHtmlMode=false;
        private boolean isBottomVisible=false;
        private boolean isMultiEdit=false;
        private String bottomContent="";

        private ViewGroup attachedView;
        private Clicks.InterceptViewz interceptViewz;
        private Clicks.ConfirmListener confirmListener;
        private Clicks.CancelListener cancelListener;



        public HeditBuilder addTitle(String title){
            this.title=title;
            return this;
        }

        public HeditBuilder addDialogGravity(int location){
            this.dialoglocation=location;
            return this;
        }

        public HeditBuilder addHint(String hint){
            this.hintext=hint;
            return this;
        }


        public HeditBuilder addContents(String str){
            this.content=str;
            return this;
        }

        public HeditBuilder addIsHtmlMode(boolean isHtmlMode){
            this.isHtmlMode=isHtmlMode;
            return this;
        }

        public HeditBuilder addIsMultiEdit(boolean isMultiEdit){
            this.isMultiEdit=isMultiEdit;
            return this;
        }

        public HeditBuilder addIsBottomVisible(boolean isBottomVisible){
            this.isBottomVisible=isBottomVisible;
            return this;
        }

        public HeditBuilder addBottomContents(String str){
            this.bottomContent=str;
            return this;
        }

        public HeditBuilder addInterceptor(Clicks.InterceptViewz interceptViewz){
            this.interceptViewz=interceptViewz;
            return this;
        }

        public HeditBuilder onConfirm(Clicks.ConfirmListener confirmListener){
            this.confirmListener=confirmListener;
            return this;
        }

        public HeditBuilder onCancel(Clicks.CancelListener cancelListener){
            this.cancelListener=cancelListener;
            return this;
        }

        public Crialoghue build(Context context){
            this.attachedView=Clicks.getView(context,R.layout.__crialoh_editlay);
            return new Crialoghue(context,this);
        }
    }


    public static class LeditBuilder {
        private String title="";
        private int dialoglocation= Gravity.CENTER;
        private String hintext="";
        private String content="";
        private boolean isHtmlMode=false;
        private boolean isBottomVisible=false;
        private String bottomContent="";
        private String leftTile="";

        private ViewGroup attachedView;
        private Clicks.InterceptViewz interceptViewz;
        private Clicks.ConfirmListener confirmListener;
        private Clicks.CancelListener cancelListener;



        public LeditBuilder addTitle(String title){
            this.title=title;
            return this;
        }

        public LeditBuilder addDialogGravity(int location){
            this.dialoglocation=location;
            return this;
        }

        public LeditBuilder addHint(String hint){
            this.hintext=hint;
            return this;
        }


        public LeditBuilder addContents(String str){
            this.content=str;
            return this;
        }

        public LeditBuilder addIsHtmlMode(boolean isHtmlMode){
            this.isHtmlMode=isHtmlMode;
            return this;
        }

        public LeditBuilder addIsBottomVisible(boolean isBottomVisible){
            this.isBottomVisible=isBottomVisible;
            return this;
        }

        public LeditBuilder addBottomContents(String str){
            this.bottomContent=str;
            return this;
        }

        public LeditBuilder addLeftContent(String str){
            this.leftTile=str;
            return this;
        }

        public LeditBuilder addInterceptor(Clicks.InterceptViewz interceptViewz){
            this.interceptViewz=interceptViewz;
            return this;
        }

        public LeditBuilder onConfirm(Clicks.ConfirmListener confirmListener){
            this.confirmListener=confirmListener;
            return this;
        }

        public LeditBuilder onCancel(Clicks.CancelListener cancelListener){
            this.cancelListener=cancelListener;
            return this;
        }

        public Crialoghue build(Context context){
            this.attachedView=Clicks.getView(context, R.layout.__crialoh_ledit_lay);
            return new Crialoghue(context,this);
        }
    }

    public static class CustomBuilder {
        private String title="";
        private int dialoglocation= Gravity.CENTER;

        private ViewGroup attachedView;
        private Clicks.InterceptViewz interceptViewz;
        private Clicks.ConfirmListenerz confirmListenerz;
        private Clicks.CancelListenerz cancelListenerz;


        public CustomBuilder addTitle(String title){
            this.title=title;
            return this;
        }

        public CustomBuilder addDialogGravity(int gravity){
            this.dialoglocation=gravity;
            return this;
        }

        public CustomBuilder addView(ViewGroup viewGroup){
            if(viewGroup!=null) this.attachedView=viewGroup;
            return this;
        }

        public CustomBuilder addView(Context context, int rootId, ViewGroup.LayoutParams lps){
            this.attachedView=Clicks.getView(context,rootId);
            if(lps!=null)this.attachedView.setLayoutParams(lps);
            return this;
        }

        public CustomBuilder addInterceptorz(Clicks.InterceptViewz interceptViewz){
            this.interceptViewz=interceptViewz;
            return this;
        }

        public CustomBuilder onConfirm(Clicks.ConfirmListenerz confirmListenerz){
            this.confirmListenerz=confirmListenerz;
            return this;
        }

        public CustomBuilder onCancel(Clicks.CancelListenerz cancelListenerz){
            this.cancelListenerz=cancelListenerz;
            return this;
        }

        public Crialoghue build(Context context){
            return new Crialoghue(context,this);
        }
    }

    public static class LetxtBuilder{
        private String leftile,rightext;
        private int dialoglocation= Gravity.CENTER;
        private String title="";
        private Clicks.InterceptViewz interceptViewz;
        private Clicks.ConfirmListenerz confirmListenerz;
        private Clicks.CancelListenerz cancelListenerz;


        public LetxtBuilder addTitle(String title){
            this.title=title;
            return this;
        }

        public LetxtBuilder addLeftile(String leftitle){
            this.leftile=leftitle;
            return this;
        }

        public LetxtBuilder addRightext(String rightext){
            this.rightext=rightext;
            return this;
        }

        public LetxtBuilder addInterceptorz(Clicks.InterceptViewz interceptViewz){
            this.interceptViewz=interceptViewz;
            return this;
        }

        public LetxtBuilder onConfirm(Clicks.ConfirmListenerz confirmListenerz){
            this.confirmListenerz=confirmListenerz;
            return this;
        }

        public LetxtBuilder onCancel(Clicks.CancelListenerz cancelListenerz){
            this.cancelListenerz=cancelListenerz;
            return this;
        }

        public Crialoghue build(Context context){
            return new Crialoghue(context,this);
        }
    }

}
