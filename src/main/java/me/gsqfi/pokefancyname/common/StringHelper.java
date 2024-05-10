package me.gsqfi.pokefancyname.common;

import java.util.List;
import java.util.stream.Collectors;

public class StringHelper {

    /**
     * 颜色代码替换
     */
    public static String replaceColorCode(String str){
        return str.replace('&','§');
    }

    public static List<String> replaceColorCode(List<String> list){
        return list.stream().map(StringHelper::replaceColorCode).collect(Collectors.toList());
    }
}
