package manageryzy.leave.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * role class
 * Created by manageryzy on 7/12/2016.
 */
public class Role {
    public final static int ROLE_NONE = 0;
    public final static int ROLE_BOSS = 1;//总经理
    public final static int ROLE_ACCOUNTANT = 2;//会计（财务）
    public final static int ROLE_COST = 4;//成本控制人员
    public final static int ROLE_DEP = 8;//部门经理
    public final static int ROLE_USER_1 = 16;//员工类型1
    public final static int ROLE_USER_2 = 32;//员工类型2
    private static Role role = null;
    private Map<Integer,Cost> costList = null;

    public static Role getRole() {
        if (role == null) {
            role = new Role();
        }

        return role;
    }

    public Role() {
        costList = new HashMap<>();
        costList.put(ROLE_NONE, new Cost(0, 0));
        costList.put(ROLE_BOSS, new Cost(0, 0));
        costList.put(ROLE_ACCOUNTANT, new Cost(0, 0));
        costList.put(ROLE_COST, new Cost(0, 0));
        costList.put(ROLE_DEP, new Cost(0, 0));
        costList.put(ROLE_USER_1, new Cost(5000, 5000));
        costList.put(ROLE_USER_2, new Cost(6000, 8000));
    }

    public Cost getCost(int role) {
        return costList.get(role);
    }

    //需要审核的限额
    public class Cost {
        public int pre;
        public int after;

        public Cost(int pre, int after) {
            this.pre = pre;
            this.after = after;
        }
    }
}
