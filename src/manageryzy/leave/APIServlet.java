package manageryzy.leave;

import com.alibaba.fastjson.JSON;
import manageryzy.leave.controller.LeaveController;
import manageryzy.leave.controller.UploadController;
import manageryzy.leave.controller.UserController;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;

/**
 * Servlet 入口
 * Created by manageryzy on 7/12/2016.
 */
public class APIServlet extends javax.servlet.http.HttpServlet {
    private SqlSessionFactory sessionFactory;

    public APIServlet() {
        super();
        InputStream is = APIServlet.class.getClassLoader().getResourceAsStream("manageryzy/leave/conf.xml");
        sessionFactory = new SqlSessionFactoryBuilder().build(is);
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doRequest(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doRequest(request, response);
    }

    private void doRequest(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        JSON res = null;
        HttpSession session = request.getSession();

        response.setContentType("application/json;charset=utf-8");

        SqlSession sqlSession = sessionFactory.openSession();

        String uri = request.getRequestURI();
        String ctl = uri.substring(4, uri.indexOf("/", 5));
        String action = uri.substring(uri.indexOf('/', 5));

        // route
        switch (ctl) {
            case "/user":
                UserController u = new UserController(request, session, sqlSession);
                res = u.router(action);
                break;

            case "/upload":
                UploadController up = new UploadController(request, session, sqlSession);
                res = up.router(action);
                break;

            case "/leave":
                LeaveController l = new LeaveController(request, session, sqlSession);
                res = l.router(action);
                break;

            default:
                response.setStatus(403);
                response.getWriter().println("{\"code\":-1}");
                return;
        }

        assert res != null;
        response.getWriter().println(res.toJSONString());
    }
}
