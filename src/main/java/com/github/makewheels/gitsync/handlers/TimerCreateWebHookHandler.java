package com.github.makewheels.gitsync.handlers;

import com.alibaba.fastjson.JSONObject;
import com.github.makewheels.gitsync.utils.GitUtil;
import com.github.makewheels.gitsync.utils.GiteeUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务，确保所有仓库都有webHook
 */
public class TimerCreateWebHookHandler {
    /**
     * 处理一个仓库的webHook
     *
     * @param repoName
     */
    private void handleSingleRepoWebHook(String repoName) {
        boolean webHookExist = GiteeUtil.isGitSyncWebHookExist(repoName);
        System.out.println("是否存在webHook: " + repoName + " " + webHookExist);
        if (!webHookExist) {
            String url = "https://1618784280874658.cn-hongkong.fc.aliyuncs.com/2016-08-15/proxy" + "/cf-git-synchronizer.LATEST/handle-web-hook-callback/" + "?webHookName=git-sync";
            System.out.println("为 " + repoName + " 创建webHook");
            System.out.println("url = " + url);
            JSONObject createResult = GiteeUtil.createWebHook(repoName, url);
            System.out.println("创建结果:" + createResult.toJSONString());
        }
    }

    public void run() {
        List<String> repoNames = GitUtil.getSyncRepoNames();

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        //初始化完成，同步每一个仓库
//        for (String repoName : repoNames) {

        String repoName = "cf-git-synchronizer";
        executorService.submit(() -> handleSingleRepoWebHook(repoName));
//        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
