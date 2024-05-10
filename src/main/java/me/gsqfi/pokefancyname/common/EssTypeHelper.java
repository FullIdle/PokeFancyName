package me.gsqfi.pokefancyname.common;

public class EssTypeHelper {

    /**
     * 是否是数字
     */
    public static boolean isInteger(String str){
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 是否有占位符变量
     */
    public static boolean isPapi(String str){
        int i;
        if ((i = str.indexOf('%')) == -1) {
            return false;
        }
        return str.substring(i+1).contains("%");
    }
}
