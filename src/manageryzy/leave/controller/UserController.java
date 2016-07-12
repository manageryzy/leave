package manageryzy.leave.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import manageryzy.leave.err.ErrNo;
import manageryzy.leave.model.User;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

/**
 * user controller
 * Created by manageryzy on 7/12/2016.
 */
public class UserController extends Controller {
    public UserController(HttpServletRequest request, HttpSession session, SqlSession sqlSession) {
        super(request, session, sqlSession);
    }

    public JSON router(String route) {
        switch (route) {
            case "/login":
                return login();
            case "/logout":
                return logout();
            case "/status":
                return status();
            default:
                return defaultRoute();
        }

    }

    private JSON login() {
        JSONObject json = new JSONObject();

        String uid = request.getParameter("uid");
        String pwd = request.getParameter("pwd");
        String roleString = request.getParameter("role");
        int role = 0;

        if (uid == null || pwd == null || roleString == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        try {
            role = Integer.parseInt(roleString);
        } catch (Exception e) {
            json.put("code", ErrNo.ERR_PARMETER);
        }

        User user;
        try {
            user = sqlSession.selectOne("manageryzy.leave.mapper.UserMapper.selectUser");
        } catch (Exception e) {
            json.put("code", ErrNo.ERR_DB);
            return json;
        }

        if (user == null) {
            json.put("code", ErrNo.ERR_LOGIN_FAIL);
            return json;
        }

        try {
            String pass = user.getPwd() + pwd + DatatypeConverter.printHexBinary(MessageDigest.getInstance("MD5").digest(user.getPwd().getBytes("UTF-8")));
            if (!user.getPwd().equals(DatatypeConverter.printHexBinary(MessageDigest.getInstance("MD5").digest(pass.getBytes("UTF-8"))))) {
                //wrong pwd
                json.put("code", ErrNo.ERR_LOGIN_FAIL);
                return json;
            } else {
                if (role != user.getPrivilege()) {
                    json.put("code", ErrNo.ERR_NO_PREVILIGE);
                    return json;
                }

                session.setAttribute("uid", uid);
                session.setAttribute("role", role);
                session.setAttribute("name", user.getName());

                json.put("code", ErrNo.ERR_NONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", ErrNo.ERR_LOGIN_FAIL);
            return json;
        }


        return json;
    }

    private JSON logout() {
        JSONObject json = new JSONObject();
        json.put("code", ErrNo.ERR_NONE);
        session.invalidate();
        return json;
    }

    private JSON status() {
        JSONObject json = new JSONObject();
        int role = 0;
        int uid = 0;
        String name;

        if (session.getAttribute("role") != null) {
            role = (int) session.getAttribute("role");
        }

        if (session.getAttribute("uid") != null) {
            uid = (int) session.getAttribute("uid");
        }

        name = (String) session.getAttribute("name");

        json.put("code", ErrNo.ERR_NONE);
        JSONObject data = new JSONObject();
        data.put("uid", uid);
        data.put("role", role);
        data.put("name", name);
        json.put("data", data);

        return json;
    }
}
