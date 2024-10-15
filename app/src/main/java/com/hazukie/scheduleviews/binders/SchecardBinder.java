package com.hazukie.scheduleviews.binders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.statics.ColorSeletor;

import java.util.List;

public class SchecardBinder extends ItemViewBinder<ClassLabel, SchecardBinder.VH> {
    private BinderClickListener binderClickListener;

    public void setBinderClickListener(BinderClickListener binderClickListener) {
        this.binderClickListener = binderClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new VH(layoutInflater.inflate(R.layout.recy_schecard, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, ClassLabel cls) {
        vh.itemView.setOnClickListener(v->{
            if(binderClickListener!=null) binderClickListener.onEdit(vh.itemView,cls);
        });

        vh.editV.setOnClickListener(v->{
            if(binderClickListener!=null) binderClickListener.onEdit(vh.editV,cls);
        });

        vh.deleteV.setOnClickListener(v->{
            if(binderClickListener!=null) binderClickListener.onDelete(vh.itemView,cls);
        });

        vh.weektime.setText(cls.addWeek()+cls.addDetailTime());//Statics.getWeekByIndex(cls.week)+Statics.getDetailTimeByIndex(cls.detailTime));
        Log.i("...>>",cls.addWeek());
        vh.courseV.setText(cls.subjectName);
        vh.startCl.setText(cls.addStartCl());//"第"+(cls.startCl+1)+"节课");
        vh.totalNum.setText(cls.addClNums());//cls.clNums+"课时");
        vh.placeV.setText(cls.addClRoom());//"@"+cls.clRoom);
        vh.planoteV.setText(cls.plaNote);
        vh.colorV.setBackgroundColor(vh.itemView.getResources().getColor(ColorSeletor.getColorByIndex(cls.color)));
        vh.colorV.setText(cls.addColorStr());//ColorSeletor.getColorStringByIndex(cls.color));
        vh.oddevenV.setText(cls.getOEWeek());
        //vh.oddevenV.setVisibility(cls.getOEWeek().equals("")?View.GONE:View.VISIBLE);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, ClassLabel cls, @NonNull List<?> payloads) {
        super.onBindViewHolder(vh, cls, payloads);
        if(!payloads.isEmpty()) {
            String isLoad = payloads.get(0).toString();
            if(isLoad.equals("updating")){
                vh.weektime.setText(cls.addWeek()+cls.addDetailTime());
                vh.courseV.setText(cls.subjectName);
                vh.startCl.setText(cls.addStartCl());
                vh.totalNum.setText(cls.addClNums());
                vh.placeV.setText(cls.addClRoom());
                vh.planoteV.setText(cls.plaNote);
                vh.colorV.setBackgroundColor(vh.itemView.getResources().getColor(ColorSeletor.getColorByIndex(cls.color)));
                vh.colorV.setText(cls.addColorStr());
                vh.oddevenV.setText(cls.getOEWeek());
                vh.oddevenV.setVisibility(cls.getOEWeek().equals("")?View.GONE:View.VISIBLE);
            }
        }
    }

    public static class VH extends RecyclerView.ViewHolder{

        private TextView weektime,startCl,totalNum,colorV,planoteV,placeV,courseV,editV,deleteV,oddevenV;
        public VH(@NonNull View itemView) {
            super(itemView);
            weektime=itemView.findViewById(R.id.class_info_card_date);
            startCl=itemView.findViewById(R.id.class_info_card_startClass);
            totalNum=itemView.findViewById(R.id.class_info_card_totalClass);
            colorV=itemView.findViewById(R.id.class_info_card_classColor);
            planoteV=itemView.findViewById(R.id.class_info_card_classNotes);
            placeV=itemView.findViewById(R.id.class_info_card_classPlace);
            courseV=itemView.findViewById(R.id.class_info_card_course);
            editV=itemView.findViewById(R.id.class_info_card_edit);
            deleteV=itemView.findViewById(R.id.class_info_card_delete);
            oddevenV=itemView.findViewById(R.id.class_info_card_oddeven);
        }
    }
}
