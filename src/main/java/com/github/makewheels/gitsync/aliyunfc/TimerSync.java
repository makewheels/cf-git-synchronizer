package com.github.makewheels.gitsync.aliyunfc;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.StreamRequestHandler;
import com.github.makewheels.gitsync.handlers.TimerSyncHandler;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class TimerSync implements StreamRequestHandler {
    private final TimerSyncHandler syncHandler = new TimerSyncHandler();

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) {
        try {
            syncHandler.run();
        } catch (GitAPIException | URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
