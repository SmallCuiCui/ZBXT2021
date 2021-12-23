package org.lxzx.boot.Utils;

import net.sf.json.JSONObject;
import org.lxzx.boot.dto.DashboardData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;


public class WebSocketUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketUtil.class);

    /**
     * @Author: TheBigBlue
     * @Description: 使用map进行存储在线的session
     **/
    public static final Map<String, Session> ONLINE_SESSION = new ConcurrentHashMap<>();

    /**
     * @Description: 添加Session
     **/
    public static void addSession(String userKey, Session session) {
        ONLINE_SESSION.put(userKey, session);
    }

    public static void remoteSession(String userKey) {
        ONLINE_SESSION.remove(userKey);
    }

    /**
     * @Description: 向某个用户发送消息
     **/
    public static Boolean sendMessage(Session session, DashboardData d) {
        if (session == null) {
            return false;
        }
        // getAsyncRemote()和getBasicRemote()异步与同步
        RemoteEndpoint.Async async = session.getAsyncRemote();
        JSONObject object = JSONObject.fromObject(d);
        String jsonstr = object.toString();
        //发送消息
//        Future<Void> future = async.sendObject(d);
        Future<Void> future = async.sendText(jsonstr);

        boolean done = future.isDone();
        LOGGER.info("服务器发送消息给客户端" + session.getId() + "的消息:" + d + "，状态为:" + done);
        return done;

    }
}
