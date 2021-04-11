package MethodUtils;

import DataPretreatment.Constant;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by 96130 on 2018/5/3.
 */
public class Client
{
    /**
     * @Description: 接收消息
     * @return 接受到的消息， String类型
     */
    public static String client() throws Exception
    {
        int k=0;
        Socket socket = null;
        try
        {
            socket = new Socket(Constant.IPAdress,Constant.Port);
        }catch (Exception e)
        {
            System.out.println("Client:启动失败");
        }
        System.out.println(socket);
        Scanner in = new Scanner(new InputStreamReader(socket.getInputStream()));//输入流
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);//输出流
        out.println("hello");
        for(int i=0;i<100000;i++)
        {
            k+=i;
        }
        String tmp = in.nextLine();
        //System.out.println(tmp);
        out.println("close");
        socket.close();
        return tmp;
    }
}
