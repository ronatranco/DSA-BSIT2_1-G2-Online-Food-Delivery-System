/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import GUI.CustomerDashboard;
import GUI.CustomerDashboardProfile;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author klara
 */
public class Customer implements ActionListener, MouseListener {

    private CustomerDashboard view;
  
    private final String cartOwnerId = Order.OrderStore.getSessionCustomerId();

    private boolean isCartVisible = false;
    private boolean isProfileVisible = false;
    private boolean isTrackOrderVisible = false;
    private boolean isHistoryVisible = false;

    // ANIMATION
    private javax.swing.Timer animationTimer;
    private double preciseWidth = 80.0;
    private int currentWidth = 80;
    private final int MIN_WIDTH = 80;
    private final int MAX_WIDTH = 200;
    private boolean targetsExpand = false;

    // FOR EASING CURVES
    private long animationStartTime;
    private double animationDuration = 280.0;
    private double startWidth;

    public Customer(CustomerDashboard view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // NAME DASHBOARD 
        if (e.getSource() == view.getPnlRightProfile().getBtnSaveInfo()
                || e.getSource() == view.getPnlRightProfile().getTxtFieldName1()
                || e.getSource() == view.getPnlRightProfile().getTxtFullAdd()) {
            view.setSavedFirstName(view.getPnlRightProfile().getTxtFieldName1().getText().trim());
            view.setSavedLastName(view.getPnlRightProfile().getTxtFieldName2().getText().trim());
            view.setSavedMobileNumber(view.getPnlRightProfile().getMobileNumber());
            view.setSavedFullAddress(view.getPnlRightProfile().getAddress());
            CustomerDashboardProfile.setSavedHomeAddress(view.getPnlRightProfile().getAddress());

            String name = view.getPnlRightProfile().getName();
            if (name.isEmpty()) {
                view.getLblName().setText("Hello, User!");
            } else {
                view.getLblName().setText("Hello, " + name + "!");
            }
        }

        // BUTTON CART 
        if (e.getSource() == view.getBtnNavCart()) {
            closeAllSidebarsExcept("cart");
            if (!isCartVisible) {
                view.getPnlRightCart().animateOpen();
                isCartVisible = true;
            } else {
                view.getPnlRightCart().animateClose();
                isCartVisible = false;
            }
        } // BUTTON PROFILE 
        else if (e.getSource() == view.getBtnNavProfile()) {
            closeAllSidebarsExcept("profile");
            if (!isProfileVisible) {
                isProfileVisible = true;
                expandSidebar();
                view.getPnlRightProfile().animateOpen();
            } else {
                isProfileVisible = false;
                collapseSidebar();
                view.getPnlRightProfile().animateClose();
            }
        } // BUTTON TRACK ORDER
        else if (e.getSource() == view.getBtnNavTrack()) {
            closeAllSidebarsExcept("track");
            if (!isTrackOrderVisible) {
                isTrackOrderVisible = true;
                expandSidebar();
                view.getPnlRightTrackOrder().animateOpen();
            } else {
                isTrackOrderVisible = false;
                collapseSidebar();
                view.getPnlRightTrackOrder().animateClose();
            }
        } // BUTTON HISTORY 
        else if (e.getSource() == view.getBtnNavHistory()) {
            closeAllSidebarsExcept("history");
            if (!isHistoryVisible) {
                isHistoryVisible = true;
                expandSidebar();
                view.getPnlRightHistory().animateOpen();
            } else {
                isHistoryVisible = false;
                collapseSidebar();
                view.getPnlRightHistory().animateClose();
            }
        } // SUBPANEL & BUTTON LISTENERS
        else if (e.getSource() == view.getPnlRightCart().getCloseButton()) {
            view.getPnlRightCart().animateClose();
            isCartVisible = false;
        } else if (e.getSource() == view.getPnlRightProfile()) {
            isProfileVisible = false;
            collapseSidebar();
            view.getPnlRightProfile().animateClose();
        } else if (view.getPnlRightHistory() != null && e.getSource() == view.getPnlRightHistory().getCloseButton()) {
            closeAllSidebarsExcept("none");
            view.getTxtSearchBar().setText("What are you craving today?");
            view.getTxtSearchBar().setForeground(new Color(120, 120, 120));
            showAllRestaurants(view.getPnlRestoCard(), true);
        } else if (view.getPnlRightTrackOrder() != null && e.getSource() == view.getPnlRightTrackOrder().getCloseButton()) {
            closeAllSidebarsExcept("none");
            view.getTxtSearchBar().setText("What are you craving today?");
            view.getTxtSearchBar().setForeground(new Color(120, 120, 120));
            showAllRestaurants(view.getPnlRestoCard(), true);
        } // NAVIGATION
        else if (e.getSource() == view.getBtnNavLogoPanda()) {
            System.out.println("Routing: Logo action triggered.");
        } else if (e.getSource() == view.getBtnNavHome()) {
            closeAllSidebarsExcept("none");
            view.getTxtSearchBar().setText("What are you craving today?");
            view.getTxtSearchBar().setForeground(new Color(120, 120, 120));
            
            if (view.getLblPlaceholder() != null) view.getLblPlaceholder().setText("");
            if (view.getPnlExpandedLocation() != null) view.getPnlExpandedLocation().setVisible(false);
            if (view.getBtnExpandLocation() != null) view.getBtnExpandLocation().setText(">");
            if (view.getSlideInTimer() != null) view.getSlideInTimer().start();
            
            showAllRestaurants(view.getPnlRestoCard(), true);
        } else if (e.getSource() == view.getBtnNavExit()) {
            System.out.println("Logging out...");
            saveCart();
            view.getMainFrame().showCard("ROLE_SELECTION");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) {
            return;
        }
        // click on a restaurant card
        for (GUI.CustomerDashboardRestaurant.RestoCard c : view.getPnlRestoCard().getCards()) {
            if (e.getSource() == c.lblName) {
                view.showMenu(c.name);
                closeAllSidebarsExcept("menu");
                break;
            }
        }
        if (e.getSource() == view.getLblHome()) {
            closeAllSidebarsExcept("none");
            view.getTxtSearchBar().setText("What are you craving today?");
            view.getTxtSearchBar().setForeground(new Color(120, 120, 120));
            
            if (view.getLblPlaceholder() != null) view.getLblPlaceholder().setText("");
            if (view.getPnlExpandedLocation() != null) view.getPnlExpandedLocation().setVisible(false);
            if (view.getBtnExpandLocation() != null) view.getBtnExpandLocation().setText(">");
            if (view.getSlideInTimer() != null) view.getSlideInTimer().start();
    
            showAllRestaurants(view.getPnlRestoCard(), true);
        } else if (e.getSource() == view.getLblCart()) {
            closeAllSidebarsExcept("cart");
            if (!isCartVisible) {
                view.getPnlRightCart().animateOpen();
                isCartVisible = true;
            } else {
                view.getPnlRightCart().animateClose();
                isCartVisible = false;
            }
        } else if (e.getSource() == view.getLblProfile()) {
            closeAllSidebarsExcept("profile");
            if (!isProfileVisible) {
                isProfileVisible = true;
                expandSidebar();
                view.getPnlRightProfile().animateOpen();
            } else {
                isProfileVisible = false;
                collapseSidebar();
                view.getPnlRightProfile().animateClose();
            }
        } else if (e.getSource() == view.getLblTrack()) {
            closeAllSidebarsExcept("track");
            if (!isTrackOrderVisible) {
                isTrackOrderVisible = true;
                expandSidebar();
                view.getPnlRightTrackOrder().animateOpen();
            } else {
                isTrackOrderVisible = false;
                collapseSidebar();
                view.getPnlRightTrackOrder().animateClose();
            }
        } else if (e.getSource() == view.getLblHistory()) {
            closeAllSidebarsExcept("history");
            if (!isHistoryVisible) {
                isHistoryVisible = true;
                expandSidebar();
                view.getPnlRightHistory().animateOpen();
            } else {
                isHistoryVisible = false;
                collapseSidebar();
                view.getPnlRightHistory().animateClose();
            }
        } else if (e.getSource() == view.getLblExit()) {
            System.out.println("Logging out...");
            saveCart();
            view.getMainFrame().showCard("ROLE_SELECTION");
        }
        view.getPnlMainBg().repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof JLabel && source != view.getSearchBarContainer()) {
            JLabel label = (JLabel) source;
            if (label == view.getLblHome() || label == view.getLblCart()
                    || label == view.getLblTrack() || label == view.getLblHistory()
                    || label == view.getLblProfile() || label == view.getLblExit()) {
                label.setForeground(new Color(210, 210, 210));
            }
        }
        if (isProfileVisible || isTrackOrderVisible || isHistoryVisible) {
            return;
        }
        if (source == view.getLeftBannerNav()
                || source == view.getBtnNavLogoPanda() || source == view.getBtnNavHome()
                || source == view.getBtnNavCart() || source == view.getBtnNavTrack()
                || source == view.getBtnNavHistory() || source == view.getBtnNavProfile()
                || source == view.getBtnNavExit() || source == view.getLblHome()
                || source == view.getLblCart() || source == view.getLblTrack()
                || source == view.getLblHistory() || source == view.getLblProfile()
                || source == view.getLblExit()) {

            if (!targetsExpand) {
                targetsExpand = true;
                setupAnimationTracking();
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof JLabel) {
            JLabel label = (JLabel) source;
            if (label == view.getLblHome() || label == view.getLblCart()
                    || label == view.getLblTrack() || label == view.getLblHistory()
                    || label == view.getLblProfile() || label == view.getLblExit()) {
                label.setForeground(Color.WHITE);
            }
        }
        if (isProfileVisible || isTrackOrderVisible || isHistoryVisible) {
            return;
        }
        Point mousePoint = e.getPoint();
        if (e.getSource() == view.getLeftBannerNav()) {
            if (!view.getLeftBannerNav().getBounds().contains(mousePoint)) {
                targetsExpand = false;
                setupAnimationTracking();
            }
        }
    }

