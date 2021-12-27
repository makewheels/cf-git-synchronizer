package com.github.makewheels.gitsync.aliyunfc;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.StreamRequestHandler;
import com.github.makewheels.gitsync.handlers.TimerCreateWebHookHandler;

import java.io.InputStream;
import java.io.OutputStream;

public class TimerCreateWebHook implements StreamRequestHandler {
    private TimerCreateWebHookHandler timerCreateWebHookHandler;

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) {
        timerCreateWebHookHandler.run();
    }
}
