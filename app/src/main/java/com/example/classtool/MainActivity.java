package com.example.classtool;

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

import com.bumptech.glide.Glide;
import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.base.BasicActivity;
import com.example.classtool.base.PopupWindows;
import com.example.classtool.binders.Class_cardBinder;
import com.example.classtool.models.Class_cardmodel;
import com.example.classtool.models.Class_colors_set;
import com.example.classtool.utils.FindSort;
import com.example.classtool.models.Static_sets;
import com.example.classtool.utils.CompareIsDuplication;
import com.example.classtool.utils.FilesUtil;
import com.example.classtool.utils.ShowDialogUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hjq.toast.ToastUtils;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BasicActivity {
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
                String time_tmp=sp.getString("current_time_temp","????????????????????????");
                int selec=sp.getInt("scheSeq",0);
                List<String> tags=new ArrayList<>();
                tags=FilesUtil.readSchedulAndTimeTag(getApplicationContext());
                String templ="";
                if(tags.size()==0||tags==null) templ="???";
                else templ=tags.get(selec).split(",")[0];
               p.Qhowpop(saveLabel,"????????????"+"("+time_tmp+")","??????????????????(???????????????:"+templ+")");
            }
        });

        chooseLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               List<String> scheduls=FilesUtil.readSchedulAndTimeTag(getApplicationContext());
               if(scheduls.size()>0){
                   String[] values=new String[scheduls.size()];
                   String[] times=new String[scheduls.size()];
                   for(int i=0;i<scheduls.size();i++){
                       String[] splits=scheduls.get(i).split(",");
                       values[i]=splits[0];
                       times[i]=splits[1];
                   }

                   int select=sp.getInt("scheSeq",0);
                   showSchedialogs("??????????????????",values,times,chooseLabel,select);
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
                List<String> checktags=FilesUtil.readSchedulAndTimeTag(getApplicationContext());
                if(checktags.size()==0){
                    FilesUtil.AppendScheDulAndTimeTag(getApplicationContext(),"????????????,????????????????????????");
                }else{

                    checktags.remove(0);
                    checktags.add(0,"????????????,"+sp.getString("current_time_temp","????????????????????????"));

                    FilesUtil.RemoveScheDulAndTimeTag(getApplicationContext(),checktags);
                }

                boolean isWrite= FilesUtil.writFile(getApplicationContext(),"????????????",jall);
                if(isWrite){
                   /* new QMUITipDialog.Builder(MainActivity.this)
                            .setTipWord("???????????????????????????")
                            .create().show();*/
                    ToastUtils.show("???????????????????????????");
                }
                else{
                    ToastUtils.show("??????????????????????????????...????????????");
                }
            }
        });

        moreLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popupq popupWindows=new Popupq(MainActivity.this,popup);
                String[] sets=new String[]{
                        "??????????????????","????????????",
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

                new QMUIDialog.MessageDialogBuilder(MainActivity.this)
                        .setTitle("????????????")
                        .setMessage("??????????????????????????????")
                        .setCancelable(true)
                        .addAction("??????", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0,"??????", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                alls.remove(position);
                                multiTypeAdapter.notifyItemRemoved(position);
                                multiTypeAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .create(R.style.DialogTheme2).show();

            }
        });

        recy.setAdapter(multiTypeAdapter);
        multiTypeAdapter.register(Class_cardmodel.class,class_cardBinder);


        /*int selec=sp.getInt("scheSeq",0);
        Log.i("TAG", "initRecy: "+selec);
        List<String> tags=new ArrayList<>();
        tags=FilesUtil.readSchedulAndTimeTag(getApplicationContext());
            String[] tasets=new String[tags.size()];
            for(int i=0;i<tags.size();i++){
                String[] ets=tags.get(i).split(",");
                tasets[i]=ets[0];
            }
           List<Class_cardmodel> sall=FilesUtil.readFileData(getApplicationContext(),tasets[selec]);
            if(sall==0||sall==null)
            for(Class_cardmodel l:sall) {
                alls.add(l);
            }*/
            multiTypeAdapter.setItems(alls);

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
        int height = QMUIDisplayHelper.dp2px(MainActivity.this, 540);
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
            TagColors.setBackgroundColor(getColor(class_cardmodel.getClassColor()));
            notes.setText(class_cardmodel.getOtherNotes());

            place.setText(class_cardmodel.getClass_classPlace());

            class_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottom("?????????",3, Static_sets.detail_dates,class_date,false,false,FindSort.returnColorSort(Static_sets.detail_dates,class_date.getText().toString()),"detail");
                }
            });

            class_weekdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottom("??????",7, Static_sets.weeks,class_weekdate,false,false,FindSort.returnColorSort(Static_sets.weeks,class_weekdate.getText().toString()),"weeks");
                }
            });

            class_start_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int allLen=sp.getInt("classLen",12);
                    showBottom("????????????",allLen, Static_sets.start_classes,class_start_date,true,false,FindSort.returnColorSort(Static_sets.start_classes,class_start_date.getText().toString()),"startq");
                }
            });

            TagColors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottom("????????????", Static_sets.colors.length, Static_sets.colors,TagColors,false,false,FindSort.returnColorSort(Static_sets.colors,TagColors.getText().toString()),"colors");
                }
            });

            Class_nums.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottom("????????????",changeNums(), Static_sets.class_nums,Class_nums,false,false, FindSort.returnColorSort(Static_sets.class_nums,Class_nums.getText().toString()),"nums");
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

                    String date=(!class_weekdate.getText().toString().isEmpty())?class_weekdate.getText().toString()+class_date.getText().toString():"???????????????";
                    String startDate=(!class_start_date.getText().toString().isEmpty())?class_start_date.getText().toString():"???1??????";
                    String classNums=(!Class_nums.getText().toString().isEmpty())?Class_nums.getText().toString():"3???";
                    String places=place.getText().toString();
                    String colorq=(!TagColors.getText().toString().isEmpty())?TagColors.getText().toString():"??????";
                    String courseq= (!class_course.getText().toString().isEmpty())?class_course.getText().toString():"?????????";
                    String noteq=notes.getText().toString();
                    int color_sort=FindSort.returnColorSort(Static_sets.colors,colorq);

                    Class_cardmodel new_data=new Class_cardmodel(date,courseq,startDate,classNums,places,colorq, Class_colors_set.Class_colors[color_sort],noteq,"class_1",0);

                    if(compareAnyChange(class_cardmodel,new_data)==false){
                        ArrayList<Object> ew_alls=new ArrayList<>();
                        ew_alls.addAll(alls);
                        ew_alls.remove(position);
                        Log.i("remove itself-->","success!\new_alls size-->"+ew_alls.size()+"removed size-->"+alls.size());
                        int check=compareIsDuplication.returnResult(ew_alls,new_data,classNums,date,summerizeClums());
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
                showBottom("?????????", Static_sets.detail_dates.length, Static_sets.detail_dates,class_date,false,false,FindSort.returnColorSort(Static_sets.detail_dates,class_date.getText().toString()),"detail");
            }
        });

        class_weekdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottom("??????",7, Static_sets.weeks,class_weekdate,false,false,FindSort.returnColorSort(Static_sets.weeks,class_weekdate.getText().toString()),"weeks");
            }
        });

        class_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int allLen=sp.getInt("classLen",12);
                showBottom("????????????",allLen, Static_sets.start_classes,class_start_date,true,false,FindSort.returnColorSort(Static_sets.start_classes,class_start_date.getText().toString()),"startq");
            }
        });

        TagColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottom("????????????",Static_sets.colors.length, Static_sets.colors,TagColors,false,false,FindSort.returnColorSort(Static_sets.colors,TagColors.getText().toString()),"colors");
            }
        });

        Class_nums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottom("????????????",changeNums(), Static_sets.class_nums,Class_nums,false,false, FindSort.returnColorSort(Static_sets.class_nums,Class_nums.getText().toString()),"nums");
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
                String date=(!class_weekdate.getText().toString().isEmpty())?class_weekdate.getText().toString()+class_date.getText().toString():"???????????????";
                String startDate=(!class_start_date.getText().toString().isEmpty())?class_start_date.getText().toString():"???1??????";
                String classNums=(!Class_nums.getText().toString().isEmpty())?Class_nums.getText().toString():"3???";
                String places=place.getText().toString();
                String colorq=(!TagColors.getText().toString().isEmpty())?TagColors.getText().toString():"??????";
                String courseq= (!class_course.getText().toString().isEmpty())?class_course.getText().toString():"";
                String noteq=notes.getText().toString();
                int color_sort=FindSort.returnColorSort(Static_sets.colors,colorq);
                Class_cardmodel new_data=new Class_cardmodel(date,courseq,startDate,classNums,places,colorq, Class_colors_set.Class_colors[color_sort],noteq,"class_1",0);
                int check=compareIsDuplication.returnResult(alls,new_data,classNums,date,summerizeClums());
                   checkClzz(check,0,new_data,1);

            }
        });
    }



    private void showBottom(String title,int StringsetLenth,String[] values,TextView view,boolean isStartDt,boolean isColor,int checkposition,String tagq){
        int Morning_nums_max=sp.getInt("Morning_nums_max",4);//morning_nums_max,??????????????????morning_nums_max+1??????,???????????????5?????????????????????5-1???
        int Noon_startClass_min=sp.getInt("Noon_startClass",5);//morning_nums_max+1??????????????????Noon_startClass+1,????????????n+1???????????????
        int Night_startClass_min=sp.getInt("Night_startClass",9);//Noon_startClass????????????????????????Noon_startClass+Noon_nums+1;??????????????????4(3+1)????????????,???3(2+1)??????,???????????????7((3+2+1)+1)??????????????????
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
                            //String detail_time=class_date.getText().toString();
                            CompareIsDuplication cpn=new CompareIsDuplication();
                            //String day=class_weekdate.getText().toString();
                           // int total=0;
                            switch (tagq){
                                case "detail":
                                    if(position==1){
                                        class_start_date.setText(Static_sets.start_classes[Noon_startClass_min]);
                                        class_date.setText(values[position]);
                                    }else if(position==2){
                                        class_start_date.setText(Static_sets.start_classes[Night_startClass_min]);
                                        class_date.setText(values[position]);
                                    }else{
                                        class_start_date.setText("???1??????");
                                        class_date.setText(values[position]);
                                    }
                                    break;

                                case "weeks":
                                    class_weekdate.setText(values[position]);
                                    break;

                                case "colors":
                                    TagColors.setText(values[position]);
                                    TagColors.setBackgroundColor(getColor(Class_colors_set.Class_colors[position]));
                                    break;

                                case "startq":
                                    if(class_date.getText().toString().equals("??????")&&position>=Noon_startClass_min&&position<Night_startClass_min){
                                        //position=0;
                                        class_date.setText("??????");
                                    }else if(class_date.getText().toString().equals("??????")&&position>=Night_startClass_min){
                                        //position=5;
                                        class_date.setText("??????");
                                    }else if(class_date.getText().toString().equals("??????")&&position<=Morning_nums_max){
                                        class_date.setText("??????");
                                    } else if(class_date.getText().toString().equals("??????")&&position<Night_startClass_min&&position>Morning_nums_max){
                                        //position=9;
                                        class_date.setText("??????");
                                    }else if(class_date.getText().toString().equals("??????")&&position<=Morning_nums_max){
                                        class_date.setText("??????");
                                    }else if(class_date.getText().toString().equals("??????")&&position<Night_startClass_min&&position>=Noon_startClass_min){
                                        class_date.setText("??????");
                                    }else if(class_date.getText().toString().equals("??????")&&position>=Night_startClass_min){
                                        class_date.setText("??????");
                                    }
                                    class_start_date.setText(values[position]);
                                    Class_nums.setText("1???");
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
                        builder.addItem("?????????"+(ij+1)+"??????");

                    }else
                    if(ij>=(Noon_startClass_min-1)&&ij<Night_startClass_min){
                        builder.addItem("?????????"+(ij+1)+"??????");
                    }else

                    if(ij>(Night_startClass_min-1)&&ij<StringsetLenth){
                        builder.addItem("?????????"+(ij+1)+"??????");
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
        String mDateq=(mclass_cardmodel.getClass_date()!=null)?mclass_cardmodel.getClass_date():"???????????????";
        String Dateq=class_cardmodel.getClass_date();

        String msubject=(mclass_cardmodel.getClass_course()!=null)?mclass_cardmodel.getClass_course():"???";
        String subject=class_cardmodel.getClass_course();

        String mstartClass=(mclass_cardmodel.getClass_startClass()!=null)?mclass_cardmodel.getClass_startClass():"?????????";
        String startClass=class_cardmodel.getClass_startClass();

        String mtotal=(mclass_cardmodel.getClass_totalClass()!=null)?mclass_cardmodel.getClass_totalClass():"3???";
        String total=class_cardmodel.getClass_totalClass();


        String mplace=(mclass_cardmodel.getClass_classPlace()!=null)?mclass_cardmodel.getClass_classPlace():"???";
        String place=class_cardmodel.getClass_classPlace();

        String mcolor=(mclass_cardmodel.getLassColor()!=null)?mclass_cardmodel.getLassColor():"??????";
        String color=class_cardmodel.getLassColor();

        String mnotes=(mclass_cardmodel.getOtherNotes()!=null)?mclass_cardmodel.getOtherNotes():"???????????????????????????";
        String notes=class_cardmodel.getOtherNotes();


        if(mDateq.equals(Dateq)&&msubject.equals(subject)&&mstartClass.equals(startClass)&&mtotal.equals(total)&&mcolor.equals(color)&&mnotes.equals(notes)&&mplace.equals(place)){
            isSame=true;
        }
        return isSame;
        }


        private void checkClzz(int check,int position,Class_cardmodel new_data,int type){

            if(check==7|check%2==1){
                ToastUtils.show("????????????????????????????????????????????????????????????~ ????????????????????????~");
            }
            else if(check%2==0){
                switch (type){
                    case 0:
                        alls.remove(position);
                        alls.add(position,new_data);
                        multiTypeAdapter.notifyItemChanged(position,"updating");
                        popups.dismiss();
                        break;
                    case 1:
                        alls.add(new_data);
                        multiTypeAdapter.notifyDataSetChanged();
                        popups.dismiss();
                        break;
                }

            }else if(check==3){
                /*new QMUITipDialog.Builder(MainActivity.this)
                        .setTipWord("??????????????????????????????????????????5???~")
                        .create().show();*/
                ToastUtils.show("??????????????????????????????????????????5???~");
            }
        }




        private  int summerizeClums(){
            int isChange=FindSort.returnColorSort(Static_sets.start_classes,class_start_date.getText().toString());
            int Morning_nums_max=sp.getInt("Morning_nums_max",4);//morning_nums_max,??????????????????morning_nums_max+1??????,???????????????5?????????????????????5-1???
            int Noon_startClass_min=sp.getInt("Noon_startClass",5);
            int Night_startClass_min=sp.getInt("Night_startClass",9);
            int classLen=sp.getInt("classLen",12);


            int Morning_nums=0;
            if(isChange>(Noon_startClass_min-1)&&isChange<Night_startClass_min){
               // isChange=isChange-Noon_startClass_min;
                Morning_nums=Night_startClass_min-Noon_startClass_min;
            }else if(isChange>=Night_startClass_min){
                //isChange=isChange-Night_startClass_min;//isChange-Night_startClass_min;
                Morning_nums=classLen-Night_startClass_min;//Night_startClass_min??????????????????+1????????????????????????12????????????11??????????????????11+1
            }else{
                Morning_nums=Morning_nums_max+1;//Morning_num_max+1;
            }

            return  Morning_nums;
        }

        private int changeNums(){

        int isChange=FindSort.returnColorSort(Static_sets.start_classes,class_start_date.getText().toString());
        int Morning_nums_max=sp.getInt("Morning_nums_max",4);//morning_nums_max,??????????????????morning_nums_max+1??????,???????????????5?????????????????????5-1???
        int Noon_startClass_min=sp.getInt("Noon_startClass",5);
        int Night_startClass_min=sp.getInt("Night_startClass",9);
        int classLen=sp.getInt("classLen",12);

        int Morning_nums=0;
        if(isChange>(Noon_startClass_min-1)&&isChange<Night_startClass_min){
            isChange=isChange-Noon_startClass_min;
            Morning_nums=Night_startClass_min-Noon_startClass_min;
        }else if(isChange>=Night_startClass_min){
            isChange=isChange-Night_startClass_min;//isChange-Night_startClass_min;
            Morning_nums=classLen-Night_startClass_min;//Night_startClass_min??????????????????+1????????????????????????12????????????11??????????????????11+1
        }else{
            Morning_nums=Morning_nums_max+1;//Morning_num_max+1;

        }
        int chenageLen=checkNums(isChange,Morning_nums);
           return chenageLen;
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
                    lall=FilesUtil.readFileData(getApplicationContext(),"????????????");
                    if(lall.size()>0) {
                        for (int y = 0; y < lall.size(); y++) {
                            Object obj = (Object) lall.get(y);
                            alls.add(obj);
                        }
                        multiTypeAdapter.notifyDataSetChanged();
                    }
                    else{
                    ToastUtils.show("??????????????????????????????????????????????????????");
                }
                    break;
                case 1:
                    Intent iw=new Intent();
                    iw.setClass(MainActivity.this,Scheduldatas.class);
                    iw.putExtra("sche",Schedule_title.getText().toString().replace("?????????????????????",""));
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
                ToastUtils.show("??????????????????");
            }else {
                boolean isWrite= FilesUtil.writFile(getApplicationContext(),EditgetStr,jall);
                List<String> l=FilesUtil.readSchedulAndTimeTag(getApplicationContext());
                int y=0;
                for(String s: l){
                    if(s.split(",")[0].equals(EditgetStr)){
                       EditgetStr="";
                       y+=1;
                    }
                }

                pop.dismiss();
                if(isWrite){
                    if(y==0) {
                        String time_temp=sp.getString("current_time_temp","????????????????????????");
                        FilesUtil.AppendScheDulAndTimeTag(getApplicationContext(),EditgetStr+","+time_temp);
                    }
                    ToastUtils.show("???????????????????????????");
                }
                else {
                    ToastUtils.show("??????????????????????????????...????????????");
                }
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
                        int ngithStartCl=9;
                       String time_tag=timetags[position];
                       try {
                           List<String> schetimesl = FilesUtil.readTimeTag(getApplicationContext());
                           List<Class_cardmodel> wime=FilesUtil.readFileData(getApplicationContext(),values[position]);
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
                               morNums=FindSort.returnColorSort(Static_sets.class_nums,ptimes[2]);
                               noonStartCl=FindSort.returnColorSort(Static_sets.start_classes,ptimes[3]);
                               ngithStartCl=FindSort.returnColorSort(Static_sets.start_classes,ptimes[5]);

                               editor.putInt("Morning_nums_max",morNums);
                               editor.putInt("Noon_startClass",noonStartCl);
                               editor.putInt("Night_startClass",ngithStartCl);
                               editor.commit();
                           }
                       }catch (Exception e){
                           e.printStackTrace();
                       }
                       Schedule_title.setText("?????????????????????"+values[position]);
                       dialog.dismiss();
                    }
                });

        for(int f=0;f<values.length;f++){
            builder.addItem(values[f]);
        }

        builder.build().show();
    }

}


