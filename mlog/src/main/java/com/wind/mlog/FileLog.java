package com.wind.mlog;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by wind on 2020/4/29:5:30 PM
 */
public class FileLog extends ALog {
    /**
     * log文件地址
     */
    private final String filename;
    private final String dir;

    /**
     * 写入文件 异步还是同步
     */
    private final boolean async;

    private ExecutorService mExecutorService;
    /**
     * 是否正在进行清理
     */
    private boolean mClearing;

    public static final int DEFAULT_SIZE = 30;
    public static final int DEFAULT_INTERVAL_TIME_MILLIS = 3 * 24 * 60 * 60 * 1000;


    private FileLog(String dir, String filename, boolean async,Condition condition) {
        this.dir = dir;
        this.filename = filename;
        this.async = async;
        if (condition!=null){
            clear(condition);
        }
    }


    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    protected void log(final int priority, final @Nullable String tag, final String message, @Nullable Throwable t) {
        if (mClearing){
            return;
        }
        if (async) {
            executorService().execute(() -> log(priority, tag, message));
        } else {
            log(priority, tag, message);
        }

    }

    private ExecutorService executorService() {
        if (mExecutorService == null) {
            synchronized (this){
                if (mExecutorService==null){
                    mExecutorService = Executors.newFixedThreadPool(2);
                }
            }

        }
        return mExecutorService;
    }

    private void log(int priority, String tag, String txt) {
        long milliTime = new Date().getTime();
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
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(formatTime(milliTime))
                .append("  ")
                .append(priorityDesc)
                .append("  ")
                .append(tag)
                .append(":")
                .append(txt);
        File file = FileUtils.getOrCreate(dir, filename);
        FileUtils.writeToFile(file, sBuilder.toString(),true);
    }





    private String formatTime(long milliTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS");
        return dateFormat.format(new Date(milliTime));
    }



    public void clear(Condition condition) {
        mClearing=true;
        executorService().execute(() -> {
            File file = FileUtils.getOrCreate(dir, filename);
            if (file != null && file.exists()) {
                if (condition != null) {
                    if (condition.sizeM <= 0) {
                        condition.sizeM = DEFAULT_SIZE;
                    }
                    if (condition.intervalTimeMillis <= 0) {
                        condition.intervalTimeMillis = DEFAULT_INTERVAL_TIME_MILLIS;
                    }
                    //The length, in bytes
                    long sizeInBytes = file.length();
                    long sizeInMB = sizeInBytes / 1024 / 1024;

                    if (sizeInMB >= condition.sizeM || System.currentTimeMillis() - file.lastModified() >= condition.intervalTimeMillis) {
                        FileUtils.delete(file);
                    }
                } else {
                    FileUtils.delete(file);
                }

            }
            mClearing=false;
        });

    }


    public static class Condition {
        /**
         * 文件大小 单位为M ，当文件大于该值时，会触发删除
         */
        private int sizeM;
        /**
         * 文件间隔了多久没有修改，当当前时间-文件修改时间大于该值时，会触发删除
         */
        private long intervalTimeMillis;

        public Condition(int sizeM, long intervalTimeMillis) {
            this.sizeM = sizeM;
            this.intervalTimeMillis = intervalTimeMillis;
        }
    }


    public static class Builder {
        private boolean async = true;
        private String dir;
        private String filename;
        private Condition condition;
        public Builder async(boolean async) {
            this.async = async;
            return this;
        }

        public Builder dir(String dir) {
            this.dir = dir;
            return this;
        }

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }
        public Builder clear(Condition condition){
            this.condition=condition;
            return this;
        }

        public FileLog build() {
           return new FileLog(dir, filename, async,condition);
        }
    }
}
