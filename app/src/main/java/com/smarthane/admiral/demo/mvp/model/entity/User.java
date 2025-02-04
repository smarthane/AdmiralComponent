package com.smarthane.admiral.demo.mvp.model.entity;

/**
 * @author smarthane
 * @time 2019/10/27 16:08
 * @describe
 */
public class User {
    private final int id;
    private final String login;
    private final String avatar_url;

    public User(int id, String login, String avatar_url) {
        this.id = id;
        this.login = login;
        this.avatar_url = avatar_url;
    }

    public String getAvatarUrl() {
        if (avatar_url.isEmpty()) return avatar_url;
        return avatar_url.split("\\?")[0];
    }


    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    @Override public String toString() {
        return "id -> " + id + " login -> " + login;
    }
}
