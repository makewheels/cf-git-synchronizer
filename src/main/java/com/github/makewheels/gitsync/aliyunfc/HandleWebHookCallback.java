package com.github.makewheels.gitsync.aliyunfc;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.StreamRequestHandler;
import com.github.makewheels.gitsync.handlers.WebHookCallbackHandler;

import java.io.InputStream;
import java.io.OutputStream;

public class HandleWebHookCallback implements StreamRequestHandler {
    private WebHookCallbackHandler webHookCallbackHandler;

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) {
        webHookCallbackHandler.run(input,output);
    }
}
