package cn.itcast.domain.system;

/**
 * 部门
 */
public class Dept {

    private String id;

    private String deptName;

    private Dept parent; // 父部门实体，
                        // 如果只还有parentId封装，那么不方便获取父部门的详细信息。
                        //  封装Dept对象，方便封装父部门的详细信息
    private String state;
    private String companyId;
    private String companyName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Dept getParent() {
        return parent;
    }

    public void setParent(Dept parent) {
        this.parent = parent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "id='" + id + '\'' +
                ", deptName='" + deptName + '\'' +
                ", parent=" + parent +
                ", state='" + state + '\'' +
                ", companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
