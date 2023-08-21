package com.wzy.mybatis.test.netty;

import com.wzy.mybatis.mapper.DeptMapper;
import com.wzy.mybatis.pojo.Dept;
import com.wzy.mybatis.pojo.Emp;
import com.wzy.mybatis.utils.SqlSessionUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.nio.charset.Charset;

public class EchoServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                ByteBuf buffer = (ByteBuf) msg;

                                System.out.println(buffer.toString(Charset.defaultCharset()));


                                    SqlSession sqlSession = SqlSessionUtils.getSqlSession() ;
                                    DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);
                                    Emp emp = mapper.getEmpByString(buffer.toString(Charset.defaultCharset()));
                                    System.out.println(emp);


                                // 建议使用 ctx.alloc() 创建 ByteBuf
                                ByteBuf response =ctx.alloc().buffer() ;
                                response.writeBytes(emp.toString().getBytes());
                                ctx.writeAndFlush(response);

                                // 思考：需要释放 buffer 吗
                                // 思考：需要释放 response 吗
                            }
                        });
                    }
                }).bind(8080);
    }
}
