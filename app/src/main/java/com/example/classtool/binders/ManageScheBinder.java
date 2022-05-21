package com.example.classtool.binders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.example.classtool.R;
import com.example.classtool.models.SchedulModel;

public class ManageScheBinder extends ItemViewBinder<SchedulModel,ManageScheBinder.MVH> {
    private  OnPItemLClick onPItemLClick=null;

    public interface OnPItemLClick{
        void onDelete(View v,String name,int position);
    }

    public void setOnPItemLClick(OnPItemLClick onPItemLClick) {
        this.onPItemLClick = onPItemLClick;
    }

    @NonNull
    @Override
    public MVH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new MVH(layoutInflater.inflate(R.layout.timelist_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MVH mvh, SchedulModel schedulModel) {

        mvh.title.setText("课表卡片");
        mvh.name.setText(schedulModel.getSche());
        mvh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onPItemLClick!=null){
                    onPItemLClick.onDelete(mvh.itemView,schedulModel.getSche(),mvh.getAbsoluteAdapterPosition());
                }
            }
        });

    }

    public class MVH extends RecyclerView.ViewHolder{

        private TextView delete,name,title;

        public MVH(@NonNull View itemView) {
            super(itemView);
            delete=(TextView) itemView.findViewById(R.id.timelist_item_delete);
            name=(TextView) itemView.findViewById(R.id.timelist_item_itemname);
            title=(TextView) itemView.findViewById(R.id.timelist_item_title);
        }
    }
}
