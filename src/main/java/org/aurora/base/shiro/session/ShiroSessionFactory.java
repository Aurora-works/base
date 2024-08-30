package org.aurora.base.shiro.session;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.web.session.mgt.WebSessionContext;
import org.aurora.base.util.IPUtils;

public class ShiroSessionFactory implements SessionFactory {

    @Override
    public Session createSession(SessionContext sessionContext) {
        SimpleSession session = new SimpleSession();
        if (sessionContext instanceof WebSessionContext webSessionContext) {
            HttpServletRequest request = (HttpServletRequest) webSessionContext.getServletRequest();
            if (request != null) {
                session.setAttribute("userAgent", request.getHeader("User-Agent"));
                session.setHost(IPUtils.getIpAddr(request));
            }
        }
        return session;
    }
}
