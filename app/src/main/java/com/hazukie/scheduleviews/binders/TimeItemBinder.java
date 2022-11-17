package com.hazukie.scheduleviews.binders;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.models.Unimodel;

public class TimeItemBinder extends ItemViewBinder<Unimodel, TimeItemBinder.TIV> {
    private ItemCall itemCall;
    private ItemClick itemClick;
    private boolean nestedScrollMod=false;

    public void setNestedScrollMod(boolean nestedScrollMod) {
        this.nestedScrollMod = nestedScrollMod;
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public void setItemCall(ItemCall itemCall) {
        this.itemCall = itemCall;
    }

    @NonNull
    @Override
    public TIV onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new TIV(layoutInflater.inflate(R.layout.recy_editaview,viewGroup,false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull TIV tiv, Unimodel unimodel) {
        String parsedTile= (!unimodel.title.isEmpty())? unimodel.title.replaceAll("(\\d{2})(\\d{2})","$1:$2"):"";
        tiv.title.setText(parsedTile);
        tiv.edit.setOnClickListener(v->{
            if(itemCall!=null) itemCall.doEdit(tiv.title,unimodel);
        });
        tiv.delete.setOnClickListener(v1->{
            if(itemCall!=null) itemCall.doDelete(unimodel);
        });

        tiv.itemView.setOnClickListener(v->{
            try{
                if(itemClick!=null) itemClick.click(tiv.itemView,unimodel);
            }catch (Exception e){
                e.printStackTrace();
            }

        });

        if(nestedScrollMod){
            tiv.itemView.setOnTouchListener((v, event) -> {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_HOVER_MOVE:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    default:
                        break;
                }
                return false;
            });
        }

    }


    public class TIV extends RecyclerView.ViewHolder{
        private TextView title,delete,edit;

        public TIV(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.editaview_title);
            delete=itemView.findViewById(R.id.editaview_delete);
            edit=itemView.findViewById(R.id.editaview_edit);
        }
    }

    public interface  ItemClick{
        void click(View v,Unimodel uni);
    }
    public interface  ItemCall{
        void doDelete(Unimodel uni);
        void doEdit(TextView txt, Unimodel uni);
    }

}
