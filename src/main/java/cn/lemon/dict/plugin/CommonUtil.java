package cn.lemon.dict.plugin;

public class CommonUtil {

    public static String toHump(String source) {
        StringBuilder strBuilder = new StringBuilder();
        if (source.contains("_")) {
            String[] tmp = source.split("_");
            strBuilder.append(tmp[0]);
            strBuilder.append(tmp[1].substring(0, 1).toUpperCase() + tmp[1].substring(1).toLowerCase());
            return strBuilder.toString();
        } else {
            return source;
        }
    }

    public static String toConstantLabel(String source) {
        StringBuilder strBuilder = new StringBuilder();
        if (source.contains("_")) {
            return source.toUpperCase();
        } else {
            for (int i = 0; i < source.length(); i++) {
                Character c = source.charAt(i);
                if (Character.isUpperCase(c)) {
                    strBuilder.append("_");
                }
                strBuilder.append(c);
            }
            return strBuilder.toString().toUpperCase();
        }
    }
}
