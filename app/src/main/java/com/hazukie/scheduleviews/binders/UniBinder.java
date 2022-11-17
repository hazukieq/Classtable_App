package com.hazukie.scheduleviews.binders;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.models.Unimodel;

public class UniBinder extends ItemViewBinder<Unimodel, UniBinder.UVH> {

    private UniClickListener uniClickListener;
    private Justify justify;
    private boolean isSelectedMode=false;

    public interface  UniClickListener{
        void onClick(View v,Unimodel uni);

    }
    public void setClickListener(UniClickListener uniClickListener) {
        this.uniClickListener=uniClickListener;
    }

    public interface Justify{
        void justifyTextView(TextView view);
    }

    public void setJustify(Justify justify) {
        this.justify = justify;
    }

    public void setIsSelectedMode(boolean mode){
        this.isSelectedMode=mode;
    }

    @NonNull
    @Override
    public UVH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new UVH(layoutInflater.inflate(R.layout.recy_unitext,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UVH uvh, Unimodel unimodel) {
        uvh.itemView.setOnClickListener(v->{
            if(uniClickListener!=null) uniClickListener.onClick(uvh.itemView, unimodel);
        });

        uvh.title.setText(Html.fromHtml(unimodel.title));
        if(justify!=null) justify.justifyTextView(uvh.title);

        if(isSelectedMode){
            if(unimodel.id==0){
                uvh.title.setBackgroundColor(uvh.itemView.getResources().getColor(R.color.white));
                uvh.title.setTextColor(uvh.itemView.getResources().getColor(R.color.ios_ext));
            }else{
                uvh.title.setBackground(uvh.itemView.getContext().getDrawable(R.drawable.btn_bg));//Color(uvh.itemView.getResources().getColor(R.color.qmuibtn_text));
                uvh.title.setTextColor(uvh.itemView.getResources().getColor(R.color.white));
            }
        }

    }

    public class UVH extends RecyclerView.ViewHolder{
        private TextView title;
        public UVH(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.recy_uni_title);
        }
    }
}
