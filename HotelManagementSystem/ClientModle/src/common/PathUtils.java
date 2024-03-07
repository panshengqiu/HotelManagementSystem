package common;

public class PathUtils {
    private static final String P_PATH = "ClientModle/images/";
    public static String getPath(String fileName){
        return P_PATH + fileName;
    }
}
