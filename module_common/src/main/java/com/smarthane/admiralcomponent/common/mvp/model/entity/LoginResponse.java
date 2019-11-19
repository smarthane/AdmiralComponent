package com.smarthane.admiralcomponent.common.mvp.model.entity;

/**
 * @author smarthane
 * @time 2019/11/10 16:39
 * @describe
 */
public class LoginResponse {

    private int errorCode;
    private String errorMsg;
    private UserInfo data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    public class UserInfo {
       private boolean admin;
       private String[] chapterTops;
       private String[] collectIds;
       private String email;
       private String icon;
       private int id;
       private String nickname;
       private String password;
       private String publicName;
       private String token;
       private int type;
       private String username;

        public boolean getAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public String[] getChapterTops() {
            return chapterTops;
        }

        public void setChapterTops(String[] chapterTops) {
            this.chapterTops = chapterTops;
        }

        public String[] getCollectIds() {
            return collectIds;
        }

        public void setCollectIds(String[] collectIds) {
            this.collectIds = collectIds;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPublicName() {
            return publicName;
        }

        public void setPublicName(String publicName) {
            this.publicName = publicName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

}
