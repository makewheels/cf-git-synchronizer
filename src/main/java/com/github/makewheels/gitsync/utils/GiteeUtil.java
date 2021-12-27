package com.github.makewheels.gitsync.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiteeUtil {
    private static final String username = "publishcode";
    private static String gitee_access_token;

    private static String get_gitee_access_token() {
        if (gitee_access_token == null) {
            gitee_access_token = System.getenv("gitee_access_token");
        }
        return gitee_access_token;
    }

    public static JSONArray getRepositories(int per_page, int page) {
        System.out.println("获取gitee仓库, per_page = " + per_page + ", page = " + page);
        String response = HttpUtil.get("https://gitee.com/api/v5/user/repos?" + "access_token="
                + get_gitee_access_token() + "&per_page=" + per_page + "&page=" + page);
        return JSON.parseArray(response);
    }

    public static JSONArray getAllRepositories() {
        JSONArray result = new JSONArray();
        int per_page = 100;
        for (int page = 1; page < 20; page++) {
            JSONArray eachPage = getRepositories(per_page, page);
            result.addAll(eachPage);
            if (eachPage.isEmpty() || eachPage.size() < per_page) {
                break;
            }
        }
        return result;
    }

    public static List<String> getAllRepoNames() {
        JSONArray allRepositories = getAllRepositories();
        List<String> names = new ArrayList<>(allRepositories.size());
        for (int i = 0; i < allRepositories.size(); i++) {
            JSONObject repo = allRepositories.getJSONObject(i);
            names.add(repo.getString("name"));
        }
        return names;
    }

    public static String getGitUrlByRepoName(String repoName) {
        return "https://gitee.com/" + username + "/" + repoName + ".git";
    }

    public static UsernamePasswordCredentialsProvider getCredential() {
        return new UsernamePasswordCredentialsProvider(username, get_gitee_access_token());
    }

    /**
     * 获取指定仓库的webhook
     *
     * @param repoName
     * @return
     */
    public static JSONArray getRepoWebHooks(String repoName) {
        String json = HttpUtil.get("https://gitee.com/api/v5/repos/" + username + "/" + repoName
                + "/hooks?" + "access_token=" + get_gitee_access_token() + "&per_page=100&page=1");
        return JSON.parseArray(json);
    }

    /**
     * 把url中的参数解析成map
     *
     * @param url
     * @return
     */
    private static Map<String, String> parseUrlParamsToMap(String url) {
        String[] split = url.split("\\?");
        if (split.length == 1) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        String[] paramArray = split[1].split("&");
        for (String param : paramArray) {
            String[] kv = param.split("=");
            map.put(kv[0], kv[1]);
        }
        return map;
    }

    /**
     * 获取git sync webHook
     * {
     * "id": 863651,
     * "url": "http://json.cn",
     * "created_at": "2021-12-27T13:47:58+08:00",
     * "password": null,
     * "project_id": 19892661,
     * "result": "",
     * "result_code": 301,
     * "push_events": true,
     * "tag_push_events": false,
     * "issues_events": false,
     * "note_events": false,
     * "merge_requests_events": false
     * }
     *
     * @param repoName
     * @return
     */
    public static JSONObject getGitSyncWebHook(String repoName) {
        JSONArray webHooks = getRepoWebHooks(repoName);
        for (int i = 0; i < webHooks.size(); i++) {
            JSONObject webHook = webHooks.getJSONObject(i);
            String url = webHook.getString("url");
            Map<String, String> map = parseUrlParamsToMap(url);
            if (map == null) continue;
            if (StringUtils.equals(map.get("webHookName"), "git-sync")) {
                return webHook;
            }
        }
        return null;
    }

    /**
     * 根据回调url中的名字参数判断指定仓库的git sync webHook是否存在
     *
     * @param repoName
     * @return
     */
    public static boolean isGitSyncWebHookExist(String repoName) {
        return getGitSyncWebHook(repoName) != null;
    }

    /**
     * 创建一个webHook
     *
     * @param repoName
     * @param webHookUrl
     * @return
     */
    public static JSONObject createWebHook(String repoName, String webHookUrl) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("url", webHookUrl);
        String json = HttpUtil.post("https://gitee.com/api/v5/repos/" + username + "/"
                + repoName + "/hooks?access_token=" + get_gitee_access_token(), paramsMap);
        return JSON.parseObject(json);
    }
}
