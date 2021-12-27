package com.github.makewheels.gitsync.handlers;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;

public class WebHookCallbackHandler {
    private final TimerSyncHandler timerSyncHandler = new TimerSyncHandler();

    public void run(InputStream input, OutputStream output) {
        String json = IoUtil.readUtf8(input);
        System.out.println("回调json: " + json);

        JSONObject callback = JSON.parseObject(json);
        String repoName = callback.getJSONObject("repository").getString("name");
        //同步指定仓库
        timerSyncHandler.syncSingleRepo(repoName);

        IoUtil.writeUtf8(output, true, "ok");
    }
}
