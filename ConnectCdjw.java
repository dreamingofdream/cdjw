package reptile.cdjw2;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConnectCdjw {

    private Map<String ,String >cookies = new HashMap<>();

    private String txtUid;

    private String txtPwd;

    private String __VIEWSTATE;

    private String __EVENTVALIDATION;

    private Connection connection;

    private Connection.Response response;

    private Document document;

    public ConnectCdjw(){
        try {
            //获取连接
            connection = Jsoup.connect("http://221.233.24.27:8080");
            connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; …) Gecko/20100101 Firefox/66.0");
            response = connection.timeout(3000).execute();
            cookies = response.cookies();
            document = Jsoup.parse(response.body());
           // System.out.println(document);
            __EVENTVALIDATION = document.select("input[name=__EVENTVALIDATION]").val();
            System.out.println(__EVENTVALIDATION);
            __VIEWSTATE = document.select("input[name=__VIEWSTATE]").val();
            System.out.println(__VIEWSTATE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadCheckCode(){
        try{
            String checkCodeUrl = "http://221.233.24.27:8080/verifycode.aspx";
            response = Jsoup.connect(checkCodeUrl).cookies(cookies).
                    ignoreContentType(true).userAgent("Mpzilla").method(Connection.Method.GET).timeout(3000).execute();
            byte[] bytes = response.bodyAsBytes();

            File file = new File("E:\\xunmeng\\Desktop\\tp.png");
            if (file.exists()){
                file.delete();
            }
            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(String txtUid,String txtPwd,String txtCheckCode){
        Map<String ,String >datas = new HashMap<>();
        datas.put("__VIEWSTATE",__VIEWSTATE);
        datas.put("__EVENTVALIDATION",__EVENTVALIDATION);
        datas.put("txtUid",txtUid);
        datas.put("txtPwd",txtPwd);
        datas.put("btLogin","%B5%C7%C2%BC");
        datas.put("txtCheckCode",txtCheckCode);
        datas.put("selKind","1");
        System.out.println(datas);
        try{
            connection = Jsoup.connect("http://221.233.24.27:8080/");
            connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; …) Gecko/20100101 Firefox/66.0");
            response = connection.postDataCharset("GB2312").ignoreContentType(true)
                    .method(Connection.Method.POST).data(datas).cookies(cookies).execute();
            document = response.parse();
            System.out.println(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String stuNum; String password;
        String checkCode;
        Scanner input = new Scanner(System.in);
        ConnectCdjw connectCdjw = new ConnectCdjw();
        connectCdjw.downloadCheckCode();
        System.out.print("请输入学号：");
        stuNum = input.next();
        System.out.println(stuNum);
        System.out.print("请输入密码：");
        password = input.next();
        System.out.println(password);
        System.out.print("请输入验证码：");
        checkCode = input.next();
        System.out.println(checkCode);
        connectCdjw.login(stuNum,password,checkCode);
        input.close();
    }
}
