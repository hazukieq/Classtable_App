package com.hazukie.scheduleviews.binders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;

public class IOSItemBinder extends ItemViewBinder<ScheWithTimeModel, IOSItemBinder.IVH> {
    private BnderClickListener clickListener;
    private int gravity=0;
    private int paddingL=0,paddingR=0,paddingTB=0;

    public interface BnderClickListener {
        void  onClick(TextView v,ScheWithTimeModel scheWithTimeModel);
    }

    public void setClickListener(BnderClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public  IOSItemBinder.IVH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new IOSItemBinder.IVH(layoutInflater.inflate(R.layout.recy_ios_item,viewGroup,false));
    }


    public void setItemGravity(int gravity){
        this.gravity=gravity;
    }

    public void setItemPadding(int paddingTB,int paddingL,int paddingR){
        this.paddingTB=paddingTB;
        this.paddingR=paddingR;
        this.paddingL=paddingL;
    }
    @Override
    public void onBindViewHolder(@NonNull IVH ivh, ScheWithTimeModel scheWithTimeModel) {
        ivh.title.setText(scheWithTimeModel.scheName);
        ivh.value.setText(scheWithTimeModel.timeName);
        if(gravity!=0) ivh.value.setGravity(gravity);
        if(paddingTB!=0|paddingL!=0|paddingR!=0) ivh.value.setPadding(paddingL,paddingTB,paddingR,paddingTB);
        ivh.value.setOnClickListener(v->{
            if(clickListener!=null) clickListener.onClick(ivh.value, scheWithTimeModel);
        });
    }

    public class IVH extends RecyclerView.ViewHolder{
        private TextView title,value;
        public IVH(@NonNull View itemView) {
            super(itemView);
            value=itemView.findViewById(R.id.recy_ios_set_value);
            title=itemView.findViewById(R.id.recy_ios_set_name);

        }
    }
}
