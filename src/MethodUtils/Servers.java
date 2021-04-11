package MethodUtils;

import DataPretreatment.Constant;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by 96130 on 2018/5/3.
 */
public class Servers implements Runnable
{
    private  String message;//待发送的消息
    private  ServerSocket server = null;
    private Socket socket = null;

    public Servers()
    {
        this.message = null;
    }

    public Servers(String message)
    {
        this.message = message;
    }

    public void run()
    {
        server();
    }
    /**
     * @Description: 消息发送方
     * this.message 发送的消息，String型
     */
    public void server()
    {
        try
        {
            server = new ServerSocket(Constant.Port);
        }catch (Exception e)
        {
            System.out.println("sever service 启动失败");
        }

        try
        {
            socket = server.accept();//调用accept()方法开始监听，等待客户端的连接
            //System.out.println("New connection accepted "+socket.getInetAddress()+":"+socket.getPort());
        }catch (Exception e)
        {
            System.out.println("Severs service Error:"+e);
        }
        Scanner in = null;//输入流
        try
        {
            in = new Scanner(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e)
        {
            System.out.println("Severs service InputSteram Error:"+e);
        }
        PrintWriter out = null;//输出流
        try
        {
            out = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e)
        {
            System.out.println("Severs service OutputSteram Error:"+e);
        }
        String tmp = in.nextLine();
        while(!tmp.equals("close"))
        {
            //System.out.println(tmp);
            if(tmp.equals("hello"))
            {
                out.println(this.message);
            }
            else
            {
                out.println("close");
            }
            tmp = in.nextLine();
        }
        try
        {
            socket.close();
            server.close();
        } catch (IOException e)
        {
            System.out.println("Severs service close Socket error:"+e);
        }
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String s)
    {
        this.message = s;
    }

    public void close()
    {
        if(this.socket!=null)
        {
            try
            {
                socket.close();
            } catch (IOException e)
            {
                System.out.println("Severs service close Socket error:"+e);
            }
        }
    }
}
