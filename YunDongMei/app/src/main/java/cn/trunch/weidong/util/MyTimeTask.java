package cn.trunch.weidong.util;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimeTask {
    private Timer timer;
    private TimerTask task;

    public MyTimeTask(TimerTask task) {
        this.task = task;
        timer = new Timer();
    }

    /**
     * .每隔time毫秒启动一次
     *
     * @param time 间隔时间.单位毫秒
     */
    public void startTask(long time) {
        timer.schedule(task, 0, time);
    }

    /**
     * .取消定时任务
     */
    public void stopTask() {
        if (timer != null) {
            timer.cancel();
            if (task != null) task.cancel();
        }
    }
}
