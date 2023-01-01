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
import com.hazukie.cskheui.Crialoghue.Crialoghue;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.FragmentContainerAct;
import com.hazukie.scheduleviews.activity.TimeEditActivity;
import com.hazukie.scheduleviews.binders.TimeItemBinder;
import com.hazukie.scheduleviews.fileutil.BasicOpts;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.Fileystem;
import com.hazukie.scheduleviews.fileutil.OftenOpts;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeModel;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.utils.DisplayHelper;

import java.util.ArrayList;
import java.util.List;

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

    private OftenOpts oftenOpts;
    private BasicOpts basicOpts;
    private List<ScheWithTimeModel> sctz;
    private List<TimeModel> timez;


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
        addV.setOnClickListener(v-> FragmentContainerAct.startActivityWithLoadUrl(getActivity(), TimeCreateFrag.class));

        recy=root.findViewById(R.id.frag_time_manage_recy);
        emptyLay=root.findViewById(R.id.frag_time_empty);


        oftenOpts=OftenOpts.getInstance(getContext());
        basicOpts=BasicOpts.getInstance(getContext());

        recyItems=new ArrayList<>();
        sctz=new ArrayList<>();
        timez=new ArrayList<>();

        sctz=oftenOpts.getRecordedScts();
        timez=oftenOpts.getRecordTms();
        for (int i = 0; i < timez.size(); i++) {
            recyItems.add(new Unimodel(i,timez.get(i).getTimeName()));
        }

        recyAdp=new MultiTypeAdapter();



        TimeItemBinder timeItemBinder=new TimeItemBinder();
        timeItemBinder.setItemClick((v,uni) -> TimeEditActivity.startActivityWithData(getActivity(),uni.title+".txt"));
        timeItemBinder.setItemCall(new TimeItemBinder.ItemCall() {
            @Override
            public void doDelete(Unimodel uni) {
                Crialoghue cioh=new Crialoghue.TxtBuilder()
                        .addTitle("作息表文件")
                        .addContent("确定删除此作息表文件吗？")
                        .onConfirm((cRialog, rootView) -> {
                            TimeModel tim=new TimeModel(uni.id, uni.title+".txt");
                            for(ScheWithTimeModel sct:sctz){
                                if(sct.getTimeName().equals(uni.getTitle())){
                                    sct.restoreTimeName();
                                }
                            }
                            timez.remove(tim);
                            int removeItem_index= recyItems.indexOf(uni);
                            recyItems.remove(uni);
                            recyAdp.notifyItemRemoved(removeItem_index);
                            executeDel(tim);
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
                            .addIsBottomVisible(true)
                            .addBottomContents("文件名不能和“默认作息表相同”")
                            .onConfirm((crialoghue, view) -> {
                                EditText ed=(EditText) view;
                                String content=ed.getText().toString().replaceAll("\\s*","");
                                boolean isNoChange=content.equals(uni.title)||content.equals("默认作息表");
                                boolean isDuplicate=false;

                                if(isNoChange){
                                    crialoghue.dismiss();
                                }else if(!content.isEmpty()){
                                    for(TimeModel tim:timez){
                                        if((tim.getTimeName().equals(content))){
                                            isDuplicate=true;
                                            break;
                                        }
                                    }
                                    if(!isDuplicate){
                                        boolean isRename=basicOpts.rename(FileRootTypes.times,uni.title+".txt", content+".txt");
                                        Log.i( "doEdit>>","isRename="+isRename);
                                        if(isRename){
                                            for(ScheWithTimeModel sct:sctz){
                                                if(sct.getTimeName().equals(uni.title)){
                                                    sct.updateTimeName(content+".txt");
                                                }
                                            }

                                            txt.setText(content);
                                            uni.title=content;
                                            refreshThm();
                                            crialoghue.dismiss();
                                        }else{
                                            DisplayHelper.Infost(getContext(),"修改文件名失败！");
                                        }

                                    }else{
                                        DisplayHelper.Infost(getActivity(),"名称已重复！");
                                    }
                                }else{
                                    DisplayHelper.Infost(getActivity(),"文件名不能为空！");
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

    private void executeDel(TimeModel del){
        try {
            basicOpts.delete(FileRootTypes.times,del.timeName);//fileHelper.delete(FileHelper.RootMode.times,del.timeName);
            Fileystem.getInstance(getContext()).putDataList(FileRootTypes.times,"time_index.txt",new ArrayList<>(getCurentThmList()));
            oftenOpts.putRawSctList(sctz);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<TimeModel> getCurentThmList(){
        List<TimeModel> neo_tims=new ArrayList<>();
        if(recyItems.size()>0){
            for(Object obj:recyItems){
                Unimodel uni=(Unimodel)obj;
                TimeModel tim=new TimeModel(uni.id, uni.getTitle()+".txt");
                neo_tims.add(tim);
            }
        }
        return neo_tims;
    }

    private void refreshThm(){
        try{
            oftenOpts.putRawSctList(sctz);
            Fileystem.getInstance(getContext()).putDataList(FileRootTypes.times,"time_index.txt",new ArrayList<>(getCurentThmList()));
            timez.clear();
            timez.addAll(oftenOpts.getRecordTms());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        sctz.clear();
        timez.clear();
        recyItems.clear();
        sctz.addAll(oftenOpts.getRecordedScts());
        timez.addAll(oftenOpts.getRecordTms());
        for (int i = 0; i < timez.size(); i++) {
            recyItems.add(new Unimodel(i,timez.get(i).getTimeName()));
        }
        recyAdp.notifyDataSetChanged();
        controlEmpty();
        Log.i("onResume>>>","timez="+timez.size());
    }
}