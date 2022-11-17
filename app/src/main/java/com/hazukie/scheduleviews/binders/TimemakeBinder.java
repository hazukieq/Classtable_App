package com.hazukie.scheduleviews.binders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.drakeet.multitype.MultiTypeAdapter;
import com.hazukie.cskheui.Crialoghue.Clicks;
import com.hazukie.cskheui.Crialoghue.Crialoghue;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.models.TimemakeModel;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.statics.Laytatics;

import java.util.List;

public class TimemakeBinder extends ItemViewBinder<TimemakeModel, TimemakeBinder.TiVH> {
    private AddCalls addCalls;

    public void setAddCalls(AddCalls addCalls) {
        this.addCalls = addCalls;
    }

    @NonNull
    @Override
    public TiVH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new TiVH(layoutInflater.inflate(R.layout.recy_time_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TiVH tiVH, TimemakeModel tim) {
        tiVH.title.setText(tim.title);
        if(tim.unis.size() != 0 ) {
            tiVH.count.setText(tim.toTotal());
            tiVH.mudp.setItems(tim.unis);
        }

        tiVH.addV.setOnClickListener(v->{if(addCalls!=null) addCalls.addSy(tim);});

        tiVH.quickAdd.setOnClickListener(v1->{if(addCalls!=null) addCalls.quickAddSy(tim);});

        tiVH.timeItemBinder.setItemCall(new TimeItemBinder.ItemCall() {
            @Override
            public void doDelete(Unimodel uni) {
                int index_=tim.unis.indexOf(uni);
                tim.unis.remove(uni);
                tiVH.mudp.notifyItemRemoved(index_);
                tiVH.count.setText(tim.toTotal());
            }

            @Override
            public void doEdit(TextView txt, Unimodel uni_) {
                Crialoghue crih=new Crialoghue.HeditBuilder()
                        .addTitle("编辑时间")
                        .addHint("请按 0800-0900 格式输入")
                        .addContents(uni_.title)
                        .onConfirm((crialoghue, view) -> {
                            EditText eidt=(EditText) view;
                            String sr=eidt.getText().toString().length()>=9?eidt.getText().toString().substring(0,9):eidt.getText().toString();
                            if((!sr.equals(uni_.title))&&(!sr.isEmpty())){
                                uni_.title=sr;
                                txt.setText(sr.replaceAll("(\\d{2})(\\d{2})","$1:$2"));
                            }
                            crialoghue.dismiss();
                        })
                        .build(tiVH.itemView.getContext());
                crih.show();
            }
        });
    }



    @Override
    public void onBindViewHolder(@NonNull TiVH holder, TimemakeModel item, @NonNull List<?> payloads) {
        super.onBindViewHolder(holder, item, payloads);
        if(!payloads.isEmpty()) {
            String isLoad = payloads.get(0).toString();
            if (isLoad.equals("updating")) {
                holder.count.setText(item.toTotal());
                holder.mudp.setItems(item.unis);
                holder.mudp.notifyDataSetChanged();
            }
        }
    }

    public class TiVH extends RecyclerView.ViewHolder{
        private TextView title,quickAdd,count;
        private ImageButton addV;
        private RecyclerView subrecy;
        private MultiTypeAdapter mudp;
        private TimeItemBinder timeItemBinder;
        public TiVH(@NonNull View itemView) {
            super(itemView);
            mudp=new MultiTypeAdapter();
            timeItemBinder=new TimeItemBinder();
            timeItemBinder.setNestedScrollMod(true);
            mudp.register(Unimodel.class,timeItemBinder);

            title = itemView.findViewById(R.id.recy_time_item_title);
            addV = itemView.findViewById(R.id.recy_time_item_add);
            quickAdd = itemView.findViewById(R.id.recy_time_item_quickAdd);
            count = itemView.findViewById(R.id.recy_time_item_count);
            subrecy=itemView.findViewById(R.id.recy_time_item_subrecy);
            subrecy.setAdapter(mudp);
        }

    }

    public interface  AddCalls{
        void addSy(TimemakeModel timemakeModel);
        void quickAddSy(TimemakeModel timemakeModel);
    }
}
