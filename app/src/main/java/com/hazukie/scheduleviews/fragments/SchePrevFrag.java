package com.hazukie.scheduleviews.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.drakeet.multitype.MultiTypeAdapter;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.activity.FragmentContainerAct;
import com.hazukie.scheduleviews.activity.ManageAct;
import com.hazukie.scheduleviews.activity.ScheCreateActivity;
import com.hazukie.scheduleviews.binders.IOSItemBinder;
import com.hazukie.scheduleviews.binders.UniBinder;
import com.hazukie.scheduleviews.custom.CRecyclerView;
import com.hazukie.scheduleviews.custom.TopbarLayout;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeModel;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.FileHelper;
import com.hazukie.scheduleviews.utils.PopupUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SchePrevFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchePrevFrag extends Fragment {
    private List<Object> objs;
    private MultiTypeAdapter adp;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SchePrevFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchemakeFrag.
     */
    public static SchePrevFrag newInstance(String param1, String param2) {
        SchePrevFrag fragment = new SchePrevFrag();
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
       View v= inflater.inflate(R.layout.fragment_scheprev, container, false);
        TopbarLayout topbarLayout=v.findViewById(R.id.frag_topbar);
        topbarLayout.setTitle("请选择作息表");


        CRecyclerView cRecyclerView=v.findViewById(R.id.frag_recy);
        objs=new ArrayList<>();
        adp=new MultiTypeAdapter();
        UniBinder uniBinder=new UniBinder();
        uniBinder.setJustify(txt->{
            int pa= DisplayHelper.dp2px(getContext(),8);
            txt.setGravity(Gravity.LEFT);
            txt.setPadding(pa,0,pa,pa);
            txt.setTextSize(14);
            txt.setBackground(null);
            txt.setTextColor(getContext().getColor(R.color.text_gray));
        });

        uniBinder.setClickListener((v12, uni) -> {
            if(uni.id==1) FragmentContainerAct.startAct2Activity(getActivity(), ManageAct.class);
        });
        IOSItemBinder iosItemBinder=new IOSItemBinder();
        iosItemBinder.setItemGravity(Gravity.RIGHT);
        int paddingR=DisplayHelper.dp2px(getContext(),12);
        iosItemBinder.setItemPadding(0,0,paddingR);
        iosItemBinder.setClickListener((v1, scheWithTimeModel) -> {
            if(scheWithTimeModel.id==0) popupList(v1);
        });


        objs.add(new ScheWithTimeModel(0,"作息表名称","默认作息表"));
        objs.add(new Unimodel(0,"<br/><font color=\"#1b88ee\">温馨提示</font><br/>请选择作息表，其将会与课表绑定"));
        objs.add(new Unimodel(1,"如需更改课表绑定的作息表，请您前往&nbsp;<b>课表设置管理界面</b>"));
        cRecyclerView.setAdapter(adp);
        adp.register(Unimodel.class,uniBinder);
        adp.register(ScheWithTimeModel.class,iosItemBinder);
        //adp.register(EditModel.class,new EditemBinder());
        adp.setItems(objs);


        Button btn=v.findViewById(R.id.frag_confirm);
        btn.setOnClickListener(v2->{
            String timeN=((ScheWithTimeModel) objs.get(0)).timeName;
            if(!timeN.contains(".txt")) timeN=timeN+".txt";
            ScheCreateActivity.startActivityWithData(getActivity(),timeN);
        });
       return v;
    }

    public  void popupList(View attatchedView) {

        PopupUtil popupUtil = new PopupUtil(getContext());
        PopupWindow p = popupUtil.initDefaultPopup(120);
        p.showAsDropDown(attatchedView,0,0,Gravity.RIGHT);
        popupUtil.initDefaultViews((txt, recy_, muDp, viewList) -> {
            txt.setText(Html.fromHtml("<b><font color=\"##1b88ee\">创建新作息表</font></b>"));
            txt.setOnClickListener(v->
                FragmentContainerAct.startActivityWithLoadUrl(getActivity(), TimeMakeFrag.class));//"com.hazukie.scheduleviews.fragments.TimemakeFragment"));

            List<TimeModel> tms=FileHelper.getRecordTms(getActivity());

            int pa= DisplayHelper.dp2px(getContext(),8);
            recy_.setPadding(pa,pa,pa,pa);

            for (TimeModel tm:tms) {
                viewList.add(new Unimodel(0,tm.getTimeName()));
            }

            viewList.add(new Unimodel(0,"默认作息表"));
            UniBinder unib = new UniBinder();
            unib.setClickListener((v, uni) -> {
                TextView t=(TextView)attatchedView;
                ScheWithTimeModel uin=(ScheWithTimeModel)objs.get(0);
                uin.timeName=uni.title+".txt";
                t.setText(""+uni.title);
                p.dismiss();
            });
            muDp.register(Unimodel.class, unib);
        });
    }

}