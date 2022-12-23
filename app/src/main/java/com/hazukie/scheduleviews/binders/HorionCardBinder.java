package com.hazukie.scheduleviews.binders;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.models.HoricardModel;

import java.util.List;


public class HorionCardBinder extends ItemViewBinder<HoricardModel, HorionCardBinder.HCV> {
    private  OnDelListener delListener;
    private OnExpandListener expandListener;
    private OnSettingListener settingListener;
    private OnOpenDoc onOpenDoc;

    public void setOnOpenDoc(OnOpenDoc onOpenDoc) {
        this.onOpenDoc = onOpenDoc;
    }

    public void setSettingListener(OnSettingListener settingListener) {
        this.settingListener = settingListener;
    }

    public void setDelListener(OnDelListener delListener) {
        this.delListener = delListener;
    }

    public void setExpandListener(OnExpandListener expandListener) {
        this.expandListener = expandListener;
    }

    public interface  OnDelListener{
        void onDel(View v,HoricardModel hrc);
    }

    public interface OnExpandListener{
        void onExpand(View v,TextView conView,String descript);
    }

    public interface OnSettingListener{
        void onSetting(View v,HoricardModel hrc);
    }

    public interface OnOpenDoc{
        void onOpen(String sche,String time);
    }

    @NonNull
    @Override
    public HCV onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new HCV(layoutInflater.inflate(R.layout.recy_horion_card,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HCV hcv, HoricardModel hrc) {
        hcv.fileName.setText(hrc.title);
        hcv.detailBtn.setText(hrc.subtitle);
        hcv.fileDescription.setText(Html.fromHtml(hrc.description));
        hcv.deleteBtn.setOnClickListener(v -> {
            if(delListener!=null) delListener.onDel(v,hrc);
        });

        hcv.detailBtn.setOnClickListener(v -> {
            if(expandListener!=null) expandListener.onExpand(v,hcv.fileDescription,hrc.subtitle);
        });

        hcv.settingBtn.setOnClickListener(v -> {
            if(settingListener!=null) settingListener.onSetting(v,hrc);
        });

        hcv.itemView.setOnClickListener(v->{
            if(onOpenDoc!=null) onOpenDoc.onOpen(hrc.title+".txt",hrc.subtitle+".txt");
        });
    }


    @Override
    public void onBindViewHolder(@NonNull HCV hcv, HoricardModel hrc, @NonNull List<?> payloads) {
        super.onBindViewHolder(hcv, hrc, payloads);
        if(!payloads.isEmpty()) {
            String isLoad = payloads.get(0).toString();
            if (isLoad.equals("updating")) {
                hcv.fileName.setText(hrc.title);
                hcv.detailBtn.setText(hrc.subtitle);
                hcv.fileDescription.setText(Html.fromHtml(hrc.description));
            }
        }
    }

    public class HCV extends RecyclerView.ViewHolder{
        private TextView fileName,fileDescription;
        private Button detailBtn,settingBtn;
        private ImageView deleteBtn;
        public HCV(@NonNull View itemView) {
            super(itemView);
            fileName=itemView.findViewById(R.id.recy_horion_card_title);
            fileDescription=itemView.findViewById(R.id.recy_horion_card_detail);
            detailBtn=itemView.findViewById(R.id.recy_horion_card_detailBtn);
            deleteBtn=itemView.findViewById(R.id.recy_horion_card_delete);
            settingBtn=itemView.findViewById(R.id.recy_horion_card_setting);
        }
    }
}
