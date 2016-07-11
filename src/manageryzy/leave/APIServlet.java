package manageryzy.leave;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import manageryzy.leave.controller.UserController;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by manageryzy on 7/12/2016.
 */
public class APIServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doRequest(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doRequest(request, response);
    }

    private void doRequest(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        JSON res = new JSONObject();
        HttpSession session = request.getSession();

        response.setContentType("application/json;charset=utf-8");

        String action = (String) request.getAttribute("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "login":
                res = UserController.login(request, session);
                break;
            case "logout":
                res = UserController.logout(request, session);
                break;
        }

        assert res != null;
        response.getWriter().println(res.toJSONString());

    }
}
