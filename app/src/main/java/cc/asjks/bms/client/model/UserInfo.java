package cc.asjks.bms.client.model;

public class UserInfo {

    /**
     * {"result":"T","data":{"uid":1,"username":"admin","password":"admin","role":"管理员","trueName":"admin","tel":"admin","regTime":"2019-01-16 18:07:03"}}
     */

    private DataBean data;
    private String result;
    private String token;

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token=token;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static class DataBean {


        private String regTime;
        private int uid;

        private String tel;
        private String username;

        private String role;
        private String password;
        private String trueName;

        public String getRegTime() {
            return regTime;
        }

        public void setRegTime(String regTime) {
            this.regTime=regTime;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }
    }
}
