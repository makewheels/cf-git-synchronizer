package com.github.makewheels.gitsync;

import cn.hutool.core.io.FileUtil;
import com.github.makewheels.gitsync.utils.GiteeUtil;
import com.github.makewheels.gitsync.utils.GithubUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws GitAPIException, IOException, URISyntaxException {
        System.setProperty("github_token", "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");
        System.setProperty("gitee_access_token", "d8ad901998ac42b59df1da71fca33901");

//        List<String> github = GithubUtil.getAllRepoNames();
//        List<String> gitee = GiteeUtil.getAllRepoNames();
//
//        List<String> theyBothHave = GitUtil.getTheyBothHave(github, gitee);
//        System.out.println(JSON.toJSONString(theyBothHave));

        File folder = new File("D:\\TEST\\1");
        if (!new File(folder, ".git").exists()) FileUtil.mkdir(folder);

        Git.init().setDirectory(folder).call();
        Git git = Git.open(folder);

        UsernamePasswordCredentialsProvider giteeCredential
                = new UsernamePasswordCredentialsProvider("gitee",
                "d8ad901998ac42b59df1da71fca33901");

        String repoName = "cloud-function-compress-object-storage";

        List<String> remoteNames = new ArrayList<>();
        List<RemoteConfig> remoteConfigs = git.remoteList().call();
        for (RemoteConfig remoteConfig : remoteConfigs) {
            remoteNames.add(remoteConfig.getName());
        }
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

        FetchResult giteeFetchResult = git.fetch().setRemote("gitee").setCredentialsProvider(giteeCredential).call();
        System.out.println(giteeFetchResult.getMessages());

        for (Ref ref : giteeFetchResult.getAdvertisedRefs()) {
            String refName = ref.getName();
            git.pull().setRemote("gitee").setRemoteBranchName(refName).call();
            git.push().setCredentialsProvider(GithubUtil.getCredential())
                    .setRemote("github").add(refName).call();
        }


    }
}
