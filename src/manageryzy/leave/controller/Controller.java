package manageryzy.leave.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import manageryzy.leave.err.ErrNo;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * base class of controller
 * Created by manageryzy on 7/12/2016.
 */
class Controller {
    protected HttpServletRequest request;
    protected HttpSession session;
    protected SqlSession sqlSession;

    Controller(HttpServletRequest request, HttpSession session, SqlSession sqlSession) {
        this.request = request;
        this.session = session;
        this.sqlSession = sqlSession;
    }

    public JSON router(String route) {
        return defaultRoute();
    }

    JSON defaultRoute() {
        JSONObject json = new JSONObject();
        json.put("code", ErrNo.ERR_ROUTE_NOT_EXIST);
        return json;
    }
}
