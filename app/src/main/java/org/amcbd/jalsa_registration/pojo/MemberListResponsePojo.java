package org.amcbd.jalsa_registration.pojo;

import java.util.List;

public class MemberListResponsePojo {

    private int status;
    private String message;

    private List<MemberListData> members;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MemberListData> getMembers() {
        return members;
    }

    public void setMembers(List<MemberListData> members) {
        this.members = members;
    }
}
