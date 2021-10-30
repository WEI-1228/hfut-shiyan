import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Question04OfCh07 {
    public static void main(String[] args) throws Exception{
        URL baidu = new URL("http://www.4399.com");
        URLConnection baiduconnection = baidu.openConnection();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(baiduconnection.getInputStream()));
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null)
            System.out.println(inputLine);
        bufferedReader.close();
    }
}
