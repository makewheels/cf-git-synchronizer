package com.github.makewheels.gitsync.handlers;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;

public class WebHookCallbackHandler {
    private final TimerSyncHandler timerSyncHandler = new TimerSyncHandler();

    /**
     * {
     *   "after": "2711fd1dd1e3247d7899c9cad24ded191992a659",
     *   "before": "fe72697d33f3700dd214500639b4a11f188b27b9",
     *   "commits": [
     *     {
     *       "added": [
     *
     *       ],
     *       "author": {
     *         "email": "makewheels",
     *         "id": null,
     *         "name": "makewheels",
     *         "remark": null,
     *         "time": "2021-12-27T15:47:10+08:00",
     *         "url": null,
     *         "user": null,
     *         "user_name": null,
     *         "username": null
     *       },
     *       "committer": {
     *         "email": "makewheels",
     *         "id": null,
     *         "name": "makewheels",
     *         "remark": null,
     *         "time": null,
     *         "url": null,
     *         "user": null,
     *         "user_name": null,
     *         "username": null
     *       },
     *       "distinct": true,
     *       "id": "2711fd1dd1e3247d7899c9cad24ded191992a659",
     *       "message": "gitee-web hook\n",
     *       "modified": [
     *         "src/main/java/com/github/makewheels/gitsync/handlers/WebHookCallbackHandler.java"
     *       ],
     *       "parent_ids": [
     *         "fe72697d33f3700dd214500639b4a11f188b27b9"
     *       ],
     *       "removed": [
     *
     *       ],
     *       "timestamp": "2021-12-27T15:47:10+08:00",
     *       "tree_id": "2f5ab16ff8fa245361fe78a8465038d1f5609a8f",
     *       "url": "https://gitee.com/publishcode/cf-git-synchronizer/commit/2711fd1dd1e3247d7899c9cad24ded191992a659"
     *     }
     *   ],
     *   "commits_more_than_ten": false,
     *   "compare": "https://gitee.com/publishcode/cf-git-synchronizer/compare/fe72697d33f3700dd214500639b4a11f188b27b9...2711fd1dd1e3247d7899c9cad24ded191992a659",
     *   "created": false,
     *   "deleted": false,
     *   "enterprise": null,
     *   "head_commit": {
     *     "added": [
     *
     *     ],
     *     "author": {
     *       "email": "makewheels",
     *       "id": null,
     *       "name": "makewheels",
     *       "remark": null,
     *       "time": "2021-12-27T15:47:10+08:00",
     *       "url": null,
     *       "user": null,
     *       "user_name": null,
     *       "username": null
     *     },
     *     "committer": {
     *       "email": "makewheels",
     *       "id": null,
     *       "name": "makewheels",
     *       "remark": null,
     *       "time": null,
     *       "url": null,
     *       "user": null,
     *       "user_name": null,
     *       "username": null
     *     },
     *     "distinct": true,
     *     "id": "2711fd1dd1e3247d7899c9cad24ded191992a659",
     *     "message": "gitee-web hook\n",
     *     "modified": [
     *       "src/main/java/com/github/makewheels/gitsync/handlers/WebHookCallbackHandler.java"
     *     ],
     *     "parent_ids": [
     *       "fe72697d33f3700dd214500639b4a11f188b27b9"
     *     ],
     *     "removed": [
     *
     *     ],
     *     "timestamp": "2021-12-27T15:47:10+08:00",
     *     "tree_id": "2f5ab16ff8fa245361fe78a8465038d1f5609a8f",
     *     "url": "https://gitee.com/publishcode/cf-git-synchronizer/commit/2711fd1dd1e3247d7899c9cad24ded191992a659"
     *   },
     *   "hook_id": 863907,
     *   "hook_name": "push_hooks",
     *   "hook_url": "https://gitee.com/publishcode/cf-git-synchronizer/hooks/863907/edit",
     *   "password": "",
     *   "project": {
     *     "clone_url": "https://gitee.com/publishcode/cf-git-synchronizer.git",
     *     "created_at": "2021-12-25T20:16:43+08:00",
     *     "default_branch": "master",
     *     "description": "同步gitee到github",
     *     "fork": false,
     *     "forks_count": 0,
     *     "full_name": "publishcode/cf-git-synchronizer",
     *     "git_http_url": "https://gitee.com/publishcode/cf-git-synchronizer.git",
     *     "git_ssh_url": "git@gitee.com:publishcode/cf-git-synchronizer.git",
     *     "git_svn_url": "svn://gitee.com/publishcode/cf-git-synchronizer",
     *     "git_url": "git://gitee.com/publishcode/cf-git-synchronizer.git",
     *     "has_issues": true,
     *     "has_pages": false,
     *     "has_wiki": true,
     *     "homepage": null,
     *     "html_url": "https://gitee.com/publishcode/cf-git-synchronizer",
     *     "id": 20016215,
     *     "language": null,
     *     "license": null,
     *     "name": "cf-git-synchronizer",
     *     "name_with_namespace": "publishcode/cf-git-synchronizer",
     *     "namespace": "publishcode",
     *     "open_issues_count": 0,
     *     "owner": {
     *       "avatar_url": "https://gitee.com/assets/no_portrait.png",
     *       "email": "tongbuwangpan@163.com",
     *       "html_url": "https://gitee.com/publishcode",
     *       "id": 8762064,
     *       "login": "publishcode",
     *       "name": "publishcode",
     *       "remark": null,
     *       "site_admin": false,
     *       "type": "User",
     *       "url": "https://gitee.com/publishcode",
     *       "user_name": "publishcode",
     *       "username": "publishcode"
     *     },
     *     "path": "cf-git-synchronizer",
     *     "path_with_namespace": "publishcode/cf-git-synchronizer",
     *     "private": false,
     *     "pushed_at": "2021-12-27T15:47:17+08:00",
     *     "ssh_url": "git@gitee.com:publishcode/cf-git-synchronizer.git",
     *     "stargazers_count": 0,
     *     "svn_url": "svn://gitee.com/publishcode/cf-git-synchronizer",
     *     "updated_at": "2021-12-27T15:47:17+08:00",
     *     "url": "https://gitee.com/publishcode/cf-git-synchronizer",
     *     "watchers_count": 1
     *   },
     *   "push_data": null,
     *   "pusher": {
     *     "email": "tongbuwangpan@163.com",
     *     "id": 8762064,
     *     "name": "publishcode",
     *     "url": "https://gitee.com/publishcode",
     *     "user_name": "publishcode",
     *     "username": "publishcode"
     *   },
     *   "ref": "refs/heads/master",
     *   "repository": {
     *     "clone_url": "https://gitee.com/publishcode/cf-git-synchronizer.git",
     *     "created_at": "2021-12-25T20:16:43+08:00",
     *     "default_branch": "master",
     *     "description": "同步gitee到github",
     *     "fork": false,
     *     "forks_count": 0,
     *     "full_name": "publishcode/cf-git-synchronizer",
     *     "git_http_url": "https://gitee.com/publishcode/cf-git-synchronizer.git",
     *     "git_ssh_url": "git@gitee.com:publishcode/cf-git-synchronizer.git",
     *     "git_svn_url": "svn://gitee.com/publishcode/cf-git-synchronizer",
     *     "git_url": "git://gitee.com/publishcode/cf-git-synchronizer.git",
     *     "has_issues": true,
     *     "has_pages": false,
     *     "has_wiki": true,
     *     "homepage": null,
     *     "html_url": "https://gitee.com/publishcode/cf-git-synchronizer",
     *     "id": 20016215,
     *     "language": null,
     *     "license": null,
     *     "name": "cf-git-synchronizer",
     *     "name_with_namespace": "publishcode/cf-git-synchronizer",
     *     "namespace": "publishcode",
     *     "open_issues_count": 0,
     *     "owner": {
     *       "avatar_url": "https://gitee.com/assets/no_portrait.png",
     *       "email": "tongbuwangpan@163.com",
     *       "html_url": "https://gitee.com/publishcode",
     *       "id": 8762064,
     *       "login": "publishcode",
     *       "name": "publishcode",
     *       "remark": null,
     *       "site_admin": false,
     *       "type": "User",
     *       "url": "https://gitee.com/publishcode",
     *       "user_name": "publishcode",
     *       "username": "publishcode"
     *     },
     *     "path": "cf-git-synchronizer",
     *     "path_with_namespace": "publishcode/cf-git-synchronizer",
     *     "private": false,
     *     "pushed_at": "2021-12-27T15:47:17+08:00",
     *     "ssh_url": "git@gitee.com:publishcode/cf-git-synchronizer.git",
     *     "stargazers_count": 0,
     *     "svn_url": "svn://gitee.com/publishcode/cf-git-synchronizer",
     *     "updated_at": "2021-12-27T15:47:17+08:00",
     *     "url": "https://gitee.com/publishcode/cf-git-synchronizer",
     *     "watchers_count": 1
     *   },
     *   "sender": {
     *     "avatar_url": "https://gitee.com/assets/no_portrait.png",
     *     "email": "tongbuwangpan@163.com",
     *     "html_url": "https://gitee.com/publishcode",
     *     "id": 8762064,
     *     "login": "publishcode",
     *     "name": "publishcode",
     *     "remark": null,
     *     "site_admin": false,
     *     "type": "User",
     *     "url": "https://gitee.com/publishcode",
     *     "user_name": "publishcode",
     *     "username": "publishcode"
     *   },
     *   "sign": "",
     *   "timestamp": "1640591261042",
     *   "total_commits_count": 1,
     *   "user": {
     *     "email": "tongbuwangpan@163.com",
     *     "id": 8762064,
     *     "name": "publishcode",
     *     "url": "https://gitee.com/publishcode",
     *     "user_name": "publishcode",
     *     "username": "publishcode"
     *   },
     *   "user_id": 8762064,
     *   "user_name": "publishcode",
     *   "webHookName": "git-sync"
     * }
     * @param input
     * @param output
     */
    public void run(InputStream input, OutputStream output) {
        String json = IoUtil.readUtf8(input);
        System.out.println("回调json: " + json);

        JSONObject callback = JSON.parseObject(json);
        //过滤测试回调
        if (callback.getString("ref").equals("refs/heads/test_version")) {
            return;
        }
        String repoName = callback.getJSONObject("repository").getString("name");
        //同步指定仓库
        timerSyncHandler.syncSingleRepo(repoName);

        IoUtil.writeUtf8(output, true, "ok");
    }
}
