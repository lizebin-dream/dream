package cn.itcast.vo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class ContractTaskVo implements Serializable {


    private String companyName;
    private String contractNo;
    private Date deliveryPeriod;
    private String userName;
    private String email;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Date getDeliveryPeriod() {
        return deliveryPeriod;
    }

    public void setDeliveryPeriod(Date deliveryPeriod) {
        this.deliveryPeriod = deliveryPeriod;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
