package com.hazukie.scheduleviews.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.MultiTypeAdapter;
import com.hazukie.cskheui.Crialoghue.Crialoghue;
import com.hazukie.cskheui.LeditView.LeditView;
import com.hazukie.cskheui.LetxtView.LetxtView;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.FragmentContainerAct;
import com.hazukie.scheduleviews.activity.ScheEditActivity;
import com.hazukie.scheduleviews.binders.HorionCardBinder;
import com.hazukie.scheduleviews.binders.UniBinder;
import com.hazukie.scheduleviews.fileutil.BasicOpts;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.OftenOpts;
import com.hazukie.scheduleviews.models.HoricardModel;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeHeadModel;
import com.hazukie.scheduleviews.models.TimeModel;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.PopupUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheManageFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheManageFrag extends Fragment {
    private RecyclerView recy;
    private LinearLayout emptyLay;
    private MultiTypeAdapter mdp;
    private List<Object> mobs;
    //private List<ScheWithTimeModel> delete_list;
    private List<ScheWithTimeModel> scts;


    //private FileHelper fileHelper;
    private OftenOpts oftenOpts;
    private BasicOpts basicOpts;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ScheManageFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheManageFrag.
     */
    public static ScheManageFrag newInstance(String param1, String param2) {
        ScheManageFrag fragment = new ScheManageFrag();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_sche_manage, container, false);
        recy=v.findViewById(R.id.frag_sche_manage_recy);
        emptyLay=v.findViewById(R.id.frag_sche_empty);
        TextView addView=v.findViewById(R.id.frag_sche_manage_add);

        //fileHelper=new FileHelper(getActivity());
        oftenOpts=OftenOpts.getInstance(getContext());
        basicOpts=BasicOpts.getInstance(getContext());

        //跳转新界面创建课表文件
        addView.setOnClickListener(v1 -> FragmentContainerAct.startActivityWithLoadUrl(getActivity(), ScheCreateBeforeFrag.class));


        //获取记录在案的所有文件数据
        scts= oftenOpts.getRecordedScts();//FileHelper.getRecordedScts(getActivity());

        HorionCardBinder horionCardBinder=new HorionCardBinder();
        //设置文件详情展开功能
        horionCardBinder.setExpandListener((v12, conView, descript) -> {
            if(conView.getVisibility()==View.GONE) conView.setVisibility(View.VISIBLE);
            else conView.setVisibility(View.GONE);
        });

        //设置删除文件功能
        horionCardBinder.setDelListener((v13, hrc) -> {
            int indx_=mobs.indexOf(hrc);
            Crialoghue cih=new Crialoghue.TxtBuilder()
                    .addTitle("课表文件")
                    .addContent("确定删除此课表文件吗?")
                    .onConfirm((cDialoh, view) -> {
                        for(ScheWithTimeModel sct:scts){
                            if(sct.getScheName().equals(hrc.title)){
                                mobs.remove(hrc);
                                mdp.notifyItemRemoved(indx_);
                                executeDel(sct);
                                break;
                            }
                        }
                        DisplayHelper.Infost(getActivity(),"删除成功！");
                        controlEmpty();
                        cDialoh.dismiss();
                    })
                    .build(getActivity());
            cih.show();
        });

        //重命名、更改关联作息表
        horionCardBinder.setSettingListener((v14, hrc) -> showSettinialoh(hrc));
        //打开课表文件编辑及保存
        horionCardBinder.setOnOpenDoc((sche, time) -> ScheEditActivity.startActivityWithData(getActivity(),sche,time));

        mobs=new ArrayList<>();
        mdp=new MultiTypeAdapter();
        mdp.register(HoricardModel.class,horionCardBinder);

        recy.setAdapter(mdp);
        mdp.setItems(mobs);

        controlEmpty();
        return v;
    }



    //打开编辑对话框
    private void showSettinialoh(HoricardModel horic){
        ViewGroup.LayoutParams lps=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout firedit=new LeditView.Builder().addLeftile("课表名称").addContent(horic.title).create(getActivity());

        LinearLayout secedit=new LetxtView.Builder()
                .addLeftile("关联作息")
                .addRightile(horic.subtitle)
                .addInterceptor((textView, textView1, linearLayout) -> textView1.setOnClickListener(v-> onEditTime(horic,textView1)))
                .create(getActivity());

        LinearLayout doubledit=new LinearLayout(getActivity());

        doubledit.setOrientation(LinearLayout.VERTICAL);
        doubledit.setLayoutParams(lps);

        doubledit.addView(firedit);
        doubledit.addView(secedit);

        Crialoghue croh=new Crialoghue.CustomBuilder()
                .addTitle("编辑课表")
                .addView(doubledit)
                .onConfirm((crialoghue, viewGroup) -> {
                    try{
                        boolean isDuplicate=false;
                        LinearLayout root= (LinearLayout) viewGroup.getChildAt(0);
                        LinearLayout fireditLay=(LinearLayout)root.getChildAt(0);

                        EditText editV= (EditText) fireditLay.getChildAt(1);

                        String mSchName=editV.getText().toString().replaceAll("\\s*","");
                        if(mSchName.equals(horic.getTitle())){
                            mdp.notifyItemChanged(mobs.indexOf(horic),"updating");
                            crialoghue.dismiss();
                        }else{
                            if(mSchName.length()>0){
                                for(ScheWithTimeModel sct:scts){
                                    if(sct.getScheName().equals(mSchName)){
                                        isDuplicate=true;
                                        break;
                                    }
                                }

                                if(!isDuplicate){
                                    boolean isRename=basicOpts.rename(FileRootTypes.sches,horic.title+".txt",mSchName+".txt");//fileHelper.rename(FileHelper.RootMode.sches,horic.title+".txt",mSchName+".txt");
                                    Log.i( "showSettinialoh>>","isRename="+isRename);
                                    if(isRename){
                                        horic.title=mSchName;
                                        mdp.notifyItemChanged(mobs.indexOf(horic),"updating");
                                        refreshScts();
                                        crialoghue.dismiss();
                                    }else{
                                        DisplayHelper.Infost(getContext(),"修改文件名失败！");
                                    }

                                }else{
                                    DisplayHelper.Infost(getActivity(),"文件名已重复，请重新输入！");
                                }
                            }else{
                                DisplayHelper.Infost(getActivity(),"字数不能为0，请重新输入！ ");
                            }
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                })
                .build(getActivity());
        croh.show();
    }


    /*
    对话框中作息表的逻辑处理
    可以更换作息表
     */
    private void onEditTime(HoricardModel horic,TextView txt){
        PopupUtil pou=new PopupUtil(getContext());
        PopupWindow pouwin=pou.initDefaultPopup(120);
        pou.initDefaultViews((textView, recy, multiAdp, viewlist) -> {
            String mSelectedTime=txt.getText().toString();
            recy.setMaxHeight(200);
            textView.setText("默认作息表");
            textView.setOnClickListener(v->{
                txt.setText("默认作息表");
                horic.subtitle="默认作息表";
                horic.description=getTimedescription("默认作息表");
                if(!mSelectedTime.equals("默认作息表")) refreshScts();
                pouwin.dismiss();
            });
            UniBinder uni=new UniBinder();
            uni.setClickListener((v, uni1) -> {
                horic.subtitle=uni1.title;
                horic.description=getTimedescription(uni1.title);
                txt.setText(uni1.title);
                if(!mSelectedTime.equals(uni1.title)) refreshScts();
                pouwin.dismiss();
            });

            multiAdp.register(Unimodel.class,uni);
            List<TimeModel> tims= oftenOpts.getRecordTms();//FileHelper.getRecordTms(getActivity());
            for(int i=0;i<tims.size();i++){
                TimeModel ti=tims.get(i);
                viewlist.add(new Unimodel(ti.id,ti.getTimeName()));
            }
        });
        pouwin.showAsDropDown(txt,0,0,Gravity.END);
    }


    private String getTimedescription(String name){
        String sdes="";
        TimeHeadModel thdm=oftenOpts.getThm(name+".txt");//(TimeHeadModel) thm;
        if(thdm!=null) sdes=thdm.outputBasics();
 /*       try {
            //Object thm=FileHelper.getInstance(getContext()).readObj(FileHelper.RootMode.times,name+".txt",TimeHeadModel.class);
            TimeHeadModel thdm=oftenOpts.getThm(name+".txt");//(TimeHeadModel) thm;
            if(thdm!=null) sdes=thdm.outputBasics();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return sdes;
    }

    //处理删除列表中的数据
    //写入修改后数据
    private void executeDel(ScheWithTimeModel del){
        boolean isDel=basicOpts.delete(FileRootTypes.sches,del.scheName);//fileHelper.delete(FileHelper.RootMode.sches,del.scheName);
        Log.i( "ExcecuteDel>>>","delete_item= "+del.scheName+" status="+isDel);
        refreshScts();
    }

    //刷新数据，并将数据写入文件
    public void refreshScts(){
        List<ScheWithTimeModel> neo_scts=new ArrayList<>();
        for(Object obj:mobs){
            HoricardModel hor=(HoricardModel)obj;
            neo_scts.add(new ScheWithTimeModel(mobs.indexOf(obj),hor.title+".txt",hor.subtitle+".txt"));
            Log.i("ScheManageFrag>>","time="+hor.subtitle);
        }
        oftenOpts.putRawSctList(neo_scts);
    }

    private void controlEmpty(){
        if(mobs.size()>0){
            recy.setVisibility(View.VISIBLE);
            emptyLay.setVisibility(View.GONE);
        }else{
            recy.setVisibility(View.GONE);
            emptyLay.setVisibility(View.VISIBLE);
        }
    }

    //数据更新
    public void reshData(){
        if(mobs!=null&&scts!=null){
            mobs.clear();
            scts.clear();
            scts.addAll(oftenOpts.getRecordedScts());//FileHelper.getRecordedScts(getActivity()));
            if(scts.size()>0&&scts.get(0)!=null){
                for(int i=0;i<scts.size();i++){
                    ScheWithTimeModel sct=scts.get(i);
                    String sdes=getTimedescription(sct.getTimeName());
                    mobs.add(new HoricardModel(i,sct.getScheName(),sct.getTimeName(),sdes));
                }
            }

            mdp.notifyDataSetChanged();
            controlEmpty();
            Log.i("onResume>>>","sct_datas has updating!");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reshData();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}