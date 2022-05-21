package com.example.classtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.base.BasicActivity;
import com.example.classtool.base.OnItemClick;
import com.example.classtool.binders.QTimeBinder;
import com.example.classtool.models.QTime;
import com.example.classtool.models.Time_sets;
import com.example.classtool.utils.CheckIllegalStrUtil;
import com.example.classtool.utils.CompareIsDuplication;
import com.example.classtool.utils.FilesUtil;
import com.example.classtool.utils.ShowDialogUtil;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TimeAddingActivity extends BasicActivity {

    private QMUISkinValueBuilder builder;
    private QMUIFrameLayout frameLayout;
    private View mainlayout;
    private QMUIFullScreenPopup popups;
    private MultiTypeAdapter multiTypeAdapter;
    private TextView time_add_pup_autoId,act_morning_nums,act_noon_startCl,act_add_noon_nums,act_ngith_start,act_ngith_nums,act_add_add,act_add_saving;
    private EditText time_add_pup_setTime;
    private QMUIRoundButton time_add_pup_save_btn;
    private ImageButton time_add_pup_close_btn;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private RecyclerView recy;
    private ArrayList<Object> alls=new ArrayList<>();
    private QMUICollapsingTopBarLayout collapsingTopBarLayout;
    private QShowDialogUtil showDialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_add_test);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        //sp= PreferenceManager.getDefaultSharedPreferences(TimeAddingActivity.this);
        showDialogUtil=new QShowDialogUtil(TimeAddingActivity.this);
        initMainViews();
        //editor=sp.edit();
        initsView();
    }

    private void initMainViews(){
       /* int morning_nums=sp.getInt("time_morning_nums_max",4);

        int noon_startCl=sp.getInt("time_noon_startClass",5);
        int ngith_startCl=sp.getInt("time_night_startClass",9);
        int classLen=sp.getInt("time_classLen",12);
        int noon_nums=sp.getInt("time_noon_nums",3);*/

        act_morning_nums=(TextView)findViewById(R.id.act_add_morning_nums);
        act_noon_startCl=(TextView)findViewById(R.id.act_add_noon_start);
        act_add_noon_nums=(TextView)findViewById(R.id.act_add_noon_nums);
        act_ngith_start=(TextView)findViewById(R.id.act_add_ngith_startCl);
        act_ngith_nums=(TextView)findViewById(R.id.act_add_ngith_nums);
        act_add_add=(TextView) findViewById(R.id.act_add_add);
        act_add_saving=(TextView)findViewById(R.id.act_add_saving);
        recy=(RecyclerView)findViewById(R.id.act_add_recy);
        collapsingTopBarLayout=(QMUICollapsingTopBarLayout)findViewById(R.id.collapsing_topbar_layout);

        collapsingTopBarLayout.setTitle("课表时间设定");
        collapsingTopBarLayout.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        collapsingTopBarLayout.setCollapsedTitleTextColor(getColor(com.qmuiteam.qmui.R.color.qmui_config_color_gray_2));
        collapsingTopBarLayout.setExpandedTitleColor(Color.TRANSPARENT);

        act_morning_nums.setText("5节");
        act_noon_startCl.setText("第6节课");
        act_ngith_start.setText("第10节课");
        act_ngith_nums.setText("3节");
        act_add_noon_nums.setText("4节");
        TextListeners();
        multiTypeAdapter=new MultiTypeAdapter();
        QTimeBinder qTimeBinder=new QTimeBinder();
        qTimeBinder.setOnItemClick(new OnItemClick() {
            @Override
            public void onClick(View v,int position, int sort,String sortStr,String str) {
                showAddpup(act_add_add,position,sort,sortStr,str);
            }
        });
        recy.setAdapter(multiTypeAdapter);
        multiTypeAdapter.register(QTime.class,qTimeBinder);
        iniDatas();
        multiTypeAdapter.setItems(alls);
        act_add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alls.clear();
                iniDatas();
                //multiTypeAdapter.setItems(alls);
                multiTypeAdapter.notifyDataSetChanged();

            }
        });


    }

    private void TextListeners(){
        act_morning_nums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottom("上午上课总节数",6,Time_sets.class_nums,act_morning_nums,false,returnColorSort(Time_sets.class_nums,act_morning_nums.getText().toString()),"mor_Clnums");
            }
        });

        act_noon_startCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottom("下午上课开始时间",13,Time_sets.start_classes,act_noon_startCl,true,returnColorSort(Time_sets.start_classes,act_noon_startCl.getText().toString()),"noon_startCl");
            }
        });

        act_add_noon_nums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottom("下午上课总节数",6,Time_sets.class_nums,act_add_noon_nums,false,returnColorSort(Time_sets.class_nums,act_add_noon_nums.getText().toString()),"noon_Clnums");
            }
        });

        act_ngith_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottom("晚上上课开始时间",13,Time_sets.start_classes,act_ngith_start,true,returnColorSort(Time_sets.start_classes,act_ngith_start.getText().toString()),"ngith_startCl");
            }
        });

        act_ngith_nums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottom("晚上上课总节数",6,Time_sets.class_nums,act_ngith_nums,false,returnColorSort(Time_sets.class_nums,act_ngith_nums.getText().toString()),"ngith_Clnums");
            }
        });

        act_add_saving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int morning_nums=returnColorSort(Time_sets.class_nums,act_morning_nums.getText().toString());
                int noon_startCl=returnColorSort(Time_sets.start_classes,act_noon_startCl.getText().toString());
                int ngith_startCl=returnColorSort(Time_sets.start_classes,act_ngith_start.getText().toString());
                int noon_nums=returnColorSort(Time_sets.class_nums,act_add_noon_nums.getText().toString());
                int classLen=ngith_startCl+returnColorSort(Time_sets.class_nums,act_ngith_nums.getText().toString())+1;
                if(alls.size()==classLen){
                    showDialogUtil.Qhowpop(act_add_saving,"保存作息时间","请输入文件名以保存相关数据");
                }else{
                    Toast.makeText(TimeAddingActivity.this, "保存失败，请点击生成具体课表时间按钮！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void iniDatas(){
        int morning_nums=returnColorSort(Time_sets.class_nums,act_morning_nums.getText().toString());
        int start_noon=returnColorSort(Time_sets.start_classes,act_noon_startCl.getText().toString());
        int noon_nums=returnColorSort(Time_sets.class_nums,act_add_noon_nums.getText().toString());
        int startCl_ngith=returnColorSort(Time_sets.start_classes,act_ngith_start.getText().toString());
        int ngith_nums=returnColorSort(Time_sets.class_nums,act_ngith_nums.getText().toString());

            for(int i=0;i<=morning_nums+noon_nums+ngith_nums+3;i++){
                if(i<=morning_nums){
                    alls.add(new QTime(i,"上午第"+(i+1)+"节","上课时间-下课时间"));
                }else if(i>=start_noon&&i<=start_noon+noon_nums){
                    alls.add(new QTime(i,"下午第"+(i+1)+"节","上课时间-下课时间"));
                }else if(i>=startCl_ngith&&i<=ngith_nums+startCl_ngith){
                    alls.add(new QTime(i,"晚上第"+(i+1)+"节","上课时间-下课时间"));
                }


        }

    }

    private void initsView(){
        builder = QMUISkinValueBuilder.acquire();
        frameLayout = new QMUIFrameLayout(TimeAddingActivity.this);
        frameLayout.setBackground(
                QMUIResHelper.getAttrDrawable(TimeAddingActivity.this, com.qmuiteam.qmui.R.attr.qmui_skin_support_popup_bg));
        builder.background(com.qmuiteam.qmui.R.attr.qmui_skin_support_popup_bg);
        QMUISkinHelper.setSkinValue(frameLayout, builder);
        frameLayout.setRadius(QMUIDisplayHelper.dp2px(TimeAddingActivity.this, 12));
        int padding = QMUIDisplayHelper.dp2px(TimeAddingActivity.this, 8);
        frameLayout.setPadding(padding, padding, padding, padding);
        int size = QMUIDisplayHelper.dp2px(TimeAddingActivity.this, 300);
        int height = QMUIDisplayHelper.dp2px(TimeAddingActivity.this, 400);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, height);

        mainlayout = View.inflate(TimeAddingActivity.this, R.layout.time_add_item_popup, null);
        time_add_pup_close_btn = (ImageButton) mainlayout.findViewById(R.id.time_add_popup_close_btn);
        time_add_pup_save_btn=(QMUIRoundButton) mainlayout.findViewById(R.id.time_add_save_btn);
        time_add_pup_autoId=(TextView) mainlayout.findViewById(R.id.time_add_popup_id);
        time_add_pup_setTime=(EditText) mainlayout.findViewById(R.id.time_add_popup_setTime);

        Glide.with(mainlayout).load(R.drawable.ic_action_close).into(time_add_pup_close_btn);

        frameLayout.addView(mainlayout, lp);
        popups = new QMUIFullScreenPopup(TimeAddingActivity.this);

    }

    private void showAddpup(View AttachView,int position,int sort,String sortStr,String content){
        popups.addView(frameLayout)
                .skinManager(QMUISkinManager.defaultInstance(TimeAddingActivity.this))
                .onBlankClick(new QMUIFullScreenPopup.OnBlankClickListener() {
                    @Override
                    public void onBlankClick(QMUIFullScreenPopup popup) {
                    }
                })
                .show(AttachView);

        time_add_pup_autoId.setText(sortStr);
        if(content.equals("上课时间-下课时间")){
            time_add_pup_setTime.setText("");
        }else{
            time_add_pup_setTime.setText(content.replace(":",""));
        }


        time_add_pup_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popups.dismiss();
            }
        });

        time_add_pup_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStr=time_add_pup_setTime.getText().toString();
                alls.remove(position);
                alls.add(position,new QTime(sort,sortStr, timeStr.replaceAll("(\\d{2})(.*)(\\d{2})$","$1:$2:$3")));
                multiTypeAdapter.notifyItemChanged(position,"updating");
               // boolean check= CheckIllegalStrUtil.isIllegalStr(timeStr);
                Toast.makeText(TimeAddingActivity.this, "内容更新成功!", Toast.LENGTH_SHORT).show();
                 popups.dismiss();
            }
        });
    }


    private void showBottom(String title,int StringsetLenth,String[] values,TextView view,boolean isStartDt,int checkposition,String tagq){
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(TimeAddingActivity.this);
        builder.setGravityCenter(true)
                .setSkinManager(QMUISkinManager.defaultInstance(TimeAddingActivity.this))
                .setTitle(title)
                .setAddCancelBtn(true)
                .setAllowDrag(true)
                .setNeedRightMark(true)
                .setCheckedIndex(checkposition)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        switch (tagq){
                            case "mor_Clnums":
                                act_noon_startCl.setText(Time_sets.start_classes[position+1]);
                                int kall=position+2+returnColorSort(Time_sets.class_nums,act_add_noon_nums.getText().toString());
                                act_ngith_start.setText(Time_sets.start_classes[kall]);
                                view.setText(values[position]);
                                break;
                            case "noon_startCl":
                                int allq=returnColorSort(Time_sets.class_nums,act_morning_nums.getText().toString());
                                int noonall=position+returnColorSort(Time_sets.class_nums,act_add_noon_nums.getText().toString());
                                if(position>allq) {
                                    view.setText(values[position]);
                                    act_ngith_start.setText(Time_sets.start_classes[noonall+1]);

                                }else{
                                    Toast.makeText(TimeAddingActivity.this, "哎呀...和上午时间段发生冲突了哟~", Toast.LENGTH_SHORT).show();
                                }

                                break;

                            case "noon_Clnums":
                                int noon_startCl=returnColorSort(Time_sets.start_classes,act_noon_startCl.getText().toString());
                                act_ngith_start.setText(Time_sets.start_classes[noon_startCl+position+1]);
                                view.setText(values[position]);
                                break;

                            case "ngith_startCl":
                                int all=returnColorSort(Time_sets.start_classes,act_noon_startCl.getText().toString())+returnColorSort(Time_sets.class_nums,act_add_noon_nums.getText().toString());
                                if(position>all) {
                                    view.setText(values[position]);
                                }else{
                                    Toast.makeText(TimeAddingActivity.this, "哎呀...和下午时间段发生冲突了哟~", Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case "ngith_Clnums":
                                view.setText(values[position]);
                                break;
                        }
                    }
                });

        if(isStartDt){
            for(int ij=0;ij<StringsetLenth;ij++){
                builder.addItem("第"+(ij+1)+"节课");

            }
        }else if(isStartDt==false){
            for (int i = 0; i < StringsetLenth; i++) {
                builder.addItem(values[i]);

            }
        }

        builder.build().show();
    }

    private int returnColorSort(String[] qolors,String value){
        int lenth=qolors.length;
        int color_sort=0;
        for(int i=0;i<lenth;i++){
            if(qolors[i].equals(value)){
                //return i;
                color_sort=i;
            }
        }
        return color_sort;
    }

    public class  QShowDialogUtil extends ShowDialogUtil{

        public QShowDialogUtil(Context getContext) {
            super(getContext);
        }

        @Override
        public void onSave(View saveView,String EditgetStr,QMUIFullScreenPopup pop) {
            super.onSave(saveView, EditgetStr,pop);
            List<QTime> qTimeList = new ArrayList<>();
            for (int i = 0; i < alls.size(); i++) {
                qTimeList.add((QTime) alls.get(i));
            }

            if (EditgetStr.isEmpty()) {
                Toast.makeText(TimeAddingActivity.this, "请输入文字！", Toast.LENGTH_SHORT).show();
            }else {
                FilesUtil.AppendClassTime(getApplicationContext(),qTimeList, EditgetStr);
                FilesUtil.AppendTimeTag(getApplicationContext(),EditgetStr+","+"第1节课,"+act_morning_nums.getText().toString()+","+act_noon_startCl.getText().toString()+","+act_add_noon_nums.getText().toString()+","+act_ngith_start.getText().toString()+","+act_ngith_nums.getText().toString());
                Toast.makeText(TimeAddingActivity.this, "数据保存成功！", Toast.LENGTH_SHORT).show();
                pop.dismiss();
                finish();
            }
        }
    }

}