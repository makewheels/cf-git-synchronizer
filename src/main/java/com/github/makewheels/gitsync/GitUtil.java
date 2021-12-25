package com.github.makewheels.gitsync;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GitUtil {
    /**
     * 求两集合的交集
     *
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> getTheyBothHave(List<String> list1, List<String> list2) {
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
