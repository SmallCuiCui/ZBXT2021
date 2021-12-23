package org.lxzx.boot.controller;

import org.lxzx.boot.Utils.WebSocketUtil;
import org.lxzx.boot.dto.DashboardData;
import org.lxzx.boot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@ServerEndpoint(value = "/getZaiWei/{userCode}")
public class WebSocketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private UserService userService;
    public static WebSocketController webSocketController;

    @PostConstruct
    public void init(){
        webSocketController = this;
        webSocketController.userService = this.userService;
    }

    @OnOpen
    public void onOpen(@PathParam("userCode") String userCode, Session session) {
        LOGGER.info("[" + userCode + "]加入连接!");
        WebSocketUtil.addSession(userCode, session);
        //一连接上就发送信息给前端、前端心跳为1分钟一次，收到心跳再发送消息给前端
        DashboardData d = webSocketController.userService.getZaiWeiConditions();
        WebSocketUtil.sendMessage(session, d);
    }

    @OnClose
    public void onClose(@PathParam("userCode") String userCode, Session session) {
        LOGGER.info("[" + userCode + "]断开连接!");
        WebSocketUtil.remoteSession(userCode);
    }

    @OnMessage
    public void OnMessage(@PathParam("userCode") String userCode, String message) {
        String messageInfo = "[" + userCode + "]" + "对服务器发送消息：" + message;
        LOGGER.info(messageInfo);
        Session session = WebSocketUtil.ONLINE_SESSION.get(userCode);
        DashboardData d = webSocketController.userService.getZaiWeiConditions();
        //发送信息
        WebSocketUtil.sendMessage(session, d);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.error(session.getId() + "异常:", throwable);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throwable.printStackTrace();
    }
}
