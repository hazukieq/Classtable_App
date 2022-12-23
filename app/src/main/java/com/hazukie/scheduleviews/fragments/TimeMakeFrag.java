package com.hazukie.scheduleviews.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.drakeet.multitype.MultiTypeAdapter;
import com.hazukie.cskheui.Crialoghue.Clicks;
import com.hazukie.cskheui.Crialoghue.Crialoghue;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.binders.EditemBinder;
import com.hazukie.scheduleviews.binders.TimemakeBinder;
import com.hazukie.scheduleviews.fileutil.FileAssist;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.Fileystem;
import com.hazukie.scheduleviews.models.EditModel;
import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.TimeModel;
import com.hazukie.scheduleviews.models.TimemakeModel;
import com.hazukie.scheduleviews.models.Timetable;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.statics.Laytatics;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.FileHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeMakeFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeMakeFrag extends Fragment {
    private List<Object> all;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public TimeMakeFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimemakeFragment.
     */

    public static TimeMakeFrag newInstance(String param1, String param2) {
        TimeMakeFrag fragment = new TimeMakeFrag();
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
        View root=inflater.inflate(R.layout.fragment_timemake, container, false);

        MultiTypeAdapter mudp=new MultiTypeAdapter();
        all=new ArrayList<>();

        Button creatDocs=root.findViewById(R.id.frag_timemake_confirm);
        creatDocs.setOnClickListener(v->{
            assembleDatas(all);
        });

        RecyclerView recy=root.findViewById(R.id.frag_timemake_recy);
        recy.setHasFixedSize(true);


        all.add(new EditModel(0,"作息表文件名",""));
        all.add(new TimemakeModel(0,"上午时间段",new ArrayList<>()));
        all.add(new TimemakeModel(0,"下午时间段",new ArrayList<>()));
        all.add(new TimemakeModel(0,"晚上时间段",new ArrayList<>()));


        EditemBinder editemBinder=new EditemBinder();
        mudp.register(EditModel.class,editemBinder);
        TimemakeBinder timemakeBinder=new TimemakeBinder();
        timemakeBinder.setAddCalls(new TimemakeBinder.AddCalls() {
            @Override
            public void addSy(TimemakeModel timemakeModel) {
                Crialoghue coh=new Crialoghue.HeditBuilder()
                        .addTitle("编辑时间")
                        .addHint("请按 0800-0900 格式输入")
                        .onConfirm((crialoghue, view) -> {
                            EditText eidt=(EditText) view;
                            String contents=eidt.getText().toString();
                            if(contents.length()>=9){
                                int sizes=timemakeModel.unis.size();
                                int index_=all.indexOf(timemakeModel);
                                timemakeModel.unis.add(new Unimodel(sizes,contents));
                                mudp.notifyItemChanged(index_,"updating");
                                crialoghue.dismiss();
                            }else{
                                //eidt.setHint("输入内容太短了，请重新输入！");
                                DisplayHelper.Infost(getActivity(),"输入内容太短了，请重新输入！");
                            }
                        })
                        .build(getActivity());
                coh.show();
            }

            @Override
            public void quickAddSy(TimemakeModel timemakeModel) {
                StringBuilder stringBuilder=new StringBuilder();
                for (int i = 0; i < timemakeModel.unis.size(); i++) {
                    stringBuilder.append(timemakeModel.unis.get(i).title).append("\n");
                }
                Crialoghue criloh=new Crialoghue.HeditBuilder()
                        .addTitle("快捷输入")
                        .addHint("请输入每节课具体时间")
                        .addIsMultiEdit(true)
                        .addContents(stringBuilder.toString())
                        .addIsBottomVisible(true)
                        .addIsHtmlMode(true)
                        .addBottomContents("<b><font color=\"#1b88ee\">说明</font></b><br/>每节课的开始、结束时间，每行只能输入一节课时间。<br/>示例：<br/>0930-1045<br/>1145-1230<br/><br/><br/>时间输入格式为：<b>小时分钟-小时分钟</b><br/>例:0200-0420<br/>")
                        .onConfirm((crialoghue, view) -> {
                            EditText eidt=(EditText) view;
                            String contents=eidt.getText().toString();
                            String[] parsedContents=contents.split("\n");
                            List<Unimodel> _unis=new ArrayList<>();
                            for (int i = 0; i < parsedContents.length; i++) {
                                String tr=parsedContents[i].length()>=9?parsedContents[i].substring(0,9):parsedContents[i];
                                if(!tr.isEmpty()){
                                    _unis.add(new Unimodel(i,tr));
                                }
                            }

                            timemakeModel.unis.addAll(_unis);
                            int index_=all.indexOf(timemakeModel);
                            mudp.notifyItemChanged(index_,"updating");
                            crialoghue.dismiss();
                        })
                        .build(getActivity());

                criloh.setCanceledOnTouchOutside(false);
                criloh.show();
            }
        });
        mudp.register(TimemakeModel.class,timemakeBinder);
        recy.setAdapter(mudp);
        mudp.setItems(all);
        return root;
    }


    private void assembleDatas(List<Object> dasa){
        List<Timetable> tms=new ArrayList<>();
        List<Unimodel> mergeLs=new ArrayList<>();
        String docName="";
        EditModel editModel=(EditModel) dasa.get(0);
        if(!editModel.hintxt.isEmpty())docName=editModel.hintxt.trim()+".txt";

        int amStart=0,amCl=0,pmCl=0,mmStart=0,mmCl=0,totalNum=0;
        for (int i = 1; i < dasa.size(); i++) {
            TimemakeModel tim=(TimemakeModel) dasa.get(i);
            if(tim.title.equals("上午时间段")&&tim.unis.size()>0)amCl=tim.unis.size();

            if(tim.title.equals("下午时间段")&&tim.unis.size()>0){
                pmCl=tim.unis.size();
            }

            if(tim.title.equals("晚上时间段")&&tim.unis.size()>0){
                mmStart=amCl+pmCl;
                mmCl=tim.unis.size();
            }
            mergeLs.addAll(tim.unis);
        }

        for (int j = 0; j < mergeLs.size(); j++) {
            Unimodel uni_=mergeLs.get(j);
            if(!uni_.title.isEmpty()){
                String[] timez= uni_.title.split("-");
                String start=timez[0].replaceAll("(\\d{2})(\\d{2})","$1:$2");
                String end=timez[1].replaceAll("(\\d{2})(\\d{2})","$1:$2");
                Timetable tma=new Timetable(j,start,end);
                tms.add(tma);
            }
        }
        totalNum=amCl+pmCl+mmCl;
        TimeHeadModel timeHeadM=new TimeHeadModel(docName,totalNum,amStart,amCl,amCl,pmCl,mmStart,mmCl,tms);

        FileAssist.applyOftenOpts oftenOpts=new FileAssist.applyOftenOpts(getContext());
        //FileHelper fileHelper=new FileHelper(getActivity());
        try{
            boolean isDuplicate=false;
            List<TimeModel> timeModels=oftenOpts.getRecordTms();//fileHelper.read(FileHelper.RootMode.times,"time_index.txt",TimeModel.class);
           for(TimeModel tm:timeModels){
               //TimeModel tm=(TimeModel) obj;
               if (tm.timeName.equals(docName)||docName.equals("默认作息表.txt")) {
                   isDuplicate = true;
                   break;
               }
           }
            Log.i( "assembleDatas: ","docName="+(!docName.isEmpty())+", isDuplicate="+isDuplicate);
           if(!docName.isEmpty()&&!isDuplicate&&tms.size()>0){
               boolean isCreate= Fileystem.getInstance(getContext()).putDataz(FileRootTypes.times,docName,timeHeadM);//fileHelper.write(FileHelper.RootMode.times,docName,timeHeadM,false);
               boolean isWirte2Index=Fileystem.getInstance(getContext()).putDataz(FileRootTypes.times,"time_index.txt",new TimeModel(0,docName),true);//fileHelper.write(FileHelper.RootMode.times,"time_index.txt",new TimeModel(0,docName),true);

               if(isCreate&&isWirte2Index){
                   DisplayHelper.Infost(getActivity(),"创建成功！");
                   getActivity().finish();
               }else{
                   DisplayHelper.Infost(getActivity(),"创建失败！");
               }

           }
           else if(isDuplicate) {
               DisplayHelper.Infost(getActivity(),"文件已存在，请重新输入！");
           }else if(tms.size()==0){
               DisplayHelper.Infost(getActivity(),"您似乎还没有进行添加操作！");
           }else if(docName.isEmpty()){
               DisplayHelper.Infost(getActivity(),"文件名为空，请重新输入！");
           }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}