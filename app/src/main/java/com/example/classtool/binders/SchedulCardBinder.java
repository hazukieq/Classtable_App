package com.example.classtool.binders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.example.classtool.R;
import com.example.classtool.base.OnItemClick;
import com.example.classtool.models.SchedulModel;

public class SchedulCardBinder extends ItemViewBinder<SchedulModel,SchedulCardBinder.SVH> {
    private OnScheClick onScheClick=null;

    public interface OnScheClick{
        void  onClick(TextView v1,TextView v2,int position,int sirtq,String sche,String time);
    }


    public void setOnScheClick(OnScheClick onScheClick) {
        this.onScheClick = onScheClick;
    }

    @NonNull
    @Override
    public SVH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new SVH(layoutInflater.inflate(R.layout.change_current_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SVH svh, SchedulModel qTime) {
         svh.sche.setText(qTime.getSche());
         svh.time.setText(qTime.getTime());

         svh.sche.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(onScheClick!=null){
                    onScheClick.onClick(svh.sche,svh.time,svh.getAbsoluteAdapterPosition(), qTime.getSort(), qTime.getSche(), qTime.getTime());
                 }
             }
         });
    }

    public class SVH extends RecyclerView.ViewHolder{
        TextView sche,time;

        public SVH(@NonNull View itemView) {
            super(itemView);
            sche=(TextView) itemView.findViewById(R.id.change_current_sche);
            time=(TextView) itemView.findViewById(R.id.change_current_time);
        }
    }
}
