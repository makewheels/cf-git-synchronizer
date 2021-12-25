package com.github.makewheels.gitsync;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.StreamRequestHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AliyunRun implements StreamRequestHandler {
    private final SyncHandler syncHandler = new SyncHandler();

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) {
        syncHandler.run();
    }
}
