package com.wind.mlog;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
    private String logFileName;
    private String logFileDir;

    /**
     * 写入文件 异步还是同步
     */
    private boolean asyn = true;

    private ExecutorService mExecutorService;

    public FileLog(String logFileDir,String logFileName) {
        this.logFileDir = logFileDir;
        this.logFileName=logFileName;
    }

    public FileLog(String logFileDir,String logFileName, boolean asyn) {
        this(logFileDir,logFileName);
        this.asyn = asyn;
    }



    public static Builder newBuilder(){
        return new Builder();
    }

    @Override
    protected void log(final int priority, final @Nullable String tag, final String message, @Nullable Throwable t) {

        if (asyn) {
            if (mExecutorService == null) {
                mExecutorService = Executors.newFixedThreadPool(2);
            }
            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    log(priority, tag, message);
                }
            });

        } else {
            log(priority, tag, message);
        }

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
        sBuilder.append(getDateTime(milliTime))
                .append("  ")
                .append(priorityDesc)
                .append("  ")
                .append(tag)
                .append(":")
                .append(txt);
        File file = getOrCreateFile(logFileDir,logFileName);
        writeToFile(file, sBuilder.toString());
    }

    public static void writeToFile(File file, String txt) {

        FileOutputStream fos = null;
        OutputStreamWriter osw = null;

        try {
            if (!file.exists()) {
                file.createNewFile();
                fos = new FileOutputStream(file);
            } else {
                fos = new FileOutputStream(file, true);
            }
            osw = new OutputStreamWriter(fos, "utf-8");
            osw.write(txt); //写入内容
            osw.write("\r\n");  //换行
        } catch (Exception e) {
            e.printStackTrace();
        } finally {   //关闭流
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private File getOrCreateFile(String fileDir,String fileName) {
      //  String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MarryU/";
        File file = new File(fileDir, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private String getDateTime(long milliTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS");
        String date = dateFormat.format(new Date(milliTime));
        return date;
    }

    /**
     * 大于50MB时自动清除文件
     */
    public void autoClear() {
        //大于50M时主动删除
        File file = getOrCreateFile(logFileDir,logFileName);

        if (file != null && file.exists()) {
            //The length, in bytes
            long sizeInBytes = file.length();
            long sizeInMB = sizeInBytes / 1024 / 1024;
            if (sizeInMB > 50) {
                file.delete();
            }
        }
    }

    private boolean clear() {
        File file = getOrCreateFile(logFileDir,logFileName);
        boolean cleard = false;
        if (file != null && file.exists()) {
            cleard = file.delete();
        }
        return cleard;
    }


    public static class Builder{
        private boolean asyn = true;
        private String logFileDir;
        private String logFileName;

        public Builder setAsyn(boolean asyn){
            this.asyn=asyn;
            return this;
        }

        public Builder setLogFileDir(String logFileDir) {
            this.logFileDir = logFileDir;
            return this;
        }

        public Builder setLogFileName(String logFileName) {
            this.logFileName = logFileName;
            return this;
        }

        public FileLog build(){

            FileLog fileLog=new FileLog(logFileDir,logFileName,asyn);
            return fileLog;
        }
    }
}
