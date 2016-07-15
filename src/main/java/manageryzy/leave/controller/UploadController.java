package manageryzy.leave.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import manageryzy.leave.err.ErrNo;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * file upload controller
 * Created by manageryzy on 7/12/2016.
 */
public class UploadController extends LeaveController {
    public UploadController(HttpServletRequest request, HttpSession session, SqlSession sqlSession) {
        super(request, session, sqlSession);
    }

    @Override
    public JSON router(String route) {
        switch (route) {
            case "upload-invite":
                return uploadInvite();
            case "upload-bill":
                return uploadBill();
            default:
                return defaultRoute();
        }
    }

    private JSON uploadInvite() {
        JSONObject json = new JSONObject();

        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (uid != leave.getUid()) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_LEAVE_EDIT) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }



        return json;
    }

    private JSON uploadBill() {
        JSONObject json = new JSONObject();

        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (uid != leave.getUid()) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_LEAVE_EDIT) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        return json;
    }
}
