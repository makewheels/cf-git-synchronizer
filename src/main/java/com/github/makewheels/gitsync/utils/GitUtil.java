package com.github.makewheels.gitsync.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GitUtil {
    /**
     * 获取要同步的仓库名列表
     *
     * @return
     */
    public static List<String> getSyncRepoNames() {
        return getTheyBothHave(GithubUtil.getAllRepoNames(), GiteeUtil.getAllRepoNames());
    }

    /**
     * 求两集合的交集
     *
     * @param list1
     * @param list2
     * @return
     */
    private static List<String> getTheyBothHave(List<String> list1, List<String> list2) {
        List<String> result = new ArrayList<>();
        Set<String> list1Set = new HashSet<>(list1.size());
        list1Set.addAll(list1);
        for (String each : list2) {
            if (list1Set.contains(each)) {
                result.add(each);
            }
        }
        return result;
    }
}
