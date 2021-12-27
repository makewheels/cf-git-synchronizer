package com.github.makewheels.gitsync.handlers;

import com.alibaba.fastjson.JSONObject;
import com.github.makewheels.gitsync.utils.GitUtil;
import com.github.makewheels.gitsync.utils.GiteeUtil;

import java.util.List;

/**
 * 定时任务，确保所有仓库都有webHook
 */
public class TimerCreateWebHookHandler {
    public void run() {
        List<String> repoNames = GitUtil.getSyncRepoNames();
//        for (String repoName : repoNames) {
        String repoName = repoNames.get(0);
        boolean webHookExist = GiteeUtil.isGitSyncWebHookExist(repoName);
        System.out.println("是否存在webHook：repoName = " + repoName + " " + webHookExist);
        if (!webHookExist) {
            String url = "https://1618784280874658.cn-hongkong.fc.aliyuncs.com/2016-08-15/proxy" +
                    "/cf-git-synchronizer.LATEST/handle-web-hook-callback/"
                    + "?webHookName=git-sync";
            System.out.println("为 " + repoName + " 创建webHook");
            System.out.println("url = " + url);
            JSONObject createResult = GiteeUtil.createWebHook(repoName, url);
            System.out.println("创建结果:" + createResult.toJSONString());
        }
//        }
    }
}
