package com.hazukie.scheduleviews.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.activity.FragmentContainerAct;
import com.hazukie.scheduleviews.activity.ScheCreateActivity;
import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.FileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheCreateFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheCreateFrag extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ScheCreateFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheprevFrag.
     */

    public static ScheCreateFrag newInstance(String param1, String param2) {
        ScheCreateFrag fragment = new ScheCreateFrag();
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
        View root=inflater.inflate(R.layout.fragment_schecreate, container, false);

        EditText edit=root.findViewById(R.id.frag_schecreate_scheedit);
        Button confirm=root.findViewById(R.id.frag_schecreate_confirm);

        confirm.setOnClickListener(v->{
            String file_name=edit.getText().toString().replaceAll("\\s*","")+".txt";
            writInits(file_name);
        });
        return root;
    }

    //写入课表文件数据
    public void writInits(String sche_file_name){
        FragmentContainerAct fragmentContainerAct=(FragmentContainerAct) getActivity();
        List<ClassLabel> main_list = fragmentContainerAct.getObjs();
        String globalTime = fragmentContainerAct.getParam();
        Log.i("write>>>",""+ main_list.size()+",str="+ globalTime);

        FileHelper fileHelper=FileHelper.getInstance(getContext());
        try{
            //获取所有课表名字，和其比较是否重复
            List<ScheWithTimeModel> recorded_sches=FileHelper.getRecordedScts(getContext());
            //List<ScheModel> recorded_sche=FileHelper.getRecordScms(getContext());
            boolean isDuplicate=false;
            for(ScheWithTimeModel sche:recorded_sches){
                if (sche.scheName.equals(sche_file_name)) {
                    isDuplicate = true;
                    break;
                }
            }

           // Log.i("writInits: ","isDup="+(!isDuplicate)+", mains="+(main_list.size()>0));
            if(!isDuplicate&& main_list.size()>0){
                boolean isCreate=fileHelper.write(FileHelper.RootMode.sches,sche_file_name,new ArrayList<>(main_list));
                recorded_sches.add(new ScheWithTimeModel(recorded_sches.size(),sche_file_name, globalTime));
               // recorded_sche.add(new ScheModel(recorded_sche.size(),sche_file_name));
                //boolean isWrite2ScheIndex=fileHelper.write(FileHelper.RootMode.sches,"sche_index.txt",new ArrayList<>(recorded_sche));
                boolean isWrite2Index=fileHelper.write(FileHelper.RootMode.index,"index.txt",new ArrayList<>(recorded_sches));

                if(isCreate&&isWrite2Index){
                    Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                    ScheCreateActivity.instance.finish();
                    //关闭上一个活动！
                    TimerTask task=new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().finish();
                            Log.i("fragAct>>","closed!");
                        }
                    };
                    Timer timer=new Timer();
                    timer.schedule(task,300);

                }else{
                    Toast.makeText(getContext(), "保存失败！", Toast.LENGTH_SHORT).show();
                }
            }else if(main_list.size()==0){
                DisplayHelper.Infost(getContext(),"您似乎没有进行添加操作！");
            }else {
                DisplayHelper.Infost(getContext(),"文件已存在，请重新输入!");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}