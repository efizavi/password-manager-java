package com.hit.dm;

public abstract class AbstractSecretDTO extends AbstractSerializable{
    // This class is for data transfer objects.
    // It is not to be written into the DAO.

    private String secretName;

    public String getSecretName() {
        return secretName;
    }

    public void setSecretName(String secretName) {
        this.secretName = secretName;
    }

    private String ownerUserName;

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    private SecretCategory secretCategory;

    public SecretCategory getSecretCategory() {
        return secretCategory;
    }

    public void setSecretCategory(SecretCategory secretCategory) {
        this.secretCategory = secretCategory;
    }

    private String domain;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
