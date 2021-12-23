package com.wind.mlog;

/**
 * created by wind on 2020/4/29:4:45 PM
 */
interface ILog {

    void v(String message,Object... args);
    void v(Throwable t,String message,Object... args);
    void v(Throwable t);
    void v(String tag,String message,Object... args);
    void v(String tag,Throwable t,String message,Object... args);
    void v(String tag,Throwable t);

    void d(String message,Object... args);
    void d(Throwable t,String message,Object... args);
    void d(Throwable t);
    void d(String tag,String message,Object... args);
    void d(String tag,Throwable t,String message,Object... args);
    void d(String tag,Throwable t);

    void i(String message,Object... args);
    void i(Throwable t,String message,Object... args);
    void i(Throwable t);
    void i(String tag,String message,Object... args);
    void i(String tag,Throwable t,String message,Object... args);
    void i(String tag,Throwable t);

    void w(String message,Object... args);
    void w(Throwable t,String message,Object... args);
    void w(Throwable t);
    void w(String tag,String message,Object... args);
    void w(String tag,Throwable t,String message,Object... args);
    void w(String tag,Throwable t);

    void e(String message,Object... args);
    void e(Throwable t,String message,Object... args);
    void e(Throwable t);
    void e(String tag,String message,Object... args);
    void e(String tag,Throwable t,String message,Object... args);
    void e(String tag,Throwable t);

    void wtf(String message,Object... args);
    void wtf(Throwable t,String message,Object... args);
    void wtf(Throwable t);
    void wtf(String tag,String message,Object... args);
    void wtf(String tag,Throwable t,String message,Object... args);
    void wtf(String tag,Throwable t);

    void log(int priority,String message,Object... args);
    void log(int priority,Throwable t,String message,Object... args);
    void log(int priority,Throwable t);
    void log(String tag,int priority,String message,Object... args);
    void log(String tag,int priority,Throwable t,String message,Object... args);
    void log(String tag,int priority,Throwable t);

    void setEnabled(boolean enabled);
}
