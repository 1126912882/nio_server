package nio;

import nio.exception.UnConnectException;
import nio.message.PingMessage;
import quorum.util.ObjectUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * Created by 吴樟 on www.haixiangzhene.xyz
 * 2018/1/27.
 */
public class NioClient {

    public void write(SocketChannel channel) throws IOException {
        ByteBuffer byteBuffer=ByteBuffer.allocate(8);
        byteBuffer.putInt(8);
        channel.write(byteBuffer);
    }

    public SocketChannel init() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        return socketChannel;
    }

    public void connect(){
        SocketAddress socketAddress=new InetSocketAddress("127.0.0.1",6379);
        SocketChannel socketChannel= null;
        try {
            Selector selector=Selector.open();
            socketChannel = init();
            if (socketChannel.connect(socketAddress)){
                System.out.println("建立连接");
                socketChannel.register(selector,SelectionKey.OP_READ);
            }else {
                System.out.println("建立失败，注册连接监听");
                socketChannel.register(selector,SelectionKey.OP_CONNECT);
            }
            retry:
            while (true){
                int length=selector.select(1000);
                System.out.println("正常执行");
                if (length==0){
                    /*try {
                        doPing(socketChannel);
                    } catch (UnConnectException e) {
                        e.printStackTrace();
                        if((socketChannel=reconect(selector,3,socketAddress))!=null)
                            continue;
                        else {
                            System.out.println("重连失败");
                            break;
                        }

                    }*/
                }
                Set<SelectionKey> selectionKeySet=selector.selectedKeys();
                for (SelectionKey selectionKey:selectionKeySet){
                    System.out.println(selectionKey);
                    if (selectionKey.isConnectable()){
                        if (socketChannel.finishConnect()){
                            System.out.println("成功建立连接");
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            write(socketChannel);
                            selectionKeySet.remove(selectionKey);
                        }
                    }else {
                        System.out.println("服务端断开连接...");
                        selectionKeySet.remove(selectionKey);
                        break retry;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public SocketChannel reconect(Selector selector,int time, SocketAddress socketAddress){
            System.out.println("正在重连");
            for (int i=0;i<time;i++) {
                try {
                    System.out.println("重连等待中");
                    Thread.sleep((2 << (i+1))*1000);
                    SocketChannel socketChannel=init();
                    if (socketChannel.connect(socketAddress)) {
                        System.out.println("重连成功");
                        return socketChannel;
                    }else {
                        socketChannel.register(selector,SelectionKey.OP_CONNECT);
                        selector.select(1500);
                        Set<SelectionKey> selectionKeySet=selector.selectedKeys();
                        for (SelectionKey selectionKey:selectionKeySet){
                            if (selectionKey.isConnectable()){
                                if (socketChannel.finishConnect()){
                                    System.out.println("成功建立连接");
                                    socketChannel.register(selector,SelectionKey.OP_READ);
                                    write(socketChannel);
                                    selectionKeySet.remove(selectionKey);
                                    return socketChannel;
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            return null;
    }

    private void doPing(SocketChannel socketChannel) throws UnConnectException {
        try {
            byte[] pingByte=ObjectUtil.writeObject(new PingMessage());
            ByteBuffer byteBuffer=ByteBuffer.allocate(pingByte.length);
            byteBuffer.put(pingByte);
            byteBuffer.clear();
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            System.out.println("清理连接");
            try {
                socketChannel.socket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            throw new UnConnectException(e.getMessage(),e.getCause());
        }

    }

    public static void main(String[] args){
        NioClient nioClient=new NioClient();
        nioClient.connect();
    }


}
