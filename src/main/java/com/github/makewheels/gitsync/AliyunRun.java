package com.github.makewheels.gitsync;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.StreamRequestHandler;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class AliyunRun implements StreamRequestHandler {
    private final SyncHandler syncHandler = new SyncHandler();

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) {
        try {
            syncHandler.run();
        } catch (GitAPIException | URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
