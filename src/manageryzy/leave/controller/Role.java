package manageryzy.leave.controller;

import java.util.ArrayList;

/**
 * role class
 * Created by manag on 7/12/2016.
 */
public class Role {
    public final static int ROLE_NONE = 0;
    public final static int ROLE_BOSS = 1;//总经理
    public final static int ROLE_ACCOUNTANT = 2;//会计（财务）
    public final static int ROLE_COST = 3;//成本控制人员
    public final static int ROLE_DEP = 4;//部门经理
    public final static int ROLE_USER_1 = 5;//员工类型1
    public final static int ROLE_USER_2 = 6;//员工类型2
    private static Role role = null;
    private ArrayList<Cost> costList = null;

    public void Role() {
        costList = new ArrayList<>();
        costList.add(ROLE_NONE, new Cost(0, 0));
        costList.add(ROLE_BOSS, new Cost(0, 0));
        costList.add(ROLE_ACCOUNTANT, new Cost(0, 0));
        costList.add(ROLE_COST, new Cost(0, 0));
        costList.add(ROLE_DEP, new Cost(0, 0));
        costList.add(ROLE_USER_1, new Cost(5000, 5000));
        costList.add(ROLE_USER_2, new Cost(6000, 8000));
    }

    public Cost getCost(int role) {
        return costList.get(role);
    }

    public Role getRole() {
        if (role == null) {
            role = new Role();
        }

        return role;
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
