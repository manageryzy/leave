package manageryzy.leave.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import manageryzy.leave.err.ErrNo;
import manageryzy.leave.model.Leave;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * core leave controller
 * Created by manageryzy on 7/12/2016.
 */
public class LeaveController extends Controller {
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
    protected int role = Role.ROLE_NONE;//用户角色
    protected int uid = 0;//用户ID
    protected int id = 0;//出差ID
    protected Leave leave = null;


    public LeaveController(HttpServletRequest request, HttpSession session, SqlSession sqlSession, ServletFileUpload upload) {
        super(request, session, sqlSession, upload);

        if (session.getAttribute("role") != null) {
            role = (int) session.getAttribute("role");
        }

        if (session.getAttribute("uid") != null) {
            uid = Integer.parseInt((String) session.getAttribute("uid"));
        }

        if (request.getParameter("id") != null) {
            id = Integer.parseInt(request.getParameter("id"));
        }

        if (id != 0) {
            leave = sqlSession.selectOne("manageryzy.leave.mapper.LeaveMapper.selectByID", id);
        }

    }


    public JSON router(String route) {
        switch (route) {
            case "/leave-new":
                return leaveNew();
            case "/leave-edit":
                return leaveEdit();
            case "/leave-pass":
                return leavePass();
            case "/leave-back":
                return leaveBack();
            case "/leave-reject":
                return leaveReject();
            case "/leave-cost-pass":
                return leaveCostPass();
            case "/leave-cost-back":
                return leaveCostBack();
            case "/leave-cost-reject":
                return leaveCostReject();
            case "/pre-edit":
                return preMoneyEdit();
            case "/pre-pass":
                return preMoneyPass();
            case "/pre-back":
                return preMoneyBack();
            case "/pre-boss-pass":
                return preMoneyBossPass();
            case "/pre-boss-reject":
                return preMoneyBossReject();
            case "/pre-pay":
                return preMoneyPay();
            case "/sum-edit":
                return sumEdit();
            case "/sum-pass":
                return sumPass();
            case "/sum-back":
                return sumBack();
            case "/after-edit":
                return afterMoneyEdit();
            case "/after-pass":
                return afterMoneyPass();
            case "/after-back":
                return afterMoneyBack();
            case "/after-reject":
                return afterMoneyReject();
            case "/after-boss-pass":
                return afterMoneyBossPass();
            case "/after-boss-back":
                return afterMoneyBossBack();
            case "/after-boss-reject":
                return afterMoneyBossReject();
            case "/after-pay":
                return afterMoneyPay();
            case "/get-by-id":
                return getLeaveByID();
            case "/get-by-uid":
                return getLeaveByUser();
            case "/get-by-status":
                return getLeaveByStatus();
            case "/get-all":
                return getAll();
            default:
                return defaultRoute();
        }

    }

    private JSON getAll() {
        JSONObject json = new JSONObject();

        if (role != Role.ROLE_BOSS) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        List<Object> list = sqlSession.selectList("manageryzy.leave.model.Leave.selectAll");
        json.put("code", ErrNo.ERR_NONE);
        json.put("data", list);

        return json;
    }

    /**
     * 新建出差
     *
     * @return JSON
     */
    private JSON leaveNew() {
        JSONObject json = new JSONObject();

        if (role < Role.ROLE_USER_1) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        leave = new Leave();
        leave.setUid(uid);
        leave.setStatus(STATUS_LEAVE_EDIT);
        leave.setRole(role);

        if (sqlSession.insert("manageryzy.leave.mapper.LeaveMapper.insertLeave", leave) != 1) {
            json.put("code", ErrNo.ERR_DB);
            return json;
        }

        json.put("code", ErrNo.ERR_NONE);
        return json;
    }

