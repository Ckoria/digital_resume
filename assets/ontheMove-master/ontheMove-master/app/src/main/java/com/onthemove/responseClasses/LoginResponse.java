package com.onthemove.responseClasses;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {


    /**
     * status : true
     * message : Login successfully
     * data : {"agent_id":9,"agent_name":"test","email":"test@gmail.com","mobile_number":"7405464260","agent_status":"active","busy_free":"free","role":"serviceboy","device_type":"android","device_token":"test","last_login_datetime":"2021-05-27 06:29:19","token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiN2ZhMTdjM2YzMzViZmI1Y2FlNzFjYzQ4YjgyYzk2OGJmM2U0NmI5NmM5MDUzMzQxZDIxZWYxZDVlMjFkZThhZDI1YzJmMjZlZGQ0MGRiODgiLCJpYXQiOjE2MjIwOTY5NTksIm5iZiI6MTYyMjA5Njk1OSwiZXhwIjoxNjUzNjMyOTU5LCJzdWIiOiI5Iiwic2NvcGVzIjpbXX0.hXCTEBU-Df1xoBKITfVfI_46oas8tqzhvH19ck-Bfqw_83j99oFnyTe9E_8VQPe6veTUd0HJu5p4szO_1cdANzHBp4LZ_gB8WmjqFWTCE29LUV09xWPvJQ6gZiqBJ-RoXHx8fuPn2Wfg5WCd90r41lWl-IAdSh-AOAQ5J2GcPSET-ZxQUaC6sHqRf45pjr9cX-DY8mcBIGlJ47w9bLSROa9QmTSRy3xPmJiaEpgGWQVcHR2xTZQSuaSBCz9tUDUSzZUncv4Kx-NlhZjca7JaetEeb_Smq57IgNc4Dw3xgraaO0-x8COsNPSF6T1U-1it9bvRGwN44nhcMUH2fzIqxkz7LZ9op3RqIVKdS6gZYSoWfa9pc3V4n6X95TqO71PbUNshK_NXHLYR3IWE_gHnUNNTjVPt7IbF8Acz3ydeg0I-rQPVictIn0YatfjTWmLYylWlxUIQ-pnyGJNGoHhsNJSEHU3SCrTWD6bTfoGA-qRdWeyYibeghbHM0drE-Hawc33rTqaBQGk3CVdbyQbGVlHi-F_tthotg2hpfiy_5xmfBV8TjfJ0uqQODnjBPXe9Nlt47uTyhDYaN8gfk3cBH1fuHyxEgHDdvw6pLwARd7WfM3Qg_TmzCNbJE7aLnrhknF_ih4FtmUsgIbh2gwN21PXhWjp998LjNfMmlYeufcc"}
     */

    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DataBean data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * agent_id : 9
         * agent_name : test
         * email : test@gmail.com
         * mobile_number : 7405464260
         * agent_status : active
         * busy_free : free
         * role : serviceboy
         * device_type : android
         * device_token : test
         * last_login_datetime : 2021-05-27 06:29:19
         * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiN2ZhMTdjM2YzMzViZmI1Y2FlNzFjYzQ4YjgyYzk2OGJmM2U0NmI5NmM5MDUzMzQxZDIxZWYxZDVlMjFkZThhZDI1YzJmMjZlZGQ0MGRiODgiLCJpYXQiOjE2MjIwOTY5NTksIm5iZiI6MTYyMjA5Njk1OSwiZXhwIjoxNjUzNjMyOTU5LCJzdWIiOiI5Iiwic2NvcGVzIjpbXX0.hXCTEBU-Df1xoBKITfVfI_46oas8tqzhvH19ck-Bfqw_83j99oFnyTe9E_8VQPe6veTUd0HJu5p4szO_1cdANzHBp4LZ_gB8WmjqFWTCE29LUV09xWPvJQ6gZiqBJ-RoXHx8fuPn2Wfg5WCd90r41lWl-IAdSh-AOAQ5J2GcPSET-ZxQUaC6sHqRf45pjr9cX-DY8mcBIGlJ47w9bLSROa9QmTSRy3xPmJiaEpgGWQVcHR2xTZQSuaSBCz9tUDUSzZUncv4Kx-NlhZjca7JaetEeb_Smq57IgNc4Dw3xgraaO0-x8COsNPSF6T1U-1it9bvRGwN44nhcMUH2fzIqxkz7LZ9op3RqIVKdS6gZYSoWfa9pc3V4n6X95TqO71PbUNshK_NXHLYR3IWE_gHnUNNTjVPt7IbF8Acz3ydeg0I-rQPVictIn0YatfjTWmLYylWlxUIQ-pnyGJNGoHhsNJSEHU3SCrTWD6bTfoGA-qRdWeyYibeghbHM0drE-Hawc33rTqaBQGk3CVdbyQbGVlHi-F_tthotg2hpfiy_5xmfBV8TjfJ0uqQODnjBPXe9Nlt47uTyhDYaN8gfk3cBH1fuHyxEgHDdvw6pLwARd7WfM3Qg_TmzCNbJE7aLnrhknF_ih4FtmUsgIbh2gwN21PXhWjp998LjNfMmlYeufcc
         */

        @SerializedName("agent_id")
        private int agentId;
        @SerializedName("agent_name")
        private String agentName;
        @SerializedName("email")
        private String email;
        @SerializedName("mobile_number")
        private String mobileNumber;
        @SerializedName("agent_status")
        private String agentStatus;
        @SerializedName("busy_free")
        private String busyFree;
        @SerializedName("role")
        private String role;
        @SerializedName("device_type")
        private String deviceType;
        @SerializedName("device_token")
        private String deviceToken;
        @SerializedName("last_login_datetime")
        private String lastLoginDatetime;
        @SerializedName("token")
        private String token;

        public int getAgentId() {
            return agentId;
        }

        public void setAgentId(int agentId) {
            this.agentId = agentId;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getAgentStatus() {
            return agentStatus;
        }

        public void setAgentStatus(String agentStatus) {
            this.agentStatus = agentStatus;
        }

        public String getBusyFree() {
            return busyFree;
        }

        public void setBusyFree(String busyFree) {
            this.busyFree = busyFree;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getLastLoginDatetime() {
            return lastLoginDatetime;
        }

        public void setLastLoginDatetime(String lastLoginDatetime) {
            this.lastLoginDatetime = lastLoginDatetime;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
