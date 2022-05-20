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
import com.example.classtool.models.QTime;

public class UniversalBinder extends ItemViewBinder<QTime,UniversalBinder.UVH> {
    private OnItemClick onItemClick=null;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public UVH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new UVH(layoutInflater.inflate(R.layout.universal_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UVH uvh, QTime qTime) {
           uvh.menu_title.setText(qTime.getStart2end());
           uvh.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(onItemClick!=null){
                       onItemClick.onClick(uvh.itemView,uvh.getLayoutPosition(), qTime.getSorq(), "", qTime.getStart2end());
                   }
               }
           });
    }

    public class UVH extends RecyclerView.ViewHolder{

        private TextView menu_title;
        public UVH(@NonNull View itemView) {
            super(itemView);
            menu_title=(TextView) itemView.findViewById(R.id.menu_title);
        }
    }
}
