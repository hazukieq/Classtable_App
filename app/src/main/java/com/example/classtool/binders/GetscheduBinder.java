package com.example.classtool.binders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.example.classtool.R;
import com.example.classtool.models.DowncardModel;
import com.example.classtool.models.DownloadBean;
import com.example.classtool.utils.DownloadManager;
import com.example.classtool.utils.DownloadObserver;
import com.example.classtool.utils.Downloadhelper;
import com.example.classtool.utils.FilesUtil;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GetscheduBinder extends ItemViewBinder<DownloadBean,GetscheduBinder.GVH> {
    private onSetClicks onSetClicks=null;

    public interface onSetClicks{
        void onUpdateProgress(int postition,int value);
        void onDelete(int position);
    }

    public void setOnSetClicks(GetscheduBinder.onSetClicks onSetClicks) {
        this.onSetClicks = onSetClicks;
    }

    /**
     * 换算文件的大小
     */
    public String FormetFileSize(long fileSize) {// 转换文件大小
        if (fileSize <= 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }


    @NonNull
    @Override
    public GVH onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        return new GVH(layoutInflater.inflate(R.layout.downcard_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GVH viewHolder, DownloadBean bean) {
        viewHolder.txt_name.setText(bean.name);
        viewHolder.txt_file_size.setText(FormetFileSize(bean.progress)
                + " / " + FormetFileSize(bean.appSize));


        if (bean.download_state == DownloadManager.STATE_START) {
            viewHolder.txt_state.setText("开始下载");
        } else if (bean.download_state == DownloadManager.STATE_WAITING) {
            viewHolder.txt_state.setText("排队等待中");
        } else if (bean.download_state == DownloadManager.STATE_DOWNLOADING) {
            viewHolder.txt_state.setText("下载中");
        } else if (bean.download_state == DownloadManager.STATE_PAUSED) {
            viewHolder.txt_state.setText("暂停中");
        } else if (bean.download_state == DownloadManager.STATE_DOWNLOADED) {
            viewHolder.txt_state.setText("下载完成");
            viewHolder.btn_delete.setVisibility(View.GONE);
        } else if (bean.download_state == DownloadManager.STATE_ERROR) {
            viewHolder.txt_state.setText("下载失败，请重新下载");
        } else {
            viewHolder.txt_state.setText("点击下载");
        }

        viewHolder.seekbar.setProgress((int) ((float) bean.progress
                / (float) bean.appSize * 100f));
        viewHolder.seekbar.setMax(100);

        DownloadManager.getInstance().registerObserver(bean.name,
                new DownloadObserver() {
                    @Override
                    public void onPrepare(DownloadBean bean) {
                        viewHolder.txt_state.setText("排队等待中");
                    }

                    @Override
                    public void onStart(DownloadBean bean) {
                        viewHolder.txt_state.setText("开始下载");
                    }

                    @Override
                    public void onProgress(DownloadBean bean) {
                        viewHolder.txt_file_size
                                .setText(FormetFileSize(bean.progress)
                                        + " / "
                                        + FormetFileSize(bean.appSize));
                        viewHolder.txt_state.setText("下载中");
                        viewHolder.seekbar
                                .setProgress((int) ((float) bean.progress
                                        / (float) bean.appSize * 100f));
                    }

                    @Override
                    public void onStop(DownloadBean bean) {
                        viewHolder.txt_state.setText("暂停中");
                    }

                    @Override
                    public void onFinish(DownloadBean bean) throws IOException {
                        viewHolder.txt_state.setText("下载完成");
                        viewHolder.btn_delete.setVisibility(View.GONE);
                        viewHolder.seekbar.setProgress(100);
                        List<DownloadBean> beans=FilesUtil.readDownload_datas();
                        List<DownloadBean> peans=new ArrayList<>();

                        if(bean.getName().equals("云同步作息时间表")) {
                            List<String> schetags = FilesUtil.readSchedulAndTimeTag(viewHolder.itemView.getContext());
                            List<String> cops=new ArrayList<>();
                            cops.addAll(schetags);
                            int i=0;
                            for (String s : schetags) {
                                if(s.equals("云同步课表,云同步作息时间表")||s.equals("云同步课表,武鸣校区时作息间")){
                                    cops.remove(s);
                                    i=schetags.indexOf(s);
                                }
                            }
                            if(i==0) cops.add("云同步课表,云同步作息时间表");
                            else cops.add(i,"云同步课表,云同步作息时间表");
                            FilesUtil.RemoveScheDulAndTimeTag(viewHolder.itemView.getContext(),cops);

                            List<String> timetags=FilesUtil.readTimeTag(viewHolder.itemView.getContext());
                            List<String> cs=new ArrayList<>();
                            int y=0;
                            cs.addAll(timetags);
                            for(String  sy:timetags){
                                if(sy.split(",")[0].equals("云同步作息时间表")){
                                    cs.remove(sy);
                                    y=timetags.indexOf(sy);
                                }
                            }

                            String tigs=FilesUtil.readBackupClassTimetag(viewHolder.itemView.getContext(),"云同步作息时间表");
                            if(y==0) cs.add(tigs);
                            else cs.add(y,tigs);

                            FilesUtil.AppendTimeTags(viewHolder.itemView.getContext(),cs);
                        }
                    }

                    @Override
                    public void onError(DownloadBean bean) {
                        viewHolder.txt_state.setText("下载失败");
                    }

                    @Override
                    public void onDelete(DownloadBean bean) {
                        viewHolder.txt_state.setText("点击下载");
                        viewHolder.txt_file_size.setText(FormetFileSize(0)
                                + " / " + FormetFileSize(bean.appSize));
                        viewHolder.seekbar.setProgress(0);
                    }
                });

        viewHolder.txt_state.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 开启下载
                try {
                    DownloadManager.getInstance().download(bean);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 删除当前任务
                try {
                    DownloadManager.getInstance().DeleteDownTask(bean);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }



    public class GVH extends RecyclerView.ViewHolder{
        private ProgressBar seekbar;
        private TextView txt_name,btn_delete,txt_state,txt_file_size;

        public GVH(@NonNull View itemView) {
            super(itemView);
            txt_name=(TextView)itemView.findViewById(R.id.downcard_t);
            btn_delete=(TextView)itemView.findViewById(R.id.downcard_delete);
            txt_file_size=(TextView)itemView.findViewById(R.id.downcard_name);
            txt_state=(TextView)itemView.findViewById(R.id.downcard_appstate);
            //start=(TextView)itemView.findViewById(R.id.downcard_start);
            seekbar=(ProgressBar) itemView.findViewById(R.id.downcard_prog);
        }
    }

}
