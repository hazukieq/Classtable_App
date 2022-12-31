package com.hazukie.scheduleviews.scheutil;

import android.widget.TextView;

import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.models.Timetable;

import java.util.List;

public interface CardListener {
    void onClick(TextView v, ClassLabel cls);
    void onClick(TextView v,ClassLabel cls,List<Timetable> times);
}
