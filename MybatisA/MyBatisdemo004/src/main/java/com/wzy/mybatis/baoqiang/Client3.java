package com.wzy.mybatis.baoqiang;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client3 {

    public static void main(String[] args) throws IOException {
        Socket socket =new Socket(InetAddress.getLocalHost(),9997);
        System.out.println("连接成功!");

        OutputStream outputStream =socket.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write("t_user");
        bufferedWriter.newLine();
        bufferedWriter.flush();


        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String s = bufferedReader.readLine();
        System.out.println(s);

        bufferedReader.close();
        bufferedWriter.close();
        socket.close();
        System.out.println("客户端退出。。。");

    }
}
