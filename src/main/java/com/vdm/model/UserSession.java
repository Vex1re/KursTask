package com.vdm.model;

public class UserSession {
    private static UserSession instance;
    private User currentUser;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public User getUser() {
        return currentUser;
    }

    public void clearSession() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isPrivileged() {
        return isLoggedIn() && currentUser.getRoleId() == 1;
    }

    public boolean canManageClients() {
        return isLoggedIn();
    }
}