    /**
     * 编辑出差
     *
     * @return JSON
     */
    private JSON leaveEdit() {
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


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            leave.setLeave_aim(request.getParameter("leave-aim"));
            leave.setLeave_target(request.getParameter("leave-target"));
            leave.setLeave_leave_date(sdf.parse(request.getParameter("leave-leave-date")));
            leave.setLeave_back_date(sdf.parse(request.getParameter("leave-back-date")));
            leave.setLeave_type(request.getParameter("leave-type"));
            leave.setLeave_plan(request.getParameter("leave-plan"));
            leave.setLeave_invite(request.getParameter("leave-invite"));
        } catch (Exception e) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (!leave.checkLeave()) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_LEAVE_DEP);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 部门经理通过
     *
     * @return JSON
     */
    private JSON leavePass() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_DEP) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_LEAVE_DEP) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_LEAVE_COST);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 部门经理打会
     *
     * @return JSON
     */
    private JSON leaveBack() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_DEP) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_LEAVE_DEP) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        String comment = request.getParameter("comment");
        if (comment == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_LEAVE_EDIT);
        leave.setLeave_comment(comment);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 部门经理拒绝
     *
     * @return JSON
     */
    private JSON leaveReject() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_DEP) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_LEAVE_DEP) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        String comment = request.getParameter("comment");
        if (comment == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_STOP);
        leave.setLeave_comment(comment);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 成本控制通过
     *
     * @return JSON
     */
    private JSON leaveCostPass() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_COST) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_LEAVE_COST) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_PRE_EDIT);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 成本控制打回
     *
     * @return JSON
     */
    private JSON leaveCostBack() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_COST) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_LEAVE_COST) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        String comment = request.getParameter("comment");
        if (comment == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_LEAVE_EDIT);
        leave.setLeave_comment(comment);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 成本控制拒绝
     *
     * @return JSON
     */
    private JSON leaveCostReject() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_COST) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_LEAVE_COST) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        String comment = request.getParameter("comment");
        if (comment == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_STOP);
        leave.setLeave_comment(comment);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 预借款编辑
     *
     * @return JSON
     */
    private JSON preMoneyEdit() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (uid != leave.getUid()) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_PRE_EDIT) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        int pre_money;

        try {
            pre_money = Integer.parseInt(request.getParameter("pre-money"));
        } catch (Exception e) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setPre_money(pre_money);
        leave.setStatus(STATUS_PRE_DEP);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 经理预借款通过
     *
     * @return JSON
     */
    private JSON preMoneyPass() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_DEP) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_PRE_DEP) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (leave.getPre_money() > Role.getRole().getCost(leave.getRole()).pre) {
            leave.setStatus(STATUS_PRE_BOSS);
        } else {
            leave.setStatus(STATUS_PRE_PAY);
        }

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 预借款打回（流程图上没有这一步，不过感觉有点智障就加上了）
     *
     * @return JSON
     */
    private JSON preMoneyBack() {
        JSONObject json = new JSONObject();

        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_DEP) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_PRE_DEP) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        String comment = request.getParameter("comment");
        if (comment == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_PRE_EDIT);
        leave.setPre_comment(comment);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 预借款经理通过
     *
     * @return JSON
     */
    private JSON preMoneyBossPass() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_BOSS) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_PRE_BOSS) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_PRE_PAY);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 预借款经理拒绝（流程图上没有打回这个挺坑的，算了，我按照流程图做好了）
     *
     * @return JSON
     */
    private JSON preMoneyBossReject() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_BOSS) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_PRE_BOSS) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        String comment = request.getParameter("comment");
        if (comment == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_STOP);
        leave.setPre_comment(comment);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 财务支付预借款
     *
     * @return JSON
     */
    private JSON preMoneyPay() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_ACCOUNTANT) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_PRE_PAY) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_SUM_EDIT);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 编辑出差总结
     *
     * @return JSON
     */
    private JSON sumEdit() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (uid != leave.getUid()) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_SUM_EDIT) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            leave.setSum_sum(request.getParameter("sum-sum"));
            leave.setSum_acc_money(Integer.parseInt(request.getParameter("sum-acc-money")));
            leave.setSum_acc_leave_date(sdf.parse(request.getParameter("sum-acc-leave-date")));
            leave.setSum_acc_back_date(sdf.parse(request.getParameter("sum-acc-back-date")));
        } catch (Exception e) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (!leave.checkSum()) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_SUM_DEP);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 经理通过总结
     *
     * @return JSON
     */
    private JSON sumPass() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_DEP) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_SUM_DEP) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_AFTER_EDIT);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 经理打回总结
     *
     * @return JSON
     */
    private JSON sumBack() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_DEP) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_SUM_DEP) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        String comment = request.getParameter("comment");
        if (comment == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_SUM_EDIT);
        leave.setSum_comment(comment);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 报销编辑
     *
     * @return JSON
     */
    private JSON afterMoneyEdit() {
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
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        int after_money;

        try {
            after_money = Integer.parseInt(request.getParameter("after-money"));
        } catch (Exception e) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setAfter_money(after_money);
        leave.setStatus(STATUS_AFTER_DEP);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 报销经理通过
     *
     * @return JSON
     */
    private JSON afterMoneyPass() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_DEP) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_AFTER_DEP) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (leave.getAfter_money() > Role.getRole().getCost(leave.getRole()).after) {
            leave.setStatus(STATUS_AFTER_BOSS);
        } else {
            leave.setStatus(STATUS_AFTER_PAY);
        }

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 报销经理打回
     *
     * @return JSON
     */
    private JSON afterMoneyBack() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_DEP) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_AFTER_DEP) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        String comment = request.getParameter("comment");
        if (comment == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_AFTER_EDIT);
        leave.setAfter_comment(comment);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 报销经理拒绝（什么？这些钱我就自己掏了？）
     *
     * @return JSON
     */
    private JSON afterMoneyReject() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_DEP) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_AFTER_DEP) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        String comment = request.getParameter("comment");
        if (comment == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_STOP);
        leave.setAfter_comment(comment);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 报销总经理通过
     *
     * @return JSON
     */
    private JSON afterMoneyBossPass() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_BOSS) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_AFTER_BOSS) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_AFTER_PAY);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 报销总经理打回
     *
     * @return JSON
     */
    private JSON afterMoneyBossBack() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_BOSS) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_AFTER_BOSS) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        String comment = request.getParameter("comment");
        if (comment == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_AFTER_EDIT);
        leave.setAfter_comment(comment);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 报销总经理拒绝（无语中，都到这里了，钱还自己掏了）
     *
     * @return JSON
     */
    private JSON afterMoneyBossReject() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_BOSS) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_AFTER_BOSS) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        String comment = request.getParameter("comment");
        if (comment == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_STOP);
        leave.setAfter_comment(comment);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 报销财务支付
     *
     * @return JSON
     */
    private JSON afterMoneyPay() {
        JSONObject json = new JSONObject();
        if (leave == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if (role != Role.ROLE_ACCOUNTANT) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if (leave.getStatus() != STATUS_AFTER_PAY) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        leave.setStatus(STATUS_FIN);

        if (sqlSession.update("manageryzy.leave.mapper.LeaveMapper.updateLeave", leave) == 0) {
            json.put("code", ErrNo.ERR_DB);
        } else {
            json.put("code", ErrNo.ERR_NONE);
        }

        return json;
    }

    /**
     * 获得某个出差的详情
     *
     * @return JSON
     */
    private JSON getLeaveByID() {
        JSONObject json = new JSONObject();

        if (leave == null) {
            json.put("code",ErrNo.ERR_NOT_FOUND);
            return json;
        }

        if (leave.getUid() != uid) {
            if(role >= Role.ROLE_USER_1){
                json.put("code", ErrNo.ERR_NO_PREVILIGE);
            }
        }

        json.put("code", ErrNo.ERR_NONE);
        json.put("data", leave);

        return json;
    }

    /**
     * 获得某个用户的出差
     *
     * @return JSON
     */
    private JSON getLeaveByUser() {
        JSONObject json = new JSONObject();

        List<Object> list = sqlSession.selectList("manageryzy.leave.mapper.LeaveMapper.selectByUser", uid);
        if (list != null) {
            json.put("code", ErrNo.ERR_NONE);
            json.put("data", list);
        }else {
            json.put("code", ErrNo.ERR_DB);
        }

        return json;
    }

    /**
     * 根据状态获得出差信息
     *
     * @return JSON
     */
    private JSON getLeaveByStatus() {
        JSONObject json = new JSONObject();

        String s = request.getParameter("status");
        if (s == null) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        int status = Integer.parseInt(s);

        if (status == 0) {
            json.put("code", ErrNo.ERR_PARMETER);
            return json;
        }

        if(role==Role.ROLE_DEP) {
            switch (status) {
                case STATUS_AFTER_DEP:
                case STATUS_LEAVE_DEP:
                case STATUS_PRE_DEP:
                case STATUS_SUM_DEP:
                    break;
                default:
                    json.put("code", ErrNo.ERR_NO_PREVILIGE);
                    return json;
            }
        }

        if(role==Role.ROLE_COST && status!=STATUS_LEAVE_COST) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        if(role==Role.ROLE_ACCOUNTANT){
            switch (status) {
                case STATUS_AFTER_PAY:
                case STATUS_PRE_PAY:
                    break;
                default:
                    json.put("code", ErrNo.ERR_NO_PREVILIGE);
                    return json;
            }
        }

        if(role==Role.ROLE_BOSS){
            switch (status) {
                case STATUS_AFTER_BOSS:
                case STATUS_PRE_BOSS:
                    break;
                default:
                    json.put("code", ErrNo.ERR_NO_PREVILIGE);
                    return json;
            }
        }

        if (role >= Role.ROLE_USER_1) {
            json.put("code", ErrNo.ERR_NO_PREVILIGE);
            return json;
        }

        List<Object> list = sqlSession.selectList("manageryzy.leave.mapper.LeaveMapper.selectByStatus", status);
        if (list != null) {
            json.put("code", ErrNo.ERR_NONE);
            json.put("data", list);
        }else {
            json.put("code", ErrNo.ERR_DB);
        }

        return json;
    }
}
