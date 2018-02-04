package nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

/**
 * Created by 吴樟 on www.haixiangzhene.xyz
 * 2018/1/27.
 */
public class NioServer {

    public void Dispachtcher(){
        try {
            SocketAddress socketAddress=new InetSocketAddress("127.0.0.1",6379);
            ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
            Selector selector=Selector.open();
            int interest= SelectionKey.OP_ACCEPT;
            SelectionKey selectionKey=serverSocketChannel.bind(socketAddress)
                                .configureBlocking(false)
                                .register(selector,interest);
            selector=selectionKey.selector();
            int index=0;
            while (true){
                selector.select(1000);
                Set<SelectionKey> selectionKeySet= selector.selectedKeys();
                for (SelectionKey key:selectionKeySet){

                        if (key.isAcceptable()) {
                            System.out.println("已经接受连接");
                            ServerSocketChannel serversocketChannel = (ServerSocketChannel) key.channel();
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            if (socketChannel == null) {
                                System.out.println("连接建立失败");
                                continue;
                            }
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println("建立成功");
                            selectionKeySet.remove(key);
                            continue;
                        }
                        if (key.isReadable()) {
                            System.out.println("读取连接");
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            int length = 0;
                            try {
                                while ((length = socketChannel.read(byteBuffer)) > 0) {
                                    byteBuffer.flip();
                                    System.out.println("正在读取...，长度" + length);
                                    byteBuffer.clear();
                                }
                            }catch (IOException ex){
                                System.out.println("客户端断开...");
                                socketChannel.close();
                            }
                            if (length==-1){
                                System.out.println("客户端断开...");
                                socketChannel.close();
                            }
                            selectionKeySet.remove(key);
                            continue;
                        }
                }
                System.out.println("等待超时正在...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args){
        NioServer nioServer=new NioServer();
        nioServer.Dispachtcher();
    }


}
