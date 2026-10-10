/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import GUI.AdminLoginPanel;
/**
 *
 * @author rownavanana
 */
public class Admin {
    private final String VALID_ADMIN_USER = "admin1";
    private final String VALID_ADMIN_PASS = "admin123";

    public int validateLogin(AdminLoginPanel panel) {
        String username = panel.txtAdminUser.getText().trim();
        String password = new String(panel.txtAdminPass.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            return 0;
        }

        if (username.equals(VALID_ADMIN_USER) && password.equals(VALID_ADMIN_PASS)) {
            return 1;
        }

        return -1;
    }

    public boolean handleForgotPassword(AdminLoginPanel panel) {
        String username = panel.txtAdminUser.getText().trim();
        String password = new String(panel.txtAdminPass.getPassword());

        if (username.isEmpty() && password.isEmpty()) {
            return false;
        } else {
            clearFields(panel);
            return true;
        }
    }

    public void clearFields(AdminLoginPanel panel) {
        panel.txtAdminUser.setText("");
        panel.txtAdminPass.setText("");
        if (panel.chkAdminSavePass != null) {
            panel.chkAdminSavePass.setSelected(false);
        }
    }
}