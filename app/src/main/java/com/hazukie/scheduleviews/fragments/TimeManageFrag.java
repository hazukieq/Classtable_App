package com.hazukie.scheduleviews.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drakeet.multitype.MultiTypeAdapter;
import com.hazukie.cskheui.Crialoghue.Clicks;
import com.hazukie.cskheui.Crialoghue.Crialoghue;
import com.hazukie.cskheui.LeditView.LeditView;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.activity.FragmentContainerAct;
import com.hazukie.scheduleviews.activity.TimeditActivity;
import com.hazukie.scheduleviews.binders.TimeItemBinder;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeModel;
import com.hazukie.scheduleviews.models.Timetable;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.statics.Laytatics;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.CipherInputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeManageFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeManageFrag extends Fragment {
    private List<Object> recyItems;
    private MultiTypeAdapter recyAdp;
    private RecyclerView recy;
    private LinearLayout emptyLay;

    private FileHelper fileHelper;
    private List<ScheWithTimeModel> sctz;
    private List<TimeModel> timez;
    private List<TimeModel> delete_list;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TimeManageFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeManageFrag.
     */
    public static TimeManageFrag newInstance(String param1, String param2) {
        TimeManageFrag fragment = new TimeManageFrag();
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
        View root= inflater.inflate(R.layout.fragment_time_manage, container, false);
        TextView addV=root.findViewById(R.id.frag_time_manage_add);
        addV.setOnClickListener(v-> FragmentContainerAct.startActivityWithLoadUrl(getActivity(), TimeMakeFrag.class,true));

        recy=root.findViewById(R.id.frag_time_manage_recy);
        emptyLay=root.findViewById(R.id.frag_time_empty);

        fileHelper=new FileHelper(getActivity());
        recyItems=new ArrayList<>();
        sctz=new ArrayList<>();
        timez=new ArrayList<>();
        delete_list=new ArrayList<>();


        sctz=FileHelper.getRecordedScts(getActivity());
        timez= FileHelper.getRecordTms(getActivity());
        for (int i = 0; i < timez.size(); i++) {
            recyItems.add(new Unimodel(i,timez.get(i).getTimeName()));
        }

        recyAdp=new MultiTypeAdapter();



        TimeItemBinder timeItemBinder=new TimeItemBinder();
        timeItemBinder.setItemClick((v,uni) -> TimeditActivity.startActivityWithData(getActivity(),uni.title+".txt"));
        timeItemBinder.setItemCall(new TimeItemBinder.ItemCall() {
            @Override
            public void doDelete(Unimodel uni) {
                Crialoghue cioh=new Crialoghue.TxtBuilder()
                        .addTitle("作息表文件")
                        .addContent("确定删除此作息表文件吗？")
                        .onConfirm((cRialog, rootView) -> {
                            TimeModel tim=new TimeModel(uni.id, uni.title+".txt");
                            delete_list.add(tim);
                            for(ScheWithTimeModel sct:sctz){
                                if(sct.getTimeName().equals(uni.getTitle())){
                                    sct.restoreTimeName();
                                }
                            }
                            timez.remove(tim);
                            recyItems.remove(uni);
                            recyAdp.notifyDataSetChanged();
                            DisplayHelper.Infost(getActivity(),"删除成功！");
                            cRialog.dismiss();
                            controlEmpty();
                        })
                        .build(getActivity());
                cioh.show();
            }

            @Override
            public void doEdit(TextView txt, Unimodel uni) {
                try{
                    Crialoghue coh=new Crialoghue.HeditBuilder()
                            .addTitle("编辑作息表")
                            .addHint("请输入新的文件名")
                            .addContents(uni.title)
                            .onConfirm((crialoghue, view) -> {
                                EditText ed=(EditText) view;
                                String content=ed.getText().toString().replaceAll("\\s*","");
                                List<TimeModel> tms=FileHelper.getRecordTms(getActivity());
                                boolean isDuplicate=false;
                                for(TimeModel tim:timez){
                                    if((tim.getTimeName().equals(content)&&(!content.equals(uni.title)))||content.equals("默认作息表")){
                                        isDuplicate=true;
                                        break;
                                    }
                                }

                                if(!isDuplicate){
                                    boolean isRename=fileHelper.rename(FileHelper.RootMode.times, uni.title+".txt", content+".txt");
                                    Log.i( "doEdit>>","isRename="+isRename);

                                    for(ScheWithTimeModel sct:sctz){
                                        if(sct.getTimeName().equals(uni.title)){
                                            sct.updateTimeName(content);
                                        }
                                    }

                                    txt.setText(content);
                                    uni.title=content;
                                    crialoghue.dismiss();
                                }else{
                                    DisplayHelper.Infost(getActivity(),"名称已重复！");
                                }
                            })
                            .build(getActivity());
                    coh.show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        recyAdp.register(Unimodel.class,timeItemBinder);
        recy.setAdapter(recyAdp);
        recyAdp.setItems(recyItems);
        controlEmpty();
        return root;
    }

    private void controlEmpty(){
        if(recyItems.size()>0){
            recy.setVisibility(View.VISIBLE);
            emptyLay.setVisibility(View.GONE);
        }else{
            recy.setVisibility(View.GONE);
            emptyLay.setVisibility(View.VISIBLE);
        }
    }

    private void executeDels(List<TimeModel> dels){
        try {
            for(TimeModel del:dels){
                fileHelper.delete(FileHelper.RootMode.times,del.timeName);
            }

            List<TimeModel> neo_tims=new ArrayList<>();
            for(Object obj:recyItems){
                Unimodel uni=(Unimodel)obj;
                TimeModel tim=new TimeModel(uni.id, uni.getTitle()+".txt");
                neo_tims.add(tim);
            }
            fileHelper.write(FileHelper.RootMode.index,"index.txt",new ArrayList<>(sctz));
            fileHelper.write(FileHelper.RootMode.times, "time_index.txt", new ArrayList<>(neo_tims));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        executeDels(delete_list);
    }

    @Override
    public void onResume() {
        super.onResume();

        sctz.clear();
        timez.clear();
        recyItems.clear();
        sctz.addAll(FileHelper.getRecordedScts(getActivity()));
        timez.addAll(FileHelper.getRecordTms(getActivity()));
        for (int i = 0; i < timez.size(); i++) {
            recyItems.add(new Unimodel(i,timez.get(i).getTimeName()));
        }
        recyAdp.notifyDataSetChanged();
        controlEmpty();
        Log.i("onResume>>>","timez="+timez.size());
    }
}