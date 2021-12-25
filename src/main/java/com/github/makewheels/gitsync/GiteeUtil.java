package com.github.makewheels.gitsync;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GiteeUtil {
    private static final String username = "publishcode";

    public static JSONArray getRepositories(int per_page, int page) {
        System.out.println("获取gitee仓库, per_page = " + per_page + ", page = " + page);
        String response = HttpUtil.get("https://gitee.com/api/v5/user/repos?" +
                "access_token=" + System.getProperty("gitee_access_token")
                + "&per_page=" + per_page + "&page=" + page);
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

}
