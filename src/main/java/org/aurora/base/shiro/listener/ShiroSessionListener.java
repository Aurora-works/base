package org.aurora.base.shiro.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

@Slf4j
public class ShiroSessionListener implements SessionListener {

    @Override
    public void onStart(Session session) {
        log.info("会话创建:{}", session.getId());
        log.info("会话创建:{}", session.getHost());
        log.info("会话创建:{}", session.getStartTimestamp());
        log.info("会话创建:{}", session.getAttribute("userAgent"));
    }

    @Override
    public void onStop(Session session) {
        log.info("会话停止:{}", session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        log.info("会话过期:{}", session.getId());
    }
}
