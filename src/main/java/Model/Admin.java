/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
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
 * @author rownavanana and gideon
 * CLASS FOR ADMIN BUSINESS LOGIC & BUTTON FUNCTIONALITY
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
            adminDashboard.getBtnNavOverview().addActionListener(this);
            adminDashboard.getBtnNavUsers().addActionListener(this);
            adminDashboard.getBtnNavRestaurants().addActionListener(this);
            adminDashboard.getBtnNavOrders().addActionListener(this);
            adminDashboard.getBtnDeleteSelectedUser().addActionListener(this);
            adminDashboard.getBtnRefreshData().addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (adminDashboard == null) return;

        Object source = e.getSource();

        if (source == adminDashboard.getBtnNavOverview()) {
            currentView = "OVERVIEW";
            adminDashboard.getLblDashboardTitle().setText("System Overview");
            adminDashboard.getCardMetricsContainer().setVisible(true);
            loadOverviewData();
        } 
        else if (source == adminDashboard.getBtnNavUsers()) {
            currentView = "USERS";
            adminDashboard.getLblDashboardTitle().setText("Manage Users Accounts");
            adminDashboard.getCardMetricsContainer().setVisible(false);
            loadUsersFromDatabase();
        } 
        else if (source == adminDashboard.getBtnNavRestaurants()) {
            currentView = "RESTAURANTS";
            adminDashboard.getLblDashboardTitle().setText("Restaurant Accounts");
            adminDashboard.getCardMetricsContainer().setVisible(false);
            loadRestaurantsFromDatabase();
        } 
        else if (source == adminDashboard.getBtnNavOrders()) {
            currentView = "ORDERS";
            adminDashboard.getLblDashboardTitle().setText("Customer Live Orders");
            adminDashboard.getCardMetricsContainer().setVisible(false);
            loadOrdersFromDatabase();
        } 
        else if (source == adminDashboard.getBtnDeleteSelectedUser()) {
            deleteSelectedRecord();
        } 
        else if (source == adminDashboard.getBtnRefreshData()) {
            refreshCurrentView();
        }
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
            case "ORDERS":
                loadOrdersFromDatabase();
                break;
        }
        JOptionPane.showMessageDialog(adminDashboard, "Content refreshed successfully!", "Refreshed", JOptionPane.INFORMATION_MESSAGE);
    }

    public void loadOverviewData() {
        String sqlUsers = "SELECT COUNT(*) FROM users";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps1 = con.prepareStatement(sqlUsers);
             ResultSet rs1 = ps1.executeQuery()) {

            if (rs1.next()) {
                adminDashboard.getLblValUsers().setText(String.valueOf(rs1.getInt(1)));
            }
            adminDashboard.getLblValOrders().setText("3");
            adminDashboard.getLblValRevenue().setText("₱4,250.00");

        } catch (SQLException ex) {
            adminDashboard.getLblValUsers().setText("0");
            adminDashboard.getLblValOrders().setText("0");
            adminDashboard.getLblValRevenue().setText("₱0.00");
        }
    }

    public void loadUsersFromDatabase() {
        String[] columns = {"ID", "Full Name", "Username", "Email", "Role", "Phone"};
        adminDashboard.getTableModel().setColumnIdentifiers(columns);
        adminDashboard.getTableModel().setRowCount(0);

        String sql = "SELECT id, full_name, username, email, role, phone FROM users";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("phone")
                };
                adminDashboard.getTableModel().addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(adminDashboard, "Error loading users: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadRestaurantsFromDatabase() {
        String[] columns = {"ID", "Restaurant Name", "Username", "Email", "Phone", "Address"};
        adminDashboard.getTableModel().setColumnIdentifiers(columns);
        adminDashboard.getTableModel().setRowCount(0);

        String sql = "SELECT id, full_name, username, email, phone, address FROM users WHERE role = 'restaurant'";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
                };
                adminDashboard.getTableModel().addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(adminDashboard, "Error loading restaurants: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadOrdersFromDatabase() {
        String[] columns = {"Order ID", "Customer Name", "Restaurant", "Items", "Total Amount", "Status"};
        adminDashboard.getTableModel().setColumnIdentifiers(columns);
        adminDashboard.getTableModel().setRowCount(0);

        adminDashboard.getTableModel().addRow(new Object[]{101, "customer1", "Panda Bistro", "Blueberry Cheesecake x2", "₱450.00", "Out for Delivery"});
        adminDashboard.getTableModel().addRow(new Object[]{102, "customer2", "Milktea House", "Wintermelon Milk Tea", "₱180.00", "Preparing"});
        adminDashboard.getTableModel().addRow(new Object[]{103, "customer3", "Burger Joint", "Cheeseburger Meal", "₱250.00", "Delivered"});
    }

    public void deleteSelectedRecord() {
        int selectedRow = adminDashboard.getDataTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(adminDashboard, "Please select an item from the table first before deleting!", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentView.equals("ORDERS")) {
            adminDashboard.getTableModel().removeRow(selectedRow);
            JOptionPane.showMessageDialog(adminDashboard, "Live order removed from view.", "Success", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = Integer.parseInt(adminDashboard.getTableModel().getValueAt(selectedRow, 0).toString());

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
}