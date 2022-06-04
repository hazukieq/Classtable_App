package com.example.classtool.utils;

import com.example.classtool.models.DownloadBean;

import java.io.IOException;

public interface DownloadObserver {
    /**准备下载*/
    void onPrepare(DownloadBean bean);
    /** 开始下载 */
    void onStart(DownloadBean bean);
    /** 下载中 */
    void onProgress(DownloadBean bean);
    /** 暂停 */
    void onStop(DownloadBean bean);
    /** 下载完成 */
    void onFinish(DownloadBean bean) throws IOException;
    /** 下载失败 */
    void onError(DownloadBean bean);
    /** 删除成功 */
    void onDelete(DownloadBean bean);
}
