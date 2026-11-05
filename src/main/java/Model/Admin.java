/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to edit this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Database.databaseConnection;
import GUI.AdminDashboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * @author rownavanana and calalang
 * 
 */
public class Admin implements ActionListener {

    private AdminDashboard adminDashboard;
    private String currentView = "OVERVIEW";

    public Admin() {
    }

    public void setAdminDashboard(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;
        registerAdminListeners();
        loadOverviewData();
    }

    private void registerAdminListeners() {
        if (adminDashboard != null) {
            try { adminDashboard.getBtnNavOverview().addActionListener(this); } catch (Exception e) {}
            try { adminDashboard.getBtnNavUsers().addActionListener(this); } catch (Exception e) {}
            try { adminDashboard.getBtnNavRestaurants().addActionListener(this); } catch (Exception e) {}
            try { adminDashboard.getBtnNavOrders().addActionListener(this); } catch (Exception e) {}
            try { adminDashboard.getBtnDeleteSelectedUser().addActionListener(this); } catch (Exception e) {}
            try { adminDashboard.getBtnRefreshData().addActionListener(this); } catch (Exception e) {}
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (adminDashboard == null) return;

        Object source = e.getSource();

        if (source == adminDashboard.getBtnNavOverview()) {
            currentView = "OVERVIEW";
            
            if (adminDashboard.getLblName() != null) {
                adminDashboard.getLblName().setText("Hello, Admin!");
                adminDashboard.getLblName().setVisible(true);
            }
            
            if (adminDashboard.getCardMetricsContainer() != null) adminDashboard.getCardMetricsContainer().setVisible(true);
            if (adminDashboard.getPnlProfileCard() != null) adminDashboard.getPnlProfileCard().setVisible(false);
            if (adminDashboard.getTableScrollPane() != null) adminDashboard.getTableScrollPane().setVisible(false);
            toggleActionButtons(false);
            loadOverviewData();
        } 
        else if (source == adminDashboard.getBtnNavUsers()) {
            currentView = "USERS";
            
            if (adminDashboard.getLblName() != null) {
                adminDashboard.getLblName().setText("Manage Customer Accounts");
                adminDashboard.getLblName().setVisible(true);
            }
            
            if (adminDashboard.getCardMetricsContainer() != null) adminDashboard.getCardMetricsContainer().setVisible(false);
            if (adminDashboard.getPnlProfileCard() != null) adminDashboard.getPnlProfileCard().setVisible(false);
            if (adminDashboard.getTableScrollPane() != null) adminDashboard.getTableScrollPane().setVisible(true);
            toggleActionButtons(true);
            loadUsersFromDatabase();
        } 
        else if (source == adminDashboard.getBtnNavRestaurants()) {
            currentView = "RESTAURANTS";
            
            if (adminDashboard.getLblName() != null) {
                adminDashboard.getLblName().setText("Restaurant Accounts");
                adminDashboard.getLblName().setVisible(true);
            }
            
            if (adminDashboard.getCardMetricsContainer() != null) adminDashboard.getCardMetricsContainer().setVisible(false);
            if (adminDashboard.getPnlProfileCard() != null) adminDashboard.getPnlProfileCard().setVisible(false);
            if (adminDashboard.getTableScrollPane() != null) adminDashboard.getTableScrollPane().setVisible(true);
            toggleActionButtons(true);
            loadRestaurantsFromDatabase();
        } 
        else if (source == adminDashboard.getBtnNavOrders()) { 
            currentView = "PROFILE";
            
            if (adminDashboard.getLblName() != null) {
                adminDashboard.getLblName().setText("Admin Profile");
                adminDashboard.getLblName().setVisible(true);
            }
            
            if (adminDashboard.getCardMetricsContainer() != null) adminDashboard.getCardMetricsContainer().setVisible(false);
            if (adminDashboard.getPnlProfileCard() != null) adminDashboard.getPnlProfileCard().setVisible(true);
            if (adminDashboard.getTableScrollPane() != null) adminDashboard.getTableScrollPane().setVisible(false);
            toggleActionButtons(false);
        }
    }

    public void actionProfilesOrResto(ActionEvent e) {
        actionPerformed(e);
    }

    private void toggleActionButtons(boolean show) {
        try { adminDashboard.getBtnDeleteSelectedUser().setVisible(show); } catch (Exception e) {}
        try { adminDashboard.getBtnBanSelectedUser().setVisible(show); } catch (Exception e) {}
        try { adminDashboard.getBtnRefreshData().setVisible(show); } catch (Exception e) {}
    }

    public void refreshCurrentView() {
        switch (currentView) {
            case "OVERVIEW":
                loadOverviewData();
                break;
            case "USERS":
                loadUsersFromDatabase();
                break;
            case "RESTAURANTS":
                loadRestaurantsFromDatabase();
                break;
        }
        JOptionPane.showMessageDialog(adminDashboard, "Content refreshed successfully!", "Refreshed", JOptionPane.INFORMATION_MESSAGE);
    }

    public void loadOverviewData() {
        String sqlCustomers = "SELECT COUNT(*) FROM users WHERE role = 'customer'";
        String sqlRestaurants = "SELECT COUNT(*) FROM users WHERE role = 'restaurant'";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps1 = con.prepareStatement(sqlCustomers);
             ResultSet rs1 = ps1.executeQuery();
             PreparedStatement ps2 = con.prepareStatement(sqlRestaurants);
             ResultSet rs2 = ps2.executeQuery()) {

            int totalCustomers = 0;
            int totalRestaurants = 0;

            if (rs1.next()) totalCustomers = rs1.getInt(1);
            if (rs2.next()) totalRestaurants = rs2.getInt(1);

            try { adminDashboard.getLblValUsers().setText(String.valueOf(totalCustomers)); } catch (Exception e) {}
            try { adminDashboard.getLblValRestaurants().setText(String.valueOf(totalRestaurants)); } catch (Exception e) {}

        } catch (SQLException ex) {
            try { adminDashboard.getLblValUsers().setText("0"); } catch (Exception e) {}
            try { adminDashboard.getLblValRestaurants().setText("0"); } catch (Exception e) {}
        }
    }

