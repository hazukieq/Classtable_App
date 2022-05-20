package com.example.classtool.binders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.example.classtool.R;
import com.example.classtool.base.OnItemClick;
import com.example.classtool.models.QTime;

import java.util.List;

public class QTimeBinder extends ItemViewBinder<QTime,QTimeBinder.QVH> {
    private OnItemClick onItemClick=null;


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public QVH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new QVH(layoutInflater.inflate(R.layout.time_add_item,viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QVH qvh, QTime qTime) {
            qvh.time_add_id.setText(qTime.getSortStr());

            String st2en=qTime.getStart2end();
            qvh.time_add_setTime.setText(st2en);
            qvh.Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClick!=null){
                        onItemClick.onClick(qvh.Edit, qvh.getAbsoluteAdapterPosition(), qTime.getSorq(),qTime.getSortStr(), qTime.getStart2end());
                    }
                }
            });
    }


    @Override
    public void onBindViewHolder(@NonNull QTimeBinder.QVH qvh, QTime qTime, @NonNull List<?> payloads) {
        super.onBindViewHolder(qvh, qTime, payloads);
        if(!payloads.isEmpty()){
            String isLoad=payloads.get(0).toString();
            if(isLoad.equals("updating")){
                qvh.time_add_id.setText(qTime.getSortStr());
                String st2en=qTime.getStart2end();//.replaceAll("(\\d{2})(.*)(\\d{2})$","$1:$2:$3");
                qvh.time_add_setTime.setText(st2en);
            }
        }
    }

    public class QVH extends RecyclerView.ViewHolder{

        private TextView time_add_id,time_add_setTime,Edit;

        public QVH(@NonNull View itemView) {
            super(itemView);
            time_add_id=(TextView) itemView.findViewById(R.id.time_add_id);
            time_add_setTime=(TextView) itemView.findViewById(R.id.time_add_setTime);
            Edit=(TextView) itemView.findViewById(R.id.time_add_edit);
        }
    }
}
