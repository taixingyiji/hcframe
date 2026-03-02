package com.taixingyiji.base.common.config;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;

@WebListener
public class webSessionListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent sre)  {
        //将所有request请求都携带上httpSession
        ((HttpServletRequest) sre.getServletRequest()).getSession();

    }
    public webSessionListener() {
    }

    @Override
    public void requestDestroyed(ServletRequestEvent arg0)  {
    }
}
