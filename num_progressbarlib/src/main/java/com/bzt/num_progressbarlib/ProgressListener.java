package com.bzt.num_progressbarlib;

/**
 * 进度监听器
 * Created by SHIBW-PC on 2015/12/8.
 */
public interface ProgressListener{

    //变化时监听
    void progressChanged(int finish,int total);

}
