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

public class TimelistBinder extends ItemViewBinder<SchedulModel,TimelistBinder.TVH> {
    private OnItemClick onItemClick=null;
    private OnItemLClick onItemLClick=null;

    public interface OnItemLClick{
        void onDelete(View v,int position);
    }

    public void setOnItemLClick(OnItemLClick onItemLClick) {
        this.onItemLClick = onItemLClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public TVH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new TVH(layoutInflater.inflate(R.layout.timelist_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TVH tvh, SchedulModel schedulModel) {
        if(schedulModel.getSort()==0) tvh.delete.setVisibility(View.INVISIBLE);
        else if(schedulModel.getSort()>0) tvh.delete.setVisibility(View.VISIBLE);

        tvh.name.setText(schedulModel.getSche());
        tvh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null){
                    onItemClick.onClick(tvh.itemView,tvh.getAbsoluteAdapterPosition(),schedulModel.getSort(), schedulModel.getSche(), schedulModel.getTime());
                }
            }
        });

        tvh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if(onItemLClick!=null){
                      onItemLClick.onDelete(tvh.itemView,tvh.getAbsoluteAdapterPosition());
                  }
            }
        });
    }

    public class TVH extends RecyclerView.ViewHolder{
        private TextView delete,name;

        public TVH(@NonNull View itemView) {
            super(itemView);
            delete=(TextView) itemView.findViewById(R.id.timelist_item_delete);
            name=(TextView) itemView.findViewById(R.id.timelist_item_itemname);
        }
    }

}
