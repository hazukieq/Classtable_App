package com.example.classtool.binders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.example.classtool.R;
import com.example.classtool.models.DowncardModel;
import com.example.classtool.utils.Downloadhelper;

import java.util.List;

public class GetscheduBinder extends ItemViewBinder<DowncardModel,GetscheduBinder.GVH> {
    private onSetClicks onSetClicks=null;

    public interface onSetClicks{
        void onUpdateProgress(int postition,int value);
        void onDelete(int position);
    }

    public void setOnSetClicks(GetscheduBinder.onSetClicks onSetClicks) {
        this.onSetClicks = onSetClicks;
    }

    @NonNull
    @Override
    public GVH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new GVH(layoutInflater.inflate(R.layout.downcard_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GVH gvh, DowncardModel schedulModel) {
        gvh.delete.setText(schedulModel.getT());
        gvh.progressBar.setProgress(schedulModel.getProgress());
    }

    @Override
    public void onBindViewHolder(@NonNull GVH holder, DowncardModel item, @NonNull List<?> payloads) {
        super.onBindViewHolder(holder, item, payloads);
        if(!payloads.isEmpty()) {
            String isLoad = payloads.get(0).toString();
            if(isLoad.equals("updating")){
                holder.progressBar.setProgress(item.getProgress());
            }
        }
    }

    public class GVH extends RecyclerView.ViewHolder{
        private ProgressBar progressBar;
        private TextView delete;

        public GVH(@NonNull View itemView) {
            super(itemView);
            delete=(TextView)itemView.findViewById(R.id.downcard_t);
            progressBar=(ProgressBar) itemView.findViewById(R.id.downcard_prog);
        }
    }
}
