package com.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.database.DBConnectionManager;

public class BaseServletWithDB extends HttpServlet{

	protected DBConnectionManager connMgr;

    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        connMgr = DBConnectionManager.getInstance();
    }
    
    public void destroy() {
        connMgr.release();
        super.destroy();
    }
}
