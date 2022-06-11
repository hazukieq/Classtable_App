package com.example.classtool.binders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.example.classtool.R;
import com.example.classtool.models.Class_cardmodel;

import java.util.List;

public class Class_cardBinder extends ItemViewBinder<Class_cardmodel,Class_cardBinder.CVH> {


    private Class_cardBinder.OnItemClickListener onItemClickListener=null;
    public interface OnItemClickListener{
        void onItemClick(View v,int position,Class_cardmodel class_cardmodel);
        void onItemDelete(View v,int position);
    }

    public void setOnItemClickListener(Class_cardBinder.OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }



    @NonNull
    @Override
    public CVH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new CVH(layoutInflater.inflate(R.layout.class_info_card_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CVH cvh, Class_cardmodel class_cardmodel) {
        cvh.course.setText(class_cardmodel.getClass_course());
        cvh.date.setText(class_cardmodel.getClass_date());
        cvh.Startclass.setText(class_cardmodel.getClass_startClass());
        cvh.Totalclass.setText(class_cardmodel.getClass_totalClass());
        cvh.Classplace.setText(class_cardmodel.getClass_classPlace());
        cvh.lassColor.setText(class_cardmodel.getLassColor());

        if (class_cardmodel.getOtherNotes().equals("")|class_cardmodel.getOtherNotes().isEmpty()){
            cvh.Othernotes.setVisibility(View.INVISIBLE);
        }else{
            cvh.Othernotes.setVisibility(View.VISIBLE);
            cvh.Othernotes.setText(class_cardmodel.getOtherNotes());
        }
        cvh.lassColor.setBackground(cvh.itemView.getContext().getDrawable(class_cardmodel.getClassColor()));

        cvh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null) onItemClickListener.onItemClick(cvh.itemView,cvh.getAbsoluteAdapterPosition(),class_cardmodel);
            }
        });
        cvh.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null) onItemClickListener.onItemClick(cvh.itemView,cvh.getAbsoluteAdapterPosition(),class_cardmodel);
            }
        });
        cvh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemDelete(cvh.itemView,cvh.getAbsoluteAdapterPosition());

                }
            }
        });


    }


    @Override
    public void onBindViewHolder(@NonNull CVH holder, Class_cardmodel item, @NonNull List<?> payloads) {
        super.onBindViewHolder(holder, item, payloads);
       if(!payloads.isEmpty()){
            String isLoad=payloads.get(0).toString();
            if(isLoad.equals("updating")){
                holder.course.setText(item.getClass_course());
                holder.date.setText(item.getClass_date());
                holder.Startclass.setText(item.getClass_startClass());
                holder.Totalclass.setText(item.getClass_totalClass());
                holder.lassColor.setText(item.getLassColor());
                holder.lassColor.setBackground(holder.itemView.getContext().getDrawable(item.getClassColor()));
                holder.Classplace.setText(item.getClass_classPlace());
                if (item.getOtherNotes().equals("")|item.getOtherNotes().isEmpty()){
                    holder.Othernotes.setVisibility(View.INVISIBLE);
                }else{
                    holder.Othernotes.setVisibility(View.VISIBLE);
                    holder.Othernotes.setText(item.getOtherNotes());
                }
            }
        }
    }

    public class CVH extends RecyclerView.ViewHolder{
        TextView course,date,Startclass,Classplace,Totalclass,edit,delete,lassColor,Othernotes;
        public CVH(@NonNull View itemView) {
            super(itemView);
            course=(TextView) itemView.findViewById(R.id.class_info_card_course);
            date=(TextView) itemView.findViewById(R.id.class_info_card_date);
            Startclass=(TextView) itemView.findViewById(R.id.class_info_card_startClass);
            Classplace=(TextView) itemView.findViewById(R.id.class_info_card_classPlace);
            Totalclass=(TextView) itemView.findViewById(R.id.class_info_card_totalClass);
            edit=(TextView) itemView.findViewById(R.id.class_info_card_edit);
            delete=(TextView) itemView.findViewById(R.id.class_info_card_delete);
            lassColor=(TextView) itemView.findViewById(R.id.class_info_card_classColor);
            Othernotes=(TextView) itemView.findViewById(R.id.class_info_card_classNotes);
        }
    }
}
