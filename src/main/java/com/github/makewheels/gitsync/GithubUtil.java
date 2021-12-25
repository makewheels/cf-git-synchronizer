package com.github.makewheels.gitsync;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GithubUtil {
    private static final String username = "makewheels";

    public static JSONArray getRepositories(int per_page, int page) {
        System.out.println("获取github仓库, per_page = " + per_page + ", page = " + page);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "token " + System.getenv("github_token"));
        String response = HttpUtil.createGet("https://api.github.com/users/" + username
                        + "/repos?per_page=" + per_page + "&page=" + page)
                .addHeaders(headers).execute().body();
        return JSON.parseArray(response);
    }

    public static JSONArray getAllRepositories() {
        JSONArray result = new JSONArray();
        int per_page = 100;
        for (int page = 1; page < 30; page++) {
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
        return "https://github.com/" + username + "/" + repoName + ".git";
    }

    public static UsernamePasswordCredentialsProvider getCredential() {
        return new UsernamePasswordCredentialsProvider(username, System.getenv("github_token"));
    }

}
