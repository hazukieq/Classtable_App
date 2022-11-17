package com.hazukie.scheduleviews.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.utils.DisplayHelper;

public class TopbarLayout extends RelativeLayout {
    private ImageView top_root_left;
    private TextView top_root_title;
    private RelativeLayout top_root;
    //左边View绑定ID
    private final int resId=1;
    //右边View绑定ID
    private final int riId=2;
    private int mTitleColor=getResources().getColor(R.color.text_gray);
    private int mBgColor=getResources().getColor(R.color.light_white);
    private String mTitle="";
    private int mSize=16;
    private int mTint=getResources().getColor(R.color.white);

    public TopbarLayout(Context context) {
        super(context);
        initView(context);
    }

    public TopbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.TopbarLayout);
        mBgColor=typedArray.getColor(R.styleable.TopbarLayout_topbar_background_color,getResources().getColor(R.color.light_white));
        mTitle=typedArray.getString(R.styleable.TopbarLayout_title_text);
        mTitleColor=typedArray.getColor(R.styleable.TopbarLayout_title_text_color,getResources().getColor(R.color.text_gray));
        mSize=typedArray.getDimensionPixelSize(R.styleable.TopbarLayout_title_text_size,18);
        mTint=typedArray.getColor(R.styleable.TopbarLayout_topbar_icon_tint,getResources().getColor(R.color.light_white));
        typedArray.recycle();
        initView(context);
    }

    public TopbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.topbarayout,this,true);

        top_root_title=findViewById(R.id.topbarayout_root_title);
        top_root=findViewById(R.id.topbarayout_root);

        //由于setTextSize显示的字体大小比xml的数值偏大，所以需要进行修正
        top_root_title.getPaint().setTextSize(mSize);
        top_root_title.setTextColor(mTitleColor);
        top_root_title.setText(mTitle);
        top_root.setBackgroundColor(mBgColor);
    }


    /**
     * @param title 设置标题
     */
    public TopbarLayout setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            top_root_title.setText(title);
        }
        return this;
    }

    public String getTitle(){
        return top_root_title.getText().toString();
    }

    /**
     * @param color 设置标题字体颜色
     */
    public TopbarLayout setTitleColor(int color){
        top_root_title.setTextColor(color);
        return this;
    }

    /**
     * @param size 设置标题字体大小
     */
    public TopbarLayout setTitleSize(int size){
        top_root_title.setTextSize(size);
        return this;
    }


   public TopbarLayout addLeftImageBackButton( OnClickListener onClickListener){
        if(checkView(resId)) {
            top_root_left = new ImageView(getContext());
            top_root_left.setId(resId);
            RelativeLayout.LayoutParams lps = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lps.addRule(RelativeLayout.ALIGN_PARENT_START);
            lps.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            int wid = DisplayHelper.dp2px(getContext(), 15);
            top_root_left.setPadding(wid, 0, wid, 0);

            top_root_left.setImageResource(R.drawable.ic_action_back);

            //过滤图标原来色彩，适应不同背景颜色
            top_root_left.setColorFilter(mTint);

            top_root_left.setOnClickListener(onClickListener);

            addView(top_root_left, lps);
        }
        return this;
   }

   public void addLftTextView(String str,OnClickListener onClickListener){
        addLeftTextView(str,getContext().getColor(R.color.text_gray),15,onClickListener);
   }

   public TopbarLayout addLeftTextView(String str,int color,int textSize,OnClickListener onClickListener){
        if(checkView(resId)){
            TextView textView=new TextView(getContext());
            RelativeLayout.LayoutParams lps = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lps.addRule(RelativeLayout.ALIGN_PARENT_START);
            lps.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            textView.setText(str);
            textView.setTextColor(color);
            textView.setTextSize(textSize);
            textView.setGravity(Gravity.CENTER_VERTICAL);

            int wid = DisplayHelper.dp2px(getContext(), 15);
            textView.setPadding(wid,0,wid,0);

            textView.setOnClickListener(onClickListener);
            textView.setId(resId);
            addView(textView,lps);
        }
        return this;
   }

   public TopbarLayout addRightImageBackButton(int icon,int iconTint,OnClickListener onClickListener){
        if(checkView(riId)){
            ImageView right=new ImageView(getContext());
            right.setId(riId);
            RelativeLayout.LayoutParams lps = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lps.addRule(RelativeLayout.ALIGN_PARENT_END);
            lps.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            int wid = DisplayHelper.dp2px(getContext(), 15);
            right.setPadding(wid, 0, wid, 0);

            right.setImageResource(icon);

            //过滤图标原来色彩，适应不同背景颜色
            right.setColorFilter(iconTint);

            right.setOnClickListener(onClickListener);

            addView(right,lps);
        }
        return this;
   }


    public void addRightTextView(String str,OnClickListener onClickListener){
        addRightTextView(str,getContext().getColor(R.color.text_gray),15,onClickListener);
    }

   public TopbarLayout addRightTextView(String str,int color,int textSize,OnClickListener onClickListener){
       if(checkView(riId)){
           TextView textView=new TextView(getContext());
           RelativeLayout.LayoutParams lps = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
           lps.addRule(RelativeLayout.ALIGN_PARENT_END);
           lps.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

           textView.setText(str);
           textView.setTextColor(color);
           textView.setTextSize(textSize);
           textView.setGravity(Gravity.CENTER_VERTICAL);

           int wid = DisplayHelper.dp2px(getContext(), 15);
           textView.setPadding(wid,0,wid,0);

           textView.setOnClickListener(onClickListener);
           textView.setId(riId);
           addView(textView,lps);
       }
       return this;
   }


   //检查左边是否添加有View，如果添加有则返回False
   private boolean checkView(int id_){
        boolean isAdd=true;
        for(int i=0;i<top_root.getChildCount();i++){
            if(top_root.getChildAt(i).getId()==id_){
                isAdd=false;
            }
        }
        return isAdd;
   }

    /**
     * @param tintColor 设置返回图标滤镜颜色，以适应不同背景色
     */
    public TopbarLayout setIconTintColor(int tintColor){
        if(top_root_left!=null){
            top_root_left.setColorFilter(tintColor);
        }

        return this;
    }

    //public Top

}