    public void loadUsersFromDatabase() {
        String[] columns = {"ID", "Full Name", "Username", "Email", "Password", "Phone", "Status"};
        try {
            adminDashboard.getTableModel().setColumnIdentifiers(columns);
            adminDashboard.getTableModel().setRowCount(0);
        } catch (Exception e) { return; }

        String sql = "SELECT id, full_name, username, email, password, phone, status FROM users WHERE role = 'customer'";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("status");
                if (status == null || status.trim().isEmpty()) status = "Active";
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    status
                };
                adminDashboard.getTableModel().addRow(row);
            }

        } catch (SQLException ex) {
            loadUsersFallback(columns);
        }
    }

    private void loadUsersFallback(String[] columns) {
        String sql = "SELECT id, full_name, username, email, password, phone FROM users WHERE role = 'customer'";
        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    "Active"
                };
                adminDashboard.getTableModel().addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(adminDashboard, "Error loading customers: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadRestaurantsFromDatabase() {
        String[] columns = {"ID", "Restaurant Name", "Username", "Email", "Password", "Phone", "Address", "Status"};
        try {
            adminDashboard.getTableModel().setColumnIdentifiers(columns);
            adminDashboard.getTableModel().setRowCount(0);
        } catch (Exception e) { return; }

        String sql = "SELECT id, full_name, username, email, password, phone, address, status FROM users WHERE role = 'restaurant'";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("status");
                if (status == null || status.trim().isEmpty()) status = "Active";
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    status
                };
                adminDashboard.getTableModel().addRow(row);
            }

        } catch (SQLException ex) {
            loadRestaurantsFallback(columns);
        }
    }

    private void loadRestaurantsFallback(String[] columns) {
        String sql = "SELECT id, full_name, username, email, password, phone, address FROM users WHERE role = 'restaurant'";
        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    "Active"
                };
                adminDashboard.getTableModel().addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(adminDashboard, "Error loading restaurants: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteSelectedRecord() {
        int selectedRow = -1;
        try { selectedRow = adminDashboard.getDataTable().getSelectedRow(); } catch (Exception e) {}

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(adminDashboard, "Please select an account from the table first before deleting!", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = 0;
        try { id = Integer.parseInt(adminDashboard.getTableModel().getValueAt(selectedRow, 0).toString()); } catch (Exception e) { return; }

        int confirm = JOptionPane.showConfirmDialog(adminDashboard, "Are you sure you want to delete account ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            adminDashboard.getTableModel().removeRow(selectedRow);
            JOptionPane.showMessageDialog(adminDashboard, "Record deleted successfully from database.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(adminDashboard, "Error deleting record: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void banSelectedRecord() {
        int selectedRow = -1;
        try { selectedRow = adminDashboard.getDataTable().getSelectedRow(); } catch (Exception e) {}

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(adminDashboard, "Please select an account from the table to ban!", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = 0;
        try { id = Integer.parseInt(adminDashboard.getTableModel().getValueAt(selectedRow, 0).toString()); } catch (Exception e) { return; }

        int confirm = JOptionPane.showConfirmDialog(adminDashboard, "Are you sure you want to ban/deactivate account ID " + id + "?", "Confirm Ban", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "UPDATE users SET status = 'Banned' WHERE id = ?";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(adminDashboard, "Account has been banned successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshCurrentView();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(adminDashboard, "Error banning account: " + ex.getMessage() + "\n(Ensure your database has a 'status' column).", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void searchDatabase(String query) {
        System.out.println("Searching database for: " + query);
    }
}