package manageryzy.leave.model;

/**
 * Created by manageryzy on 7/11/2016.
 */
public class User {
    private int uid;
    private String name;
    private String pwd;
    private String salt;
    private int privilege;

    public User() {
        this.uid = -1;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }
}
