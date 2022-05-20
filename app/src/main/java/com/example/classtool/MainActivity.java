package com.example.classtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.base.PopupWindows;
import com.example.classtool.binders.Class_cardBinder;
import com.example.classtool.models.Class_cardmodel;
import com.example.classtool.models.Class_colors_set;
import com.example.classtool.models.FindSort;
import com.example.classtool.models.QTime;
import com.example.classtool.models.Time_sets;
import com.example.classtool.utils.CompareIsDuplication;
import com.example.classtool.utils.FilesUtil;
import com.example.classtool.utils.ShowDialogUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private QMUISkinValueBuilder builder;
    private QMUIFrameLayout frameLayout;
    private  View mainlayout;
    private TextView class_weekdate,class_date,class_start_date,TagColors,class_course,visitLabel,chooseLabel,saveLabel,moreLabel,Schedule_title,tempSaveLabel;
    private EditText place,notes;
    private FloatingActionButton addLabel;
    private QMUIFullScreenPopup popups;
    private QMUIPopup popup;
    private MultiTypeAdapter multiTypeAdapter;

    private ImageButton imageButton;
    private TextView Class_nums;
    private RecyclerView recy;
    private QMUIRoundButton comfirm;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private ArrayList<Object> alls=new ArrayList<>();
    private  List<String> ncs=new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        sp= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editor=sp.edit();
        initRecy();
        visitLabel=(TextView) findViewById(R.id.visitSchedul);
        addLabel=(FloatingActionButton) findViewById(R.id.addLabel);
        chooseLabel=(TextView) findViewById(R.id.chooseLabel);
        saveLabel=(TextView) findViewById(R.id.saveLabe);
        tempSaveLabel=(TextView)findViewById(R.id.tempSaveLabel);
        moreLabel=(TextView)findViewById(R.id.moreLabel);
        Schedule_title=(TextView) findViewById(R.id.class_schedule_title);
        initViews();
    }



    private void initViews(){
        Showp p=new Showp(MainActivity.this);

        saveLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time_tmp=sp.getString("current_time_temp","武鸣校区作息时间");
                int selec=sp.getInt("scheSeq",0);
                List<String> tags=new ArrayList<>();
                tags=FilesUtil.readSchedulAndTimeTag();
               p.Qhowpop(saveLabel,"保存课表"+"("+time_tmp+")","请输入文件名(所选模板名:"+tags.get(selec).split(",")[0]+")");
            }
        });

        chooseLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               List<String> scheduls=FilesUtil.readSchedulAndTimeTag();
               if(scheduls.size()>0){
                   String[] values=new String[scheduls.size()];
                   String[] times=new String[scheduls.size()];
                   for(int i=0;i<scheduls.size();i++){
                       String[] splits=scheduls.get(i).split(",");
                       values[i]=splits[0];
                       times[i]=splits[1];
                   }

                   int select=sp.getInt("scheSeq",0);
                   showSchedialogs("课表模板选择",values,times,chooseLabel,select);
               }

            }
        });

        addLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog(addLabel);
            }
        });

        visitLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent();
                Bundle bundle=new Bundle();

                in.setClass(MainActivity.this,PrevisiSchdulActivity.class);

                List<Class_cardmodel> kalls=new ArrayList<>();
                for(int j=0;j<alls.size();j++){
                    Class_cardmodel l=(Class_cardmodel) alls.get(j);
                    kalls.add(l);
                }

                bundle.putSerializable("list",(Serializable) kalls);
                in.putExtras(bundle);
                startActivity(in);
            }
        });

        tempSaveLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Class_cardmodel> jall=new ArrayList<>();
                for(int i=0;i<alls.size();i++){
                    jall.add((Class_cardmodel) alls.get(i));
                }
                boolean isWrite= FilesUtil.writFile("临时课表数据",jall);
                if(isWrite) Toast.makeText(MainActivity.this, "课表数据保存成功！", Toast.LENGTH_SHORT).show();
                else Toast.makeText(MainActivity.this, "当前列表数据好像为空...保存失败", Toast.LENGTH_SHORT).show();
            }
        });

        moreLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popupq popupWindows=new Popupq(MainActivity.this,popup);
                String[] sets=new String[]{
                        "临时数据恢复","课表时间",
                };
                popupWindows.showPopup(moreLabel,sets,300,150,0,8);
            }
        });
    }


    private void initRecy(){
        recy=(RecyclerView) findViewById(R.id.main_recy);
        multiTypeAdapter=new MultiTypeAdapter();
        Class_cardBinder class_cardBinder=new Class_cardBinder();
        class_cardBinder.setOnItemClickListener(new Class_cardBinder.OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position,Class_cardmodel class_cardmodel) {
                ShowDialos(v,position,class_cardmodel);
            }
            @Override
            public void onItemDelete(View v, int position) {
                alls.remove(position);
                multiTypeAdapter.notifyItemRemoved(position);
                multiTypeAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"已删除第"+(position+1)+"个标签",Toast.LENGTH_SHORT).show();
            }
        });

        recy.setAdapter(multiTypeAdapter);
        multiTypeAdapter.register(Class_cardmodel.class,class_cardBinder);
        int selec=sp.getInt("scheSeq",0);
        Log.i("TAG", "initRecy: "+selec);
        List<String> tags=new ArrayList<>();
        tags=FilesUtil.readSchedulAndTimeTag();
        if(tags.size()>0){
            String[] tasets=new String[tags.size()];
            for(int i=0;i<tags.size();i++){
                String[] ets=tags.get(i).split(",");
                tasets[i]=ets[0];
            }
            List<Class_cardmodel> sall=FilesUtil.readFileData(tasets[selec]);
            for(Class_cardmodel l:sall){
                alls.add(l);
            }
            multiTypeAdapter.setItems(alls);
        }



    }

    private void initPopus(){
        builder = QMUISkinValueBuilder.acquire();
        frameLayout = new QMUIFrameLayout(MainActivity.this);
        frameLayout.setBackground(
                QMUIResHelper.getAttrDrawable(MainActivity.this, com.qmuiteam.qmui.R.attr.qmui_skin_support_popup_bg));
        builder.background(com.qmuiteam.qmui.R.attr.qmui_skin_support_popup_bg);
        QMUISkinHelper.setSkinValue(frameLayout, builder);
        frameLayout.setRadius(QMUIDisplayHelper.dp2px(MainActivity.this, 12));
        int padding = QMUIDisplayHelper.dp2px(MainActivity.this, 20);
        frameLayout.setPadding(padding, padding, padding, padding);
        int size = QMUIDisplayHelper.dp2px(MainActivity.this, 300);
        int height = QMUIDisplayHelper.dp2px(MainActivity.this, 580);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, height);


        mainlayout = View.inflate(MainActivity.this, R.layout.class_edit_layout, null);
        imageButton = (ImageButton) mainlayout.findViewById(R.id.class_close_btn);
        comfirm=(QMUIRoundButton)mainlayout.findViewById(R.id.comfirAct_btn);
        place=(EditText)mainlayout.findViewById(R.id.class_classroom);
        notes=(EditText)mainlayout.findViewById(R.id.class_notes);
        class_weekdate = (TextView) mainlayout.findViewById(R.id.class_spinner_weekdate);
        class_date=(TextView)mainlayout.findViewById(R.id.class_spinner_date);
        class_start_date=(TextView)mainlayout.findViewById(R.id.class_spinner_start_date);
        TagColors=(TextView)mainlayout.findViewById(R.id.class_color);
        Class_nums=(TextView)mainlayout.findViewById(R.id.class_spinner_date_nums);
        class_course = (TextView) mainlayout.findViewById(R.id.class_subject);



        Glide.with(mainlayout).load(R.drawable.ic_action_close).into(imageButton);

        frameLayout.addView(mainlayout, lp);
        popups = new QMUIFullScreenPopup(MainActivity.this);
    }


    public void ShowDialos(View AttachView,int position,Class_cardmodel class_cardmodel) {
        initPopus();

        if(class_cardmodel!=null) {
            String detail_date = class_cardmodel.getClass_date().substring(3);
            String dateq = class_cardmodel.getClass_date().substring(0, 3);

            class_weekdate.setText(dateq);
            class_date.setText(detail_date);
            class_course.setText(class_cardmodel.getClass_course());
            class_start_date.setText(class_cardmodel.getClass_startClass());//.substring(1,6));
            Class_nums.setText(class_cardmodel.getClass_totalClass());

            TagColors.setText(class_cardmodel.getLassColor());
            notes.setText(class_cardmodel.getOtherNotes());

            place.setText(class_cardmodel.getClass_classPlace());

            class_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottom("时间段",3, Time_sets.detail_dates,class_date,false,false,FindSort.returnColorSort(Time_sets.detail_dates,class_date.getText().toString()),"detail");
                }
            });

            class_weekdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottom("日期",7,Time_sets.weeks,class_weekdate,false,false,FindSort.returnColorSort(Time_sets.weeks,class_weekdate.getText().toString()),"weeks");
                }
            });

            class_start_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottom("上课时间",12,Time_sets.start_classes,class_start_date,true,false,FindSort.returnColorSort(Time_sets.start_classes,class_start_date.getText().toString()),"startq");
                }
            });

            TagColors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottom("标签颜色",Time_sets.colors.length,Time_sets.colors,TagColors,false,false,FindSort.returnColorSort(Time_sets.colors,TagColors.getText().toString()),"colors");
                }
            });

            Class_nums.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeNums();
                   }
            });
        }

            popups.addView(frameLayout)
                    .skinManager(QMUISkinManager.defaultInstance(MainActivity.this))
                    .onBlankClick(new QMUIFullScreenPopup.OnBlankClickListener() {
                        @Override
                        public void onBlankClick(QMUIFullScreenPopup popup) {

                        }
                    })
                    .onDismiss(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            //Toast.makeText(MainActivity.this(), "onDismiss", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show(AttachView);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popups.dismiss();
                }
            });

            CompareIsDuplication compareIsDuplication=new CompareIsDuplication();

            comfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String date=(!class_weekdate.getText().toString().isEmpty())?class_weekdate.getText().toString()+class_date.getText().toString():"星期一早上";
                    String startDate=(!class_start_date.getText().toString().isEmpty())?class_start_date.getText().toString():"第1节课";
                    String classNums=(!Class_nums.getText().toString().isEmpty())?Class_nums.getText().toString():"3节";
                    String places=place.getText().toString();
                    String colorq=(!TagColors.getText().toString().isEmpty())?TagColors.getText().toString():"红色";
                    String courseq= (!class_course.getText().toString().isEmpty())?class_course.getText().toString():"未填写";
                    String noteq=notes.getText().toString();
                    int color_sort=FindSort.returnColorSort(Time_sets.colors,colorq);

                    Class_cardmodel new_data=new Class_cardmodel(date,courseq,startDate,classNums,places,colorq, Class_colors_set.Class_colors[color_sort],noteq,"class_1",0);

                    if(compareAnyChange(class_cardmodel,new_data)==false){
                        ArrayList<Object> ew_alls=new ArrayList<>();
                        ew_alls.addAll(alls);
                        ew_alls.remove(position);
                        Log.i("remove itself-->","success!\new_alls size-->"+ew_alls.size()+"removed size-->"+alls.size());
                        int check=compareIsDuplication.returnResult(ew_alls,new_data,classNums,date);
                       checkClzz(check,position,new_data,0);

                    }else{
                        popups.dismiss();
                    }

                }
            });
        }


    public void ShowDialog(View AttachView) {
        initPopus();
        class_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottom("时间段",Time_sets.detail_dates.length,Time_sets.detail_dates,class_date,false,false,FindSort.returnColorSort(Time_sets.detail_dates,class_date.getText().toString()),"detail");
            }
        });

        class_weekdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottom("日期",7,Time_sets.weeks,class_weekdate,false,false,FindSort.returnColorSort(Time_sets.weeks,class_weekdate.getText().toString()),"weeks");
            }
        });

        class_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int allLen=sp.getInt("classLen",12);
                showBottom("上课时间",allLen,Time_sets.start_classes,class_start_date,true,false,FindSort.returnColorSort(Time_sets.start_classes,class_start_date.getText().toString()),"startq");
            }
        });

        TagColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottom("标签颜色",10,Time_sets.colors,TagColors,false,false,FindSort.returnColorSort(Time_sets.colors,TagColors.getText().toString()),"colors");
            }
        });

        Class_nums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              changeNums();
                }
        });

        popups.addView(frameLayout)
                .skinManager(QMUISkinManager.defaultInstance(MainActivity.this))
                .onBlankClick(new QMUIFullScreenPopup.OnBlankClickListener() {
                    @Override
                    public void onBlankClick(QMUIFullScreenPopup popup) {
                    }
                })
                .show(AttachView);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popups.dismiss();
            }
        });

        CompareIsDuplication compareIsDuplication=new CompareIsDuplication();
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date=(!class_weekdate.getText().toString().isEmpty())?class_weekdate.getText().toString()+class_date.getText().toString():"星期一早上";
                String startDate=(!class_start_date.getText().toString().isEmpty())?class_start_date.getText().toString():"第1节课";
                String classNums=(!Class_nums.getText().toString().isEmpty())?Class_nums.getText().toString():"3节";
                String places=place.getText().toString();
                String colorq=(!TagColors.getText().toString().isEmpty())?TagColors.getText().toString():"红色";
                String courseq= (!class_course.getText().toString().isEmpty())?class_course.getText().toString():"您还未填写任何哦~";
                String noteq=notes.getText().toString();
                int color_sort=FindSort.returnColorSort(Time_sets.colors,colorq);
                Class_cardmodel new_data=new Class_cardmodel(date,courseq,startDate,classNums,places,colorq, Class_colors_set.Class_colors[color_sort],noteq,"class_1",0);
                int check=compareIsDuplication.returnResult(alls,new_data,classNums,date);
                   checkClzz(check,0,new_data,1);

            }
        });
    }



    private void showBottom(String title,int StringsetLenth,String[] values,TextView view,boolean isStartDt,boolean isColor,int checkposition,String tagq){
        int Morning_nums_max=sp.getInt("Morning_nums_max",4);//morning_nums_max,实际为早上有morning_nums_max+1节课,实际节数为5，在这里实际为5-1；
        int Noon_startClass_min=sp.getInt("Noon_startClass",5);//morning_nums_max+1，实际表示为Noon_startClass+1,表示从第n+1节开始上课
        int Night_startClass_min=sp.getInt("Night_startClass",9);//Noon_startClass与节数来决定，即Noon_startClass+Noon_nums+1;比如，下午第4(3+1)节课开始,有3(2+1)节课,则晚上从第7((3+2+1)+1)节开始上课；
            QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(MainActivity.this);
            builder.setGravityCenter(true)
                    .setSkinManager(QMUISkinManager.defaultInstance(MainActivity.this))
                    .setTitle(title)
                    .setAddCancelBtn(true)
                    .setAllowDrag(true)
                    .setNeedRightMark(true)
                    .setCheckedIndex(checkposition)
                    .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            dialog.dismiss();
                            String detail_time=class_date.getText().toString();
                            CompareIsDuplication cpn=new CompareIsDuplication();
                            String day=class_weekdate.getText().toString();
                            int total=0;
                            switch (tagq){
                                case "detail":
                                    if(position==1){
                                        class_start_date.setText(Time_sets.start_classes[Noon_startClass_min]);
                                        class_date.setText(values[position]);
                                    }else if(position==2){
                                        class_start_date.setText(Time_sets.start_classes[Night_startClass_min]);
                                        class_date.setText(values[position]);
                                    }else{
                                        class_start_date.setText("第1节课");
                                        class_date.setText(values[position]);
                                    }
                                    break;

                                case "weeks":
                                    class_weekdate.setText(values[position]);
                                    break;

                                case "colors":
                                    TagColors.setText(values[position]);
                                    break;

                                case "startq":
                                    if(class_date.getText().toString().equals("早上")&&position>=Noon_startClass_min&&position<Night_startClass_min){
                                        //position=0;
                                        class_date.setText("下午");
                                       // Toast.makeText(MainActivity.this, "早上最多5节课，请重新选择！", Toast.LENGTH_SHORT).show();
                                    }else if(class_date.getText().toString().equals("下午")&&position>=Night_startClass_min){
                                        //position=5;
                                        class_date.setText("晚上");
                                        //Toast.makeText(MainActivity.this, "下午最多4节课，请重新选择！", Toast.LENGTH_SHORT).show();
                                    }else if(class_date.getText().toString().equals("下午")&&position<=Morning_nums_max){
                                        class_date.setText("早上");
                                    } else if(class_date.getText().toString().equals("晚上")&&position<Night_startClass_min&&position>Morning_nums_max){
                                        //position=9;
                                        class_date.setText("下午");
                                        //Toast.makeText(MainActivity.this, "晚上最多4节课，请重新选择！", Toast.LENGTH_SHORT).show();
                                    }else if(class_date.getText().toString().equals("晚上")&&position<=Morning_nums_max){
                                        class_date.setText("早上");
                                    }else if(class_date.getText().toString().equals("早上")&&position<Night_startClass_min&&position>=Noon_startClass_min){
                                        class_date.setText("下午");
                                    }else if(class_date.getText().toString().equals("早上")&&position>=Night_startClass_min){
                                        class_date.setText("晚上");
                                    }
                                    class_start_date.setText(values[position]);
                                    Class_nums.setText("1节");
                                    break;

                                    case "nums":
                                    Class_nums.setText(values[position]);
                                    break;

                            }
                        }
                    });


                if(isStartDt){
                for(int ij=0;ij<StringsetLenth;ij++){
                    if(ij<=Morning_nums_max){
                        builder.addItem("早上第"+(ij+1)+"节课");

                    }else
                    if(ij>=(Noon_startClass_min-1)&&ij<Night_startClass_min){
                        builder.addItem("下午第"+(ij+1)+"节课");
                    }else

                    if(ij>(Night_startClass_min-1)&&ij<StringsetLenth){
                        builder.addItem("晚上第"+(ij+1)+"节课");
                    }

                }
            }else if(isStartDt==false){
                    for (int i = 0; i < StringsetLenth; i++) {
                        builder.addItem(values[i]);

                    }
                    }

            builder.build().show();
        }




        private boolean compareAnyChange(Class_cardmodel mclass_cardmodel,Class_cardmodel class_cardmodel){
        boolean isSame=false;
        String mDateq=(mclass_cardmodel.getClass_date()!=null)?mclass_cardmodel.getClass_date():"星期一早上";
        String Dateq=class_cardmodel.getClass_date();

        String msubject=(mclass_cardmodel.getClass_course()!=null)?mclass_cardmodel.getClass_course():"无";
        String subject=class_cardmodel.getClass_course();

        String mstartClass=(mclass_cardmodel.getClass_startClass()!=null)?mclass_cardmodel.getClass_startClass():"第一节";
        String startClass=class_cardmodel.getClass_startClass();

        String mtotal=(mclass_cardmodel.getClass_totalClass()!=null)?mclass_cardmodel.getClass_totalClass():"3节";
        String total=class_cardmodel.getClass_totalClass();


        String mplace=(mclass_cardmodel.getClass_classPlace()!=null)?mclass_cardmodel.getClass_classPlace():"无";
        String place=class_cardmodel.getClass_classPlace();

        String mcolor=(mclass_cardmodel.getLassColor()!=null)?mclass_cardmodel.getLassColor():"红色";
        String color=class_cardmodel.getLassColor();

        String mnotes=(mclass_cardmodel.getOtherNotes()!=null)?mclass_cardmodel.getOtherNotes():"备注：其他注意事项";
        String notes=class_cardmodel.getOtherNotes();


        if(mDateq.equals(Dateq)&&msubject.equals(subject)&&mstartClass.equals(startClass)&&mtotal.equals(total)&&mcolor.equals(color)&&mnotes.equals(notes)&&mplace.equals(place)){
            isSame=true;
        }
        return isSame;
        }


        private void checkClzz(int check,int position,Class_cardmodel new_data,int type){

            if(check==7|check%2==1){
                Toast.makeText(MainActivity.this, "和其他标签的上课时间或下课时间产生矛盾惹~ 请做检查后再试哦~", Toast.LENGTH_LONG).show();
                // popups.dismiss();
            }//else if(check==2){
               // Toast.makeText(MainActivity.this, "和其他标签的同一天的具体上课时间内产生矛盾了哟~ 请重新修改时间~", Toast.LENGTH_LONG).show();

            else if(check%2==0){
                switch (type){
                    case 0:
                        //new UpdateTask(new_data,(Class_cardmodel) alls.get(position)).execute();
                        alls.remove(position);
                        alls.add(position,new_data);
                        multiTypeAdapter.notifyItemChanged(position,"updating");
                        Toast.makeText(MainActivity.this, "内容更新成功", Toast.LENGTH_SHORT).show();
                        popups.dismiss();
                        break;
                    case 1:
                        //new AddTask(new_data).execute();
                        alls.add(new_data);
                        multiTypeAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "添加标签成功", Toast.LENGTH_SHORT).show();
                        popups.dismiss();
                        break;
                }

            }else if(check==3){
                Toast.makeText(MainActivity.this, "请勿重复添加，已有标签课时为5啦~", Toast.LENGTH_SHORT).show();
            }
        }



        private void changeNums(){

        int isChange=FindSort.returnColorSort(Time_sets.start_classes,class_start_date.getText().toString());
        int Morning_nums_max=sp.getInt("Morning_nums_max",4);//morning_nums_max,实际为早上有morning_nums_max+1节课,实际节数为5，在这里实际为5-1；
        int Noon_startClass_min=sp.getInt("Noon_startClass",5);
        int Night_startClass_min=sp.getInt("Night_startClass",9);
        int classLen=sp.getInt("classLen",12);

        int Morning_nums=0;
        if(isChange>(Noon_startClass_min-1)&&isChange<Night_startClass_min){
            isChange=isChange-Noon_startClass_min;
            Morning_nums=Night_startClass_min-Noon_startClass_min;
        }else if(isChange>=Night_startClass_min){
            isChange=isChange-Night_startClass_min;//isChange-Night_startClass_min;
            Morning_nums=classLen-Night_startClass_min;//Night_startClass_min的最后一节课+1，比如最后一节为12，实际为11，则总课时为11+1
        }else{
            Morning_nums=Morning_nums_max+1;//Morning_num_max+1;

        }
        int chenageLen=checkNums(isChange,Morning_nums);
            showBottom("共计节数",chenageLen,Time_sets.class_nums,Class_nums,false,false, FindSort.returnColorSort(Time_sets.class_nums,Class_nums.getText().toString()),"nums");
        }

        private int  checkNums(int startNums,int max){
        int numMax=max-startNums;
        return numMax;
    }


    public class  Popupq extends  PopupWindows{

        public Popupq(Context context, QMUIPopup popup) {
            super(context, popup);
        }

        @Override
        public void Clickpopup(int i) {
            super.Clickpopup(i);
            switch (i){
                case 0:
                    List<Class_cardmodel> lall=new ArrayList<>();
                    alls.clear();
                    lall=FilesUtil.readFileData("临时课表数据");
                    if(lall.size()>0) {
                        for (int y = 0; y < lall.size(); y++) {
                            Object obj = (Object) lall.get(y);
                            alls.add(obj);
                        }
                        multiTypeAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "数据恢复成功！", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    Toast.makeText(MainActivity.this, "恢复失败，临时课表文件没有相关数据！", Toast.LENGTH_SHORT).show();
                }
                    break;
                case 1:
                    Intent iw=new Intent();
                    iw.setClass(MainActivity.this,Scheduldatas.class);
                    startActivity(iw);
                    break;
            }
        }
    }

    class Showp extends ShowDialogUtil{

        public Showp(Context getContext) {
            super(getContext);
        }

        @Override
        public void onSave(View saveView, String EditgetStr,QMUIFullScreenPopup pop) {
            super.onSave(saveView, EditgetStr,pop);
            List<Class_cardmodel> jall=new ArrayList<>();
            for(int i=0;i<alls.size();i++){
                jall.add((Class_cardmodel) alls.get(i));
            }
            if (EditgetStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "请输入文字！", Toast.LENGTH_SHORT).show();
            }else {
                boolean isWrite= FilesUtil.writFile(EditgetStr,jall);
                List<String> l=FilesUtil.readSchedulAndTimeTag();
                int y=0;
                for(String s: l){
                    if(s.split(",")[0].equals(EditgetStr)){
                       EditgetStr="";
                       y+=1;
                    }
                }
                if(y==0) {
                    String time_temp=sp.getString("current_time_temp","武鸣校区作息时间");
                    FilesUtil.AppendScheDulAndTimeTag(EditgetStr+","+time_temp);
                }
                pop.dismiss();
                if(isWrite) Toast.makeText(MainActivity.this, "课表数据保存成功！", Toast.LENGTH_SHORT).show();
                else Toast.makeText(MainActivity.this, "当前列表数据好像为空...保存失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void showSchedialogs(String title,String[] values,String[] timetags,TextView view,int checkposition){
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(MainActivity.this);
        builder.setGravityCenter(true)
                .setSkinManager(QMUISkinManager.defaultInstance(MainActivity.this))
                .setTitle(title)
                .setAddCancelBtn(true)
                .setAllowDrag(true)
                .setNeedRightMark(true)
                .setCheckedIndex(checkposition)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                       editor.putInt("scheSeq",position);
                       editor.commit();
                        int tag_index=0;
                        int morNums=4;
                        int noonStartCl=5;
                        //int noonNums=3;
                        int ngithStartCl=9;
                       String time_tag=timetags[position];
                       try {
                           List<String> schetimesl = FilesUtil.readTimeTag();
                           List<Class_cardmodel> wime=FilesUtil.readFileData(values[position]);
                           alls.clear();
                           for(Class_cardmodel c:wime){
                               alls.add(c);
                           }
                           multiTypeAdapter.notifyDataSetChanged();
                           if(schetimesl.size()>0){
                               for(int o=0;o<schetimesl.size();o++){
                                   if(schetimesl.get(o).split(",")[0].equals(time_tag)){
                                       tag_index=o;
                                   }
                               }
                               String[] ptimes=schetimesl.get(tag_index).split(",");
                               morNums=FindSort.returnColorSort(Time_sets.class_nums,ptimes[2]);
                               noonStartCl=FindSort.returnColorSort(Time_sets.start_classes,ptimes[3]);
                               ngithStartCl=FindSort.returnColorSort(Time_sets.start_classes,ptimes[5]);

                               editor.putInt("Morning_nums_max",morNums);
                               editor.putInt("Noon_startClass",noonStartCl);
                               editor.putInt("Night_startClass",ngithStartCl);
                               editor.commit();
                           }
                       }catch (Exception e){
                           e.printStackTrace();
                       }
                       Toast.makeText(MainActivity.this, "您已选择"+values[position]+"!", Toast.LENGTH_SHORT).show();
                       dialog.dismiss();
                    }
                });

        for(int f=0;f<values.length;f++){
            builder.addItem(values[f]);
        }

        builder.build().show();
    }

}


