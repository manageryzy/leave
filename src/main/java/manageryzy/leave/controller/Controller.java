package manageryzy.leave.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import manageryzy.leave.err.ErrNo;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
    protected ServletFileUpload upload;

    Controller(HttpServletRequest request, HttpSession session, SqlSession sqlSession, ServletFileUpload upload) {
        this.request = request;
        this.session = session;
        this.sqlSession = sqlSession;
        this.upload = upload;
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
