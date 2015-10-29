package example.hp.com.myclient;

/**
 * Created by hp on 2015/10/28.
 */
public class MyUser {

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        MyUser.username = username;
    }

    private static String username;

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setIsLoggedIn(boolean isLoggedIn) {
        MyUser.isLoggedIn = isLoggedIn;
    }

    private static boolean isLoggedIn=false;
}
