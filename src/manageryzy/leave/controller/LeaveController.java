package manageryzy.leave.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import manageryzy.leave.model.Leave;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * core leave controller
 * Created by manageryzy on 7/12/2016.
 */
public class LeaveController extends Controller {
    protected int role = Role.ROLE_NONE;//用户角色
    protected int uid = 0;//用户ID
    protected int id = 0;//出差ID
    protected Leave leave = null;


    public LeaveController(HttpServletRequest request, HttpSession session, SqlSession sqlSession) {
        super(request, session, sqlSession);

        if (session.getAttribute("role") != null) {
            role = (int) session.getAttribute("role");
        }

        if (session.getAttribute("uid") != null) {
            uid = (int) session.getAttribute("uid");
        }

        if (request.getParameter("id") != null) {
            id = Integer.parseInt(request.getParameter("id"));
        }

        if (id != 0) {
            leave = sqlSession.selectOne("manageryzy.leave.mapper.LeaveMapper.selectByID", id);
        }

    }

    /**
     * 成本控制打回
     *
     * @return JSON
     */
    private static JSON leaveCostBack() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 预借款编辑
     *
     * @return JSON
     */
    private static JSON preMoneyEdit() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 预借款打回（流程图上没有这一步，不过感觉有点智障就加上了）
     *
     * @return JSON
     */
    private static JSON preMoneyBack() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
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
            case "/after-reject":
                return aferMoneyReject();
            case "/after-boss-pass":
                return afterMoneyBossPass();
            case "/after-boss-back":
                return afterMoneyBossPass();
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
            default:
                return defaultRoute();
        }

    }

    /**
     * 新建出差
     *
     * @return JSON
     */
    private JSON leaveNew() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 编辑出差
     *
     * @return JSON
     */
    private JSON leaveEdit() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 部门经理通过
     *
     * @return JSON
     */
    private JSON leavePass() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 部门经理打会
     *
     * @return JSON
     */
    private JSON leaveBack() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 部门经理拒绝
     *
     * @return JSON
     */
    private JSON leaveReject() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 成本控制通过
     *
     * @return JSON
     */
    private JSON leaveCostPass() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 成本控制拒绝
     *
     * @return JSON
     */
    private JSON leaveCostReject() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 经理预借款通过
     *
     * @return JSON
     */
    private JSON preMoneyPass() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 预借款经理通过
     *
     * @return JSON
     */
    private JSON preMoneyBossPass() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 预借款经理拒绝（流程图上没有打回这个挺坑的，算了，我按照流程图做好了）
     *
     * @return JSON
     */
    private JSON preMoneyBossReject() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 财务支付预借款
     *
     * @return JSON
     */
    private JSON preMoneyPay() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 编辑出差总结
     *
     * @return JSON
     */
    private JSON sumEdit() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 经理通过总结
     *
     * @return JSON
     */
    private JSON sumPass() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 经理打回总结
     *
     * @return JSON
     */
    private JSON sumBack() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 报销编辑
     *
     * @return JSON
     */
    private JSON afterMoneyEdit() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 报销经理通过
     *
     * @return JSON
     */
    private JSON afterMoneyPass() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 报销经理打回
     *
     * @return JSON
     */
    private JSON afterMoneyBack() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 报销经理拒绝（什么？这些钱我就自己掏了？）
     *
     * @return JSON
     */
    private JSON aferMoneyReject() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 报销总经理通过
     *
     * @return JSON
     */
    private JSON afterMoneyBossPass() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 报销总经理打回
     *
     * @return JSON
     */
    private JSON afterMoneyBossBack() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 报销总经理拒绝（无语中，都到这里了，钱还自己掏了）
     *
     * @return JSON
     */
    private JSON afterMoneyBossReject() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 报销财务支付
     *
     * @return JSON
     */
    private JSON afterMoneyPay() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 获得某个出差的详情
     *
     * @return JSON
     */
    private JSON getLeaveByID() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 获得某个用户的出差
     *
     * @return JSON
     */
    private JSON getLeaveByUser() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }

    /**
     * 根据状态获得出差信息
     *
     * @return JSON
     */
    private JSON getLeaveByStatus() {
        JSONObject json = new JSONObject();
        //TODO:
        return json;
    }
}