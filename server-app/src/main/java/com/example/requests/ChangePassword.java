package com.example.requests;

public class ChangePassword {
    private String oldPassword;
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {this.newPassword = newPassword;}

    public String getOldPassword() {return oldPassword;}
    public void setOldPassword(String oldPassword) {this.oldPassword = oldPassword;}
}
