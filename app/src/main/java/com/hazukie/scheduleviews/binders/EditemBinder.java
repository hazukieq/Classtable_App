package com.hazukie.scheduleviews.binders;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.models.EditModel;

public class EditemBinder extends ItemViewBinder<EditModel, EditemBinder.EVH> {

    @NonNull
    @Override
    public EVH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new EVH(layoutInflater.inflate(R.layout.recy_sinedit_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EVH evh, EditModel editModel) {
        evh.txt.setText(""+editModel.ques);
        if(editModel.hintxt.length()>0) evh.hintx.setText(editModel.hintxt);
        Editable editable=evh.hintx.getText();
        int po=editable.length();
        evh.hintx.setSelection(po);

        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString()))editModel.hintxt="";
                else editModel.hintxt=s.toString();
            }
        };
        evh.hintx.addTextChangedListener(textWatcher);
    }

    public class EVH extends RecyclerView.ViewHolder{
        private TextView txt;
        private EditText hintx;
        public EVH(@NonNull View itemView) {
            super(itemView);
            txt=itemView.findViewById(R.id.recy_sineidt_name);
            hintx=itemView.findViewById(R.id.recy_sinedit_value);
        }
    }
}