    private void setupAnimationTracking() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        animationStartTime = System.currentTimeMillis();
        startWidth = preciseWidth;
        startAnimation();
    }

    private void startAnimation() {
        animationTimer = new javax.swing.Timer(4, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - animationStartTime;
                double progress = Math.min(1.0, elapsed / animationDuration);
                double easeOutProgress = 1.0 - Math.pow(1.0 - progress, 3);

                int targetWidth = targetsExpand ? MAX_WIDTH : MIN_WIDTH;
                preciseWidth = startWidth + (targetWidth - startWidth) * easeOutProgress;
                currentWidth = (int) Math.round(preciseWidth);

                if (targetsExpand) {
                    if (progress >= 1.0 || currentWidth >= MAX_WIDTH) {
                        currentWidth = MAX_WIDTH;
                        preciseWidth = MAX_WIDTH;
                        view.setLabelsVisible(true);
                        animationTimer.stop();
                    }
                } else {
                    if (view.getLblHome().isVisible()) {
                        view.setLabelsVisible(false);
                    }
                    if (progress >= 1.0 || currentWidth <= MIN_WIDTH) {
                        currentWidth = MIN_WIDTH;
                        preciseWidth = MIN_WIDTH;
                        animationTimer.stop();
                    }
                }
                applyWidthLayout(currentWidth);
            }
        });
        animationTimer.start();
    }

    private void applyWidthLayout(int width) {
        view.getLeftBannerNav().setBounds(0, 0, width, 720);
        int baseX = width + 30;

        if (view.getLblName() != null) {
            view.getLblName().setBounds(baseX, 34, 420, 35);
        }
        view.getSearchBarContainer().setBounds(baseX, 80, 420, 35);
        view.getPnlRestoCard().topBannerQuote.setBounds(baseX, 135, 865, 120);
        GUI.CustomerDashboardRestaurant restoCard = view.getPnlRestoCard();

        // RESTAURANT SCROLL
        restoCard.scrollPane.setBounds(0, 320, 1024, 400);
        // RESTAURANT LABEL
        restoCard.lblRestaurant.setBounds(baseX, 270, 400, 50);
        if (restoCard.restoLine != null) {
            restoCard.restoLine.setBounds(baseX + 220, 298, 644, 5);
        }
        // RESTAURANT CARDS (built from the database)
        restoCard.layoutCards(baseX);
        view.getPnlMainBg().repaint();
    }

    public void expandSidebar() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        targetsExpand = true;
        currentWidth = MAX_WIDTH;
        preciseWidth = MAX_WIDTH;
        view.setLabelsVisible(true);
        applyWidthLayout(MAX_WIDTH);
    }

    public void collapseSidebar() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        targetsExpand = false;
        currentWidth = MIN_WIDTH;
        preciseWidth = MIN_WIDTH;
        view.setLabelsVisible(false);
        applyWidthLayout(MIN_WIDTH);
    }
    
    

    private void closeAllSidebarsExcept(String exception) {
        if (!exception.equals("cart")) {
            view.closeCheckoutPanels(); 
        }
        boolean isMenuVisible = view.isAnyMenuVisible();

        if (exception.equals("profile") || exception.equals("history") || exception.equals("track") || exception.equals("none")) {
            view.hideAllMenus();
            isMenuVisible = false;
        }
        boolean hideRestaurantView = isMenuVisible;
        if (!exception.equals("cart")) {
            view.getPnlRightCart().animateClose();
            isCartVisible = false;
        }
        if (!exception.equals("profile")) {
            view.getPnlRightProfile().animateClose();
            if (isProfileVisible) {
                isProfileVisible = false;
                collapseSidebar();
            }
        } else {
            hideRestaurantView = true;
        }
        if (!exception.equals("history")) {
            view.getPnlRightHistory().animateClose();
            if (isHistoryVisible) {
                isHistoryVisible = false;
                collapseSidebar();
            }
        } else {
            hideRestaurantView = true;
        }
        if (!exception.equals("track")) {
            view.getPnlRightTrackOrder().animateClose();
            if (isTrackOrderVisible) {
                isTrackOrderVisible = false;
                collapseSidebar();
            }
        } else {
            hideRestaurantView = true;
        }
        if (exception.equals("none")) {
            collapseSidebar();
        }

        if (view.getPnlRestoCard() != null) {
            view.getPnlRestoCard().setVisible(!hideRestaurantView);
            view.getSearchBarContainer().setVisible(!hideRestaurantView);

            if (view.getLblName() != null) {
                view.getLblName().setVisible(!hideRestaurantView);
            }
        }
    }

    public void searchRestaurants(String query) {
        GUI.CustomerDashboardRestaurant restoCard = view.getPnlRestoCard();
        if (query.trim().isEmpty() || query.equals("What are you craving today?")) {
            showAllRestaurants(restoCard, true);
            return;
        }
        restoCard.filterByName(query);
        view.getPnlMainBg().repaint();
    }

    public void filterRestaurantsByLocation(String location) {
        GUI.CustomerDashboardRestaurant restoCard = view.getPnlRestoCard();
        if (restoCard == null) return;
        if (location == null || location.isEmpty()) {
            showAllRestaurants(restoCard, true);
            return;
        }
        restoCard.showOnly(view.getAreaIndex().get(location));
        view.getPnlMainBg().repaint();
    }

    private void showAllRestaurants(GUI.CustomerDashboardRestaurant restoCard, boolean visible) {
        restoCard.showAll(visible);
        view.getPnlMainBg().repaint();
    }

    // CART SAVE / RESTORE 
    public void saveCart() {
        if (view == null || view.getPnlRightCart() == null) return;
        List<String[]> rows = new ArrayList<>();
        JPanel container = view.getPnlRightCart().getCartContainer();
        if (container == null) return;

        for (java.awt.Component comp : container.getComponents()) {
            if (!(comp instanceof JPanel)) continue;
            JPanel card = (JPanel) comp;

            String restaurantName = null;
            for (java.awt.Component headerComp : card.getComponents()) {
                if (headerComp instanceof JPanel) {
                    for (java.awt.Component sub : ((JPanel) headerComp).getComponents()) {
                        if (sub instanceof JCheckBox) {
                            restaurantName = ((JCheckBox) sub).getText();
                        }
                    }
                }
            }
            if (restaurantName == null) continue;

            for (java.awt.Component c : card.getComponents()) {
                if (!(c instanceof JPanel)) continue;
                JPanel row = (JPanel) c;
                if (row.getComponentCount() == 0 || !(row.getComponent(0) instanceof JPanel)) continue;

                JPanel topSubRow = (JPanel) row.getComponent(0);
                String itemName = "";
                int qty = 0;
                double unitPrice = 0.0;

                for (java.awt.Component sub : topSubRow.getComponents()) {
                    if (sub instanceof JLabel) {
                        itemName = ((JLabel) sub).getText();
                    } else if (sub instanceof JPanel) {
                        for (java.awt.Component rc : ((JPanel) sub).getComponents()) {
                            if (rc instanceof JPanel) {
                                for (java.awt.Component q : ((JPanel) rc).getComponents()) {
                                    if (q instanceof JLabel) {
                                        try { qty = Integer.parseInt(((JLabel) q).getText()); }
                                        catch (NumberFormatException ignored) {}
                                    }
                                }
                            } else if (rc instanceof JLabel) {
                                String t = ((JLabel) rc).getText().replace("\u20B1", "").replace(",", "").trim();
                                try {
                                    double line = Double.parseDouble(t);
                                    if (qty > 0) unitPrice = line / qty;
                                } catch (NumberFormatException ignored) {}
                            }
                        }
                    }
                }

                String instructions = "";
                for (java.awt.Component sub : row.getComponents()) {
                    if (sub instanceof JTextField) {
                        String text = ((JTextField) sub).getText().trim();
                        if (!text.isEmpty() && !text.equals("Add special instructions...")) {
                            instructions = text;
                        }
                    }
                }

                if (!itemName.isEmpty() && qty > 0) {
                    rows.add(new String[]{restaurantName, itemName, String.valueOf(qty),
                            String.valueOf(unitPrice), instructions});
                }
            }
        }
        Order.OrderStore.saveCart(cartOwnerId, rows);
    }

    public void restoreCart() {
        if (view == null || view.getPnlRightCart() == null) return;
        JPanel container = view.getPnlRightCart().getCartContainer();
        if (container == null || container.getComponentCount() > 0) return; 

        List<String[]> rows = Order.OrderStore.loadCart(cartOwnerId);
        if (rows.isEmpty()) return;

        for (String[] r : rows) {
            String[][] item = { { r[1], r[2], r[3], r[4] } };
            view.getPnlRightCart().addRestaurantCards(r[0], item);
        }
        view.getPnlRightCart().updateCartTotals();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}