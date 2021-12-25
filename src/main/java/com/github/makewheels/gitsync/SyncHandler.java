package com.github.makewheels.gitsync;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SyncHandler {
    private final File workDir = new File(System.getenv("work_dir"), "cf-git-synchronizer");
    private final File reposDir = new File(workDir, "repos");

    private void initAllRepos(List<String> repoNames) throws GitAPIException, URISyntaxException {
        //init repo
        FileUtil.mkdir(reposDir);

        //检查每一个仓库
        for (String repoName : repoNames) {
            File repoFolder = new File(reposDir, repoName);
            FileUtil.mkdir(repoFolder);
            //如果没有git仓库，则创建仓库
            if (!new File(repoFolder, ".git").exists()) {
                Git git = Git.init().setDirectory(repoFolder).call();
                //添加两个远程地址
                List<RemoteConfig> remoteConfigs = git.remoteList().call();
                List<String> remoteNames = remoteConfigs.stream()
                        .map(RemoteConfig::getName).collect(Collectors.toList());

                if (!remoteNames.contains("gitee")) {
                    git.remoteAdd()
                            .setName("gitee")
                            .setUri(new URIish(GiteeUtil.getGitUrlByRepoName(repoName)))
                            .call();
                }

                if (!remoteNames.contains("github")) {
                    git.remoteAdd()
                            .setName("github")
                            .setUri(new URIish(GithubUtil.getGitUrlByRepoName(repoName)))
                            .call();
                }
            }
        }
    }

    public void run() throws GitAPIException, URISyntaxException, IOException, InterruptedException {
        //获取两边仓库交集
        List<String> repoNames = GitUtil.getTheyBothHave(GithubUtil.getAllRepoNames(), GiteeUtil.getAllRepoNames());
        Collections.sort(repoNames);
        System.out.println("两边都有的仓库：" + JSON.toJSONString(repoNames));
        initAllRepos(repoNames);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        //初始化完成，同步每一个仓库
        for (String repoName : repoNames) {
            System.out.println("sync: " + repoName);
            Git git = Git.open(new File(reposDir, repoName));
            //通过fetch获取gitee分支
            FetchResult giteeFetchResult = git.fetch().setRemote("gitee")
                    .setCredentialsProvider(GiteeUtil.getCredential()).call();

            executorService.submit(() -> {
                //同步每一个分支
                for (Ref ref : giteeFetchResult.getAdvertisedRefs()) {
                    String refName = ref.getName();
                    try {
                        git.pull().setCredentialsProvider(GiteeUtil.getCredential())
                                .setRemote("gitee").setRemoteBranchName(refName).call();
                        git.push().setCredentialsProvider(GithubUtil.getCredential())
                                .setRemote("github").add(refName).call();
                    } catch (GitAPIException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);

    }
}
