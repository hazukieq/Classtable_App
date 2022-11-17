package com.hazukie.scheduleviews.binders;

import android.view.View;

import com.hazukie.scheduleviews.models.ClassLabel;

public interface BinderClickListener {
    void onDelete(View v,ClassLabel classLabel);
    void onEdit(View v, ClassLabel classLabel);
}
