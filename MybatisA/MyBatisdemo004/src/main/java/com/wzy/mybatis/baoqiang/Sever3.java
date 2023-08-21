package com.wzy.mybatis.baoqiang;

import com.wzy.mybatis.mapper.ParameterMapper;
import com.wzy.mybatis.pojo.User;
import com.wzy.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Sever3 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9997);
        System.out.println("服务端等待接听。。。");
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();


        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String s = bufferedReader.readLine();
        System.out.println(s);

            SqlSession sqlSession = SqlSessionUtils.getSqlSession();
            ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
            List<User> list = mapper.getAllUser();
//            for(User user : list)
//            {
//                System.out.println(user);
//            }

        OutputStream outputStream = socket.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write(list.toString());
        bufferedWriter.newLine();
        bufferedWriter.flush();

        socket.shutdownOutput();

        bufferedWriter.close();
        bufferedReader.close();
        socket.close();
        serverSocket.close();


    }
}
