package com.wind.mlog;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * created by wind on 2020/4/29:4:59 PM
 */
public abstract class ALog implements ILog{
    protected boolean mEnabled=true;
    public void setEnabled(boolean enabled){
        this.mEnabled=enabled;
    }

    final ThreadLocal<String> explicitTag = new ThreadLocal<>();

    String getTag(){
        String tag=explicitTag.get();
        if (tag!=null){
            explicitTag.remove();
        }
        return tag;
    }
    private void prepareLog(int priority, Throwable t, String message, Object... args) {
        if (!mEnabled){
            return;
        }
        String tag=getTag();

        if (message!=null && message.length()==0){
            message=null;
        }
        if (message==null){
            if (t==null){
                return;
            }
            message=getStackTraceString(t);
        }else {
            if (args!=null && args.length>0){
                message=formatMessage(message,args);
            }
            if (t!=null){
                message+= "\n"+getStackTraceString(t);
            }
        }
        log(priority,tag,message,t);
    }
    protected abstract void log(int priority, @Nullable String tag, /*@NotNull*/ String message,
                                @Nullable Throwable t);
    private String formatMessage(String message,Object[] args){
        return String.format(message,args);
    }
    private String getStackTraceString(Throwable t){
        StringWriter sw=new StringWriter(256);
        PrintWriter pw=new PrintWriter(sw,false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }



    @Override
    public void v(String message, Object... args) {
        prepareLog(Log.VERBOSE, null, message, args);
    }

    @Override
    public void v(String tag, String message, Object... args) {
        explicitTag.set(tag);
        prepareLog(Log.VERBOSE, null, message, args);
    }

    @Override
    public void v(Throwable t, String message, Object... args) {
        prepareLog(Log.VERBOSE, t, message, args);
    }

    @Override
    public void v(String tag, Throwable t, String message, Object... args) {
        explicitTag.set(tag);
        prepareLog(Log.VERBOSE, t, message, args);
    }

    @Override
    public void v(Throwable t) {
        prepareLog(Log.VERBOSE, t, null);
    }
    @Override
    public void v(String tag, Throwable t) {
        explicitTag.set(tag);
        prepareLog(Log.VERBOSE, t, null);
    }


    @Override
    public void d(String message, Object... args) {
        prepareLog(Log.DEBUG, null, message, args);
    }

    @Override
    public void d(String tag, String message, Object... args) {
        explicitTag.set(tag);
        prepareLog(Log.DEBUG, null, message, args);
    }

    @Override
    public void d(Throwable t, String message, Object... args) {
        prepareLog(Log.DEBUG, t, message, args);
    }

    @Override
    public void d(String tag, Throwable t, String message, Object... args) {
        explicitTag.set(tag);
        prepareLog(Log.DEBUG, t, message, args);
    }

    @Override
    public void d(Throwable t) {
        prepareLog(Log.DEBUG, t, null);
    }

    @Override
    public void d(String tag, Throwable t) {
        explicitTag.set(tag);
        prepareLog(Log.DEBUG, t, null);
    }

    @Override
    public void i(String message, Object... args) {
        prepareLog(Log.INFO, null,message,args);
    }

    @Override
    public void i(String tag, String message, Object... args) {
        explicitTag.set(tag);
        prepareLog(Log.INFO, null,message,args);
    }

    @Override
    public void i(Throwable t, String message, Object... args) {
        prepareLog(Log.INFO, t, message, args);
    }

    @Override
    public void i(String tag, Throwable t, String message, Object... args) {
        explicitTag.set(tag);
        prepareLog(Log.INFO, t, message, args);
    }

    @Override
    public void i(Throwable t) {
        prepareLog(Log.INFO, t,null);
    }

    @Override
    public void i(String tag, Throwable t) {
        explicitTag.set(tag);
        prepareLog(Log.INFO, t,null);
    }

    @Override
    public void w(String message, Object... args) {
        prepareLog(Log.WARN,null,message,args);
    }

    @Override
    public void w(String tag, String message, Object... args) {
        explicitTag.set(tag);
        prepareLog(Log.WARN,null,message,args);
    }

    @Override
    public void w(Throwable t, String message, Object... args) {
        prepareLog(Log.WARN,t,message,args);
    }

    @Override
    public void w(String tag, Throwable t, String message, Object... args) {
        explicitTag.set(tag);
        prepareLog(Log.WARN,t,message,args);
    }

    @Override
    public void w(Throwable t) {
        prepareLog(Log.WARN,t,null);
    }

    @Override
    public void w(String tag, Throwable t) {
        explicitTag.set(tag);
        prepareLog(Log.WARN,t,null);
    }

    @Override
    public void e(String message, Object... args) {
        prepareLog(Log.ERROR,null,message,args);
    }

    @Override
    public void e(String tag, String message, Object... args) {
        explicitTag.set(tag);
        prepareLog(Log.ERROR,null,message,args);
    }

    @Override
    public void e(Throwable t, String message, Object... args) {
        prepareLog(Log.WARN,t,message,args);
    }

    @Override
    public void e(String tag, Throwable t, String message, Object... args) {
        explicitTag.set(tag);
        prepareLog(Log.WARN,t,message,args);
    }

    @Override
    public void e(Throwable t) {
        prepareLog(Log.WARN,t,null);
    }

    @Override
    public void e(String tag, Throwable t) {
        explicitTag.set(tag);
        e(t);
    }

    @Override
    public void wtf(String message, Object... args) {
        prepareLog(Log.ASSERT, null, message, args);
    }

    @Override
    public void wtf(String tag, String message, Object... args) {
        explicitTag.set(tag);
        wtf(message,args);
    }

    @Override
    public void wtf(Throwable t, String message, Object... args) {
        prepareLog(Log.ASSERT, t, message, args);
    }

    @Override
    public void wtf(String tag, Throwable t, String message, Object... args) {
        explicitTag.set(tag);
        wtf(t,message,args);
    }

    @Override
    public void wtf(Throwable t) {
        prepareLog(Log.ASSERT, t,null);
    }

    @Override
    public void wtf(String tag, Throwable t) {
        explicitTag.set(tag);
        wtf(t);
    }

    @Override
    public void log(int priority, String message, Object... args) {
        prepareLog(priority, null,message,args);
    }

    @Override
    public void log(String tag, int priority, String message, Object... args) {
        explicitTag.set(tag);
        log(priority,message,args);
    }

    @Override
    public void log(int priority, Throwable t, String message, Object... args) {
        prepareLog(priority, t,message,args);
    }

    @Override
    public void log(String tag, int priority, Throwable t, String message, Object... args) {
        explicitTag.set(tag);
        log(priority,t,message,args);
    }

    @Override
    public void log(int priority, Throwable t) {
        prepareLog(priority, t,null);
    }

    @Override
    public void log(String tag, int priority, Throwable t) {
        explicitTag.set(tag);
        log(priority,t);
    }

    protected String getPriorityDesc(int priority){
        String priorityDesc = "[DEBUG]";
        switch (priority) {
            case Log.VERBOSE:
                priorityDesc = "[VERBOSE]";
                break;
            case Log.DEBUG:
                priorityDesc = "[DEBUG]";
                break;
            case Log.INFO:
                priorityDesc = "[INFO]";
                break;
            case Log.WARN:
                priorityDesc = "[WARN]";
                break;
            case Log.ERROR:
                priorityDesc = "[ERROR]";
                break;
            case Log.ASSERT:
                priorityDesc = "[ASSERT]";
                break;
        }
        return priorityDesc;
    }
}
