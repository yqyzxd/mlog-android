package com.wind.mlog;

import androidx.annotation.Nullable;

/**
 * created by wind on 2020/4/30:2:43 PM
 */
public class SystemOutLog extends ALog{
    private StringBuilder sBuilder;
    public SystemOutLog(){
        sBuilder=new StringBuilder();
    }
    @Override
    protected void log(int priority, @Nullable String tag, String message, @Nullable Throwable t) {
        if (!mEnabled){
            return;
        }
        if (sBuilder.length()>0) {
            sBuilder.delete(0, sBuilder.length());
        }
        sBuilder
                .append(getPriorityDesc(priority))
                .append(" ")
                .append(message);
        System.out.println(sBuilder.toString());
    }
}
