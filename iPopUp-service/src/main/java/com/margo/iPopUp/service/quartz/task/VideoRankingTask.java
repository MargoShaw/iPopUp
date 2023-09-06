package com.margo.iPopUp.service.quartz.task;

import com.margo.iPopUp.service.VideoService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author margoshaw
 * @date 2023/09/01 20:35
 **/
public class VideoRankingTask extends QuartzJobBean {

    @Autowired
    VideoService videoService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

    }
}
