package manageryzy.leave.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import manageryzy.leave.APIServlet;
import manageryzy.leave.err.ErrNo;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

/**
 * file upload controller
 * Created by manageryzy on 7/12/2016.
 */
public class UploadController extends LeaveController {
    private final static int STATUS_STOP = -1;//被终止
    private final static int STATUS_ERR = 0;//异常状态
    private final static int STATUS_LEAVE_EDIT = 1;//编辑出差申请
    private final static int STATUS_LEAVE_DEP = 2;//出差申请部门审核中
    private final static int STATUS_LEAVE_COST = 3;//出差申请成本控制审核之中
    private final static int STATUS_PRE_EDIT = 5;//编辑预借款
    private final static int STATUS_PRE_DEP = 6;//预借款部门审核中
    private final static int STATUS_PRE_BOSS = 7;//预借款boss审核中
    private final static int STATUS_PRE_PAY = 8;//预借款等待付款
    private final static int STATUS_SUM_EDIT = 9;//总结填写
    private final static int STATUS_SUM_DEP = 10;//总结部门评审中
    private final static int STATUS_AFTER_EDIT = 11;//报销填写中
    private final static int STATUS_AFTER_DEP = 12;//报销部门审核中
    private final static int STATUS_AFTER_BOSS = 13;//报销boss审核中
    private final static int STATUS_AFTER_PAY = 14;//等待报销
    private final static int STATUS_FIN = 15;//完成

    public UploadController(HttpServletRequest request, HttpSession session, SqlSession sqlSession, ServletFileUpload upload) {
        super(request, session, sqlSession, upload);
    }

    @Override
    public JSON router(String route) {
        switch (route) {
            case "/upload-invite":
                return uploadInvite();
            case "/upload-bill":
                return uploadBill();
            case "/list":
                return getListUpload();
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
            json.put("code", ErrNo.ERR_UPLOAD_NOT_ALLOW);
            return json;
        }

        File uploadd = new File(APIServlet.path+"/upload/");
        if (!uploadd.exists()) {
            if (!uploadd.mkdir()) {
                json.put("code", ErrNo.ERR_UPLOAD_IO_ERR);
                return json;
            }
        }

        File uploadDir = new File( APIServlet.path+"/upload/"+leave.getId());
        if(!uploadDir.exists()){
            if (!uploadDir.mkdir()) {
                json.put("code", ErrNo.ERR_UPLOAD_IO_ERR);
                return json;
            }
        }

        File inviteDir = new File(APIServlet.path+"/upload/"+leave.getId()+"/invite");
        if(!inviteDir.exists()){
            if (!inviteDir.mkdir()) {
                json.put("code", ErrNo.ERR_UPLOAD_IO_ERR);
                return json;
            }
        }


        try {
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                String name = item.getName();
                if (name == null) {
                    continue;
                }
                String subName = name.substring(name.lastIndexOf('.'));

                if (!subName.equalsIgnoreCase(".jpg") && !subName.equalsIgnoreCase(".jpeg")) {
                    json.put("code", ErrNo.ERR_UPLOAD_EXT_ERR);
                    return json;
                }

                File file = new File(APIServlet.path+"/upload/" + leave.getId() + "/invite/" + name);

                item.write(file);
            }
        } catch (Exception e) {
            json.put("code", ErrNo.ERR_UPLOAD_IO_ERR);
            return json;
        }

        json.put("code", ErrNo.ERR_NONE);

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

        if (leave.getStatus() != STATUS_AFTER_EDIT) {
            json.put("code", ErrNo.ERR_UPLOAD_NOT_ALLOW);
            return json;
        }

        File uploadd = new File(APIServlet.path+ "/upload/");
        if (!uploadd.exists()) {
            if (!uploadd.mkdir()) {
                json.put("code", ErrNo.ERR_UPLOAD_IO_ERR);
                return json;
            }
        }

        File uploadDir = new File(APIServlet.path+"/upload/"+leave.getId());
        if(!uploadDir.exists()){
            if (!uploadDir.mkdir()) {
                json.put("code", ErrNo.ERR_UPLOAD_IO_ERR);
                return json;
            }
        }

        File billDir = new File(APIServlet.path+"/upload/"+leave.getId()+"/bill");
        if(!billDir.exists()){
            if (!billDir.mkdir()) {
                json.put("code", ErrNo.ERR_UPLOAD_IO_ERR);
                return json;
            }
        }

        try {
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                String name = item.getName();
                if (name == null) {
                    continue;
                }
                String subName = name.substring(name.lastIndexOf('.'));

                if (!subName.equalsIgnoreCase(".jpg") && !subName.equalsIgnoreCase(".jpeg")) {
                    json.put("code", ErrNo.ERR_UPLOAD_EXT_ERR);
                    return json;
                }

                File file = new File(APIServlet.path+"/upload/" + leave.getId() + "/invite/" + name);

                item.write(file);
            }
        } catch (Exception e) {
            json.put("code", ErrNo.ERR_UPLOAD_IO_ERR);
            return json;
        }

        json.put("code", ErrNo.ERR_NONE);

        return json;
    }

    private JSON getListUpload(){
        JSONObject json = new JSONObject();

        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (uid != leave.getUid() && role>= Role.ROLE_USER_1) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        json.put("code",ErrNo.ERR_NONE);

        JSONArray inviteArray = new JSONArray();
        File invite = new File(APIServlet.path+"/upload/" + leave.getId()+"/invite/");
        File[] invites = invite.listFiles();
        if (invites != null) {
            for (File f : invites) {
                inviteArray.add(f.getName());
            }
        }

        JSONArray billArray = new JSONArray();
        File bill = new File(APIServlet.path+"/upload/" + leave.getId()+"/bill/");
        File[] bills = bill.listFiles();
        if (bills != null) {
            for (File f : bills) {
                billArray.add(f.getName());
            }
        }

        json.put("invite", inviteArray);
        json.put("bill", billArray);

        return json;
    }

}
