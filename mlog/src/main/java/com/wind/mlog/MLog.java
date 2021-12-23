package com.wind.mlog;

import android.os.Build;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by wind on 2020/4/29:4:39 PM
 * 多功能日志架构
 */
public class MLog extends ALog{
    private static final ALog[] LOG_ARRAY_EMPTY = new ALog[0];
    private final List<ALog> LOGS=new ArrayList<>();
    private volatile ALog[] logsAsArray=LOG_ARRAY_EMPTY;
    private static volatile MLog defaultInstance;


    private static final int CALL_STACK_INDEX = 5;
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");
    private static final int MAX_LOG_LENGTH = 4000;
    private static final int MAX_TAG_LENGTH = 23;


    @Override
    protected void log(int priority, @Nullable String tag, String message, @Nullable Throwable t) {
        if (!mEnabled){
            return;
        }

        if (tag==null||tag.length()==0){
            tag=getTag();
        }

        ALog[] logs = logsAsArray;
        for (ALog log : logs) {
            try {
                log.log(priority,tag,message,t);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 注册日志实现类
     * @param log
     */
    public void register(ALog log){
        if (log!=null){
            synchronized (LOGS){
                LOGS.add(log);
                logsAsArray=LOGS.toArray(new ALog[LOGS.size()]);
            }
        }
    }

    /**
     * 获取默认的日志类
     * @return
     */
    public static MLog getDefault() {
        MLog instance = defaultInstance;
        if (instance == null) {
            synchronized (MLog.class) {
                instance = MLog.defaultInstance;
                if (instance == null) {
                    instance = MLog.defaultInstance = new MLog();
                }
            }
        }
        return instance;
    }


    @Override final String getTag() {
        String tag = super.getTag();
        if (tag != null) {
            return tag;
        }

        // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
        // because Robolectric runs them on the JVM but on Android the elements are different.
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length <= CALL_STACK_INDEX) {
            throw new IllegalStateException(
                    "Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }
        return createStackElementTag(stackTrace[CALL_STACK_INDEX]);
    }
    @Nullable
    protected String createStackElementTag(StackTraceElement element) {
        String tag = element.getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1);
        // Tag length limit was removed in API 24.
        if (tag.length() <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return tag;
        }
        return tag.substring(0, MAX_TAG_LENGTH);
    }



}
