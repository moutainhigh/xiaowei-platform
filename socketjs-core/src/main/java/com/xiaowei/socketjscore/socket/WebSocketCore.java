package com.xiaowei.socketjscore.socket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/webSocketCore/{userId}")
@Component
@Slf4j
public class WebSocketCore {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocketCore> webSocketSet = new CopyOnWriteArraySet<WebSocketCore>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String userId;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) throws IOException {
//        String id = session.getId();
        this.session = session;
        this.userId = userId;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有新连接加入,id为" + this.userId + "！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
//        log.info("来自客户端的消息:" + message);
//        //群发消息
//        for(WebSocketCore item: webSocketSet){
//            try {
//                item.sendMessageToAll(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//                continue;
//            }
//        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket发生错误", error);
    }

    /**
     * 发送消息给所有人
     *
     * @param message
     * @throws IOException
     */
    public void sendMessageToAll(Object message){
        if (message == null) {
            return;
        }
        webSocketSet.forEach(webSocketCore -> {
            try {
                webSocketCore.session.getBasicRemote().sendText(JSON.toJSONString(message));
            } catch (IOException e) {
                log.error("websocket发送消息失败",e);
            }
        });
        //this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 发送消息给指定用户
     *
     * @param message
     * @throws IOException
     */
    public void sendMessageToOne(Object message, String userId){
        try {
            if (message == null) {
                return;
            }
            Optional<WebSocketCore> optional = webSocketSet.stream().filter(webSocketCore -> userId.equals(webSocketCore.userId))
                    .findAny();

            if (optional.isPresent()) {
                optional.get().session.getBasicRemote().sendText(JSON.toJSONString(message));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketCore.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketCore.onlineCount--;
    }
}
