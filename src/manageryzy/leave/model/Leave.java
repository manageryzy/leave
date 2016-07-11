package manageryzy.leave.model;

import java.util.Date;

/**
 * Created by manageryzy on 7/11/2016.
 */
public class Leave {
    private int id;
    private int status;
    private int uid;
    private String leave_aim;
    private String leave_target;
    private Date leave_leave_date;
    private Date leave_back_date;
    private String leave_type;
    private String leave_plan;
    private String leave_invite;
    private String leave_comment;
    private int pre_money;
    private String pre_comment;
    private Date sum_acc_leave_date;
    private Date sum_acc_back_date;
    private int sum_acc_money;
    private String sum_sum;
    private String sum_comment;
    private int after_money;
    private String after_comment;

    public Leave() {
        this.id = -1;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getLeave_aim() {
        return leave_aim;
    }

    public void setLeave_aim(String leave_aim) {
        this.leave_aim = leave_aim;
    }

    public String getLeave_target() {
        return leave_target;
    }

    public void setLeave_target(String leave_target) {
        this.leave_target = leave_target;
    }

    public Date getLeave_leave_date() {
        return leave_leave_date;
    }

    public void setLeave_leave_date(Date leave_leave_date) {
        this.leave_leave_date = leave_leave_date;
    }

    public Date getLeave_back_date() {
        return leave_back_date;
    }

    public void setLeave_back_date(Date leave_back_date) {
        this.leave_back_date = leave_back_date;
    }

    public String getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(String leave_type) {
        this.leave_type = leave_type;
    }

    public String getLeave_plan() {
        return leave_plan;
    }

    public void setLeave_plan(String leave_plan) {
        this.leave_plan = leave_plan;
    }

    public String getLeave_invite() {
        return leave_invite;
    }

    public void setLeave_invite(String leave_invite) {
        this.leave_invite = leave_invite;
    }

    public String getLeave_comment() {
        return leave_comment;
    }

    public void setLeave_comment(String leave_comment) {
        this.leave_comment = leave_comment;
    }

    public int getPre_money() {
        return pre_money;
    }

    public void setPre_money(int pre_money) {
        this.pre_money = pre_money;
    }

    public String getPre_comment() {
        return pre_comment;
    }

    public void setPre_comment(String pre_comment) {
        this.pre_comment = pre_comment;
    }

    public Date getSum_acc_leave_date() {
        return sum_acc_leave_date;
    }

    public void setSum_acc_leave_date(Date sum_acc_leave_date) {
        this.sum_acc_leave_date = sum_acc_leave_date;
    }

    public Date getSum_acc_back_date() {
        return sum_acc_back_date;
    }

    public void setSum_acc_back_date(Date sum_acc_back_date) {
        this.sum_acc_back_date = sum_acc_back_date;
    }

    public int getSum_acc_money() {
        return sum_acc_money;
    }

    public void setSum_acc_money(int sum_acc_money) {
        this.sum_acc_money = sum_acc_money;
    }

    public String getSum_sum() {
        return sum_sum;
    }

    public void setSum_sum(String sum_sum) {
        this.sum_sum = sum_sum;
    }

    public String getSum_comment() {
        return sum_comment;
    }

    public void setSum_comment(String sum_comment) {
        this.sum_comment = sum_comment;
    }

    public int getAfter_money() {
        return after_money;
    }

    public void setAfter_money(int after_money) {
        this.after_money = after_money;
    }

    public String getAfter_comment() {
        return after_comment;
    }

    public void setAfter_comment(String after_comment) {
        this.after_comment = after_comment;
    }
}
