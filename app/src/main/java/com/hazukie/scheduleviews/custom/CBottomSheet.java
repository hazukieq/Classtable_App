package com.hazukie.scheduleviews.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.drakeet.multitype.ItemViewBinder;
import com.drakeet.multitype.MultiTypeAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.databinding.BottomsheetSchecardBinding;
import com.hazukie.scheduleviews.models.ClassLabel;
import com.hazukie.scheduleviews.statics.ColorSeletor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CBottomSheet extends BottomSheetDialog {
    public CBottomSheet(@NonNull Context context, int theme) {
        super(context, theme);
    }

    public CBottomSheet(@NonNull Context context, Builder builder) {
        this(context,R.style.bottomialogStyle);

        if(builder.attachedView!=null){
            setContentView(builder.attachedView);
            final BottomSheetBehavior<View> mDialogBehavior=BottomSheetBehavior.from((View)builder.attachedView.getParent());
            mDialogBehavior.setPeekHeight(getContext().getResources().getDisplayMetrics().heightPixels/2);
            if(builder.interceptView!=null&&builder.interceptBinding==null) builder.interceptView.doIntercept(this,builder.attachedView);
            else if(builder.interceptView==null&&builder.interceptBinding!=null) builder.interceptBinding.doIntercept(this, builder.dataBinding);

        }
        getWindow().setDimAmount(0.16f);
    }

    public CBottomSheet(@NonNull Context context, ScheBuilder scheBuilder) {
        this(context,R.style.bottomialogStyle);
        BottomsheetSchecardBinding bottomsheetSchecardBinding= DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.bottomsheet_schecard,null,false);
        setContentView(bottomsheetSchecardBinding.getRoot());
        getWindow().setDimAmount(0.16f);
        initSche(bottomsheetSchecardBinding,scheBuilder);
    }



    private void initSche(BottomsheetSchecardBinding binding,ScheBuilder builder){
        final BottomSheetBehavior<View> mDialogBehavior=BottomSheetBehavior.from((View)binding.getRoot().getParent());
        mDialogBehavior.setPeekHeight(getContext().getResources().getDisplayMetrics().heightPixels/2);

        if(builder.interceptBinding!=null) builder.interceptBinding.doIntercept(this,binding);
        //通过设置传入数据刷新当前UI
        binding.setClazz(builder.classLabel);
        binding.classColor.setBackgroundColor(getContext().getColor(ColorSeletor.getColorByIndex(builder.classLabel.color)));

        //使内部嵌套布局可以滑动
        binding.classScrolle.setNestedScrollingEnabled(true);
        //外部不可取消对话框
        setCanceledOnTouchOutside(false);
        binding.classCancelBtn.setOnClickListener(v-> dismiss());
    }



    public CBottomSheet(@NonNull Context context,DefaultBuilder builder) {
        this(context,R.style.bottomialogStyle);
        getWindow().setDimAmount(0.16f);
        View root= View.inflate(context, R.layout.bootmsheet_unilay,null);
        setContentView(root);
        initsByDefault(root,builder);
    }

    private void initsByDefault(View root, DefaultBuilder builder){
        final BottomSheetBehavior<View> mDialogBehavior=BottomSheetBehavior.from((View)root.getParent());
        mDialogBehavior.setSkipCollapsed(builder.isNotExpand);

        TextView cancelV=root.findViewById(R.id.bottomsheet_uni_cancel);
        TextView titleV=root.findViewById(R.id.bottomsheet_uni_title);
        CRecyclerView crecy=root.findViewById(R.id.bottomsheet_uni_recy);

        crecy.setMaxHeight(builder.recyMaxHeight);
        if(builder.interceptView!=null) builder.interceptView.doIntercept(this,root);

        titleV.setText(builder.title);
        cancelV.setOnClickListener(v->{
            if(builder.cancelListener!=null) builder.cancelListener.doCancel(this,v);
            else dismiss();
        });

        MultiTypeAdapter mudp=new MultiTypeAdapter();
        if(builder.binderMap.size()>0){
            for (Class<?> clazz:builder.binderMap.keySet()) {
                mudp.register(clazz, Objects.requireNonNull(builder.binderMap.get(clazz)));
                if(builder.regis!=null) builder.regis.doInterceptByRegis(this,clazz,builder.binderMap.get(clazz));
            }
        }
        if(builder.objs.size()>0) mudp.setItems(builder.objs);
        crecy.setAdapter(mudp);
    }



    public interface  CancelListener{
        void doCancel(CBottomSheet cBottomSheet,View v);
    }


    public interface  InterceptView{
        void doIntercept(CBottomSheet cBottomSheet,View v);
    }

    public interface InterceptBinding{
        void doIntercept(CBottomSheet cBottomSheet,ViewDataBinding binding);
    }

    public interface InterceptOfRecyRegis{
        void doInterceptByRegis(CBottomSheet cBottomSheet,Object clazz, ItemViewBinder binder);
    }


    static View  addV(Context context,int Vid){
        return LayoutInflater.from(context).inflate(Vid,null);
    }

    public static class Builder{
        private InterceptBinding interceptBinding;
        private ClassLabel classLabel;
        private ViewDataBinding dataBinding;
        private View attachedView;
        private InterceptView interceptView;

        public Builder addScheObj(ClassLabel classLabel){
            if(classLabel!=null)this.classLabel=classLabel;
            else this.classLabel=new ClassLabel(1,0,1,0,"","", "","",11,false);
            return this;
        }

        public Builder addView(View v,InterceptView interceptView){
            this.attachedView=v;
            if(interceptView!=null) this.interceptView=interceptView;
            return this;
        }

        public Builder addView(int vid, Context context,InterceptView interceptView){
            this.attachedView=CBottomSheet.addV(context,vid);
            if(interceptView!=null) this.interceptView=interceptView;
            return this;
        }

        public Builder addView(int resID,Context context,InterceptBinding interceptBinding){
            this.dataBinding= DataBindingUtil.inflate(LayoutInflater.from(context),resID,null,false);
            this.attachedView=dataBinding.getRoot();
            if(interceptBinding!=null) this.interceptBinding=interceptBinding;
            return this;
        }

        public CBottomSheet build(Context context){
            return new CBottomSheet(context,this);
        }
    }

    public static class ScheBuilder {
        //private int maxExpandHeight=420;
        private int recyMaxHeight=400;
        private InterceptBinding interceptBinding;
        private ClassLabel classLabel;


        public ScheBuilder addRecyHeight(int max){
            this.recyMaxHeight=max;
            return this;
        }

        public ScheBuilder onInterceptBinding(InterceptBinding interceptBinding){
            this.interceptBinding=interceptBinding;
            return this;
        }

        public ScheBuilder addScheObj(ClassLabel classLabel){
            if(classLabel!=null)this.classLabel=classLabel;
            else this.classLabel=new ClassLabel(1,0,1,0,"", "","","",11,false);
            return this;
        }



        public CBottomSheet build(Context context){
            return new CBottomSheet(context,this);
        }
    }


    public static class DefaultBuilder{
        private String title="";
        private List<Object> objs=new ArrayList<>();
        private CancelListener cancelListener;
        private boolean isNotExpand=true;
        private int recyMaxHeight=160;
        private Map<Class<?>,ItemViewBinder> binderMap=new HashMap<>();

        private InterceptView interceptView;
        private InterceptOfRecyRegis regis;

        public DefaultBuilder addDatas(List<Object> objs){
            this.objs=objs;
            return this;
        }

        public DefaultBuilder addDatas(Object[] objs){
            this.objs.clear();
            this.objs.addAll(Arrays.asList(objs));
            return this;
        }

        public DefaultBuilder addTitle(String title){
            this.title=title;
            return this;
        }


        public DefaultBuilder addCancel(CancelListener cancelListener){
            this.cancelListener=cancelListener;
            return this;
        }

        public DefaultBuilder isNotExpand(boolean isExpand){
            this.isNotExpand=isExpand;
            return this;
        }

        public DefaultBuilder addRecyHeight(int max){
            this.recyMaxHeight=max;
            return this;
        }

        public DefaultBuilder addRegister(Class<?> clazz, ItemViewBinder binder,InterceptOfRecyRegis regis){
            if(clazz!=null&&binder!=null){
                this.binderMap.put(clazz,binder);
                if(regis!=null) this.regis=regis;
            }

            return this;
        }

        public DefaultBuilder addRegisters(Map<Class<?>,ItemViewBinder> binderMap){
            this.binderMap=binderMap;
            return this;
        }

        public DefaultBuilder addIntercetped(InterceptView interceptView){
            this.interceptView=interceptView;
            return this;
        }
        public CBottomSheet build(Context context){
            return new CBottomSheet(context,this);
        }
    }
}
