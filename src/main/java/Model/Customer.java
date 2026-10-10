package Model;

import GUI.CustomerDashboard;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

/**
 *
 * @author klara
 */
public class Customer implements ActionListener, MouseListener {

    private CustomerDashboard view;

    private boolean isCartVisible = false;
    private boolean isProfileVisible = false;
    private boolean isTrackOrderVisible = false;
    private boolean isHistoryVisible = false;

    // Animation
    private javax.swing.Timer animationTimer;
    private double preciseWidth = 80.0;
    private int currentWidth = 80;
    private final int MIN_WIDTH = 80;
    private final int MAX_WIDTH = 200;
    private boolean targetsExpand = false;

    // For easing curves
    private long animationStartTime;
    private double animationDuration = 280.0;
    private double startWidth;

    public Customer(CustomerDashboard view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // --- BUTTON CART ---
        if (e.getSource() == view.getBtnNavCart()) {
            closeAllSidebarsExcept("cart");
            if (!isCartVisible) {
                view.getPnlRightCart().animateOpen();
                isCartVisible = true;
            } else {
                view.getPnlRightCart().animateClose();
                isCartVisible = false;
            }
        } // --- BUTTON PROFILE ---
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
        } // --- BUTTON TRACK ORDER ---
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
        } // --- BUTTON HISTORY ---
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
        } // --- SUBPANEL & BUTTON LISTENERS ---
        else if (e.getSource() == view.getPnlRightCart().getCloseButton()) {
            view.getPnlRightCart().animateClose();
            isCartVisible = false;
        } else if (e.getSource() == view.getPnlRightProfile()) {
            isProfileVisible = false;
            collapseSidebar();
            view.getPnlRightProfile().animateClose();
        } else if (view.getPnlRightHistory() != null && e.getSource() == view.getPnlRightHistory().getCloseButton()) {
            System.out.println("Routing: History close triggered. Returning to home.");
            closeAllSidebarsExcept("none");
            view.getTxtSearchBar().setText("What are you craving today?");
            view.getTxtSearchBar().setForeground(new Color(120, 120, 120));
            showAllRestaurants(view.getPnlRestoCard(), true);

        } else if (view.getPnlRightTrackOrder() != null && e.getSource() == view.getPnlRightTrackOrder().getCloseButton()) {
            System.out.println("Routing: Track Order close triggered. Returning to home.");
            closeAllSidebarsExcept("none");
            view.getTxtSearchBar().setText("What are you craving today?");
            view.getTxtSearchBar().setForeground(new Color(120, 120, 120));
            showAllRestaurants(view.getPnlRestoCard(), true);
        } // --- NAVIGATION ---
        else if (e.getSource() == view.getBtnNavLogoPanda()) {
            System.out.println("Routing: Logo action triggered.");
        } else if (e.getSource() == view.getBtnNavHome()) {
            System.out.println("Routing: Home tab requested. Clearing active sidebars.");
            closeAllSidebarsExcept("none");
            view.getTxtSearchBar().setText("What are you craving today?");
            view.getTxtSearchBar().setForeground(new Color(120, 120, 120));
            showAllRestaurants(view.getPnlRestoCard(), true);
        } else if (e.getSource() == view.getBtnNavExit()) {
            System.out.println("Logging out via Button...");
            System.exit(0);
        }
        view.getPnlMainBg().repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == view.getLblHome()) {
            closeAllSidebarsExcept("none");
            view.getTxtSearchBar().setText("What are you craving today?");
            view.getTxtSearchBar().setForeground(new Color(120, 120, 120));
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
            System.exit(0);
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
        if (!targetsExpand) {
            targetsExpand = true;
            setupAnimationTracking();
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
    // == RESTAURANT SCROLL ==
    restoCard.scrollPane.setBounds(0, 320, 1024, 400);
    // == RESTAURANT LABEL ==
    restoCard.lblRestaurant.setBounds(baseX, 270, 400, 50);
    if (restoCard.restoLine != null) {
        restoCard.restoLine.setBounds(baseX + 220, 298, 644, 5); 
    }
    
    // == ROW 1 RESTAURANT ==
    restoCard.pnlRestoCard1.setBounds(baseX, 110, 200, 130);
    if (restoCard.lblRestoImage1 != null) {
        restoCard.lblRestoImage1.setBounds(45, 10, 110, 110);
    }
    restoCard.pnlRestoCard2.setBounds(baseX + 220, 110, 200, 130);
    if (restoCard.lblRestoImage2 != null) {
        restoCard.lblRestoImage2.setBounds(45, 10, 110, 110);
    }
    restoCard.pnlRestoCard3.setBounds(baseX + 440, 110, 200, 130);
    if (restoCard.lblRestoImage3 != null) {
        restoCard.lblRestoImage3.setBounds(45, 10, 110, 110);
    }
    restoCard.pnlRestoCard4.setBounds(baseX + 660, 110, 200, 130);

    // == ROW 2 RESTAURANT ==
    restoCard.pnlRestoCard5.setBounds(baseX, 330,200, 130);
    restoCard.pnlRestoCard6.setBounds(baseX + 220, 330, 200, 130);
    restoCard.pnlRestoCard7.setBounds(baseX + 440, 330, 200, 130);
    restoCard.pnlRestoCard8.setBounds(baseX + 660,330, 200, 130);
    
    restoCard.scrollContentPanel.setPreferredSize(new Dimension(1000, 605));
    restoCard.scrollContentPanel.revalidate();
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
        boolean hideRestaurantView = false;
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
            hideRestaurantView = true; // Full screen panel -> hide background
        }
        if (!exception.equals("history")) {
            view.getPnlRightHistory().animateClose();
            if (isHistoryVisible) {
                isHistoryVisible = false;
                collapseSidebar();
            }
        } else {
            hideRestaurantView = true; // Full screen panel -> hide background
        }
        if (!exception.equals("track")) {
            view.getPnlRightTrackOrder().animateClose();
            if (isTrackOrderVisible) {
                isTrackOrderVisible = false;
                collapseSidebar();
            }
        } else {
            hideRestaurantView = true; // Full screen panel -> hide background
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
        String lowerQuery = query.toLowerCase().trim();
        boolean matches1 = "mcdollibee".contains(lowerQuery);
        if (restoCard.pnlRestoCard1 != null) {
            restoCard.pnlRestoCard1.setVisible(matches1);
        }
        if (restoCard.lblRestoImage1 != null) {
            restoCard.lblRestoImage1.setVisible(matches1);
        }
        boolean matches2 = "brain cells milk tea".contains(lowerQuery);
        if (restoCard.pnlRestoCard2 != null) {
            restoCard.pnlRestoCard2.setVisible(matches2);
        }
        if (restoCard.lblRestoImage2 != null) {
            restoCard.lblRestoImage2.setVisible(matches2);
        }
        boolean matches3 = "mang pinaasa".contains(lowerQuery);
        if (restoCard.pnlRestoCard3 != null) {
            restoCard.pnlRestoCard3.setVisible(matches3);
        }
        if (restoCard.lblRestoImage3 != null) {
            restoCard.lblRestoImage3.setVisible(matches3);
        }
        boolean matches4 = "kamote korner".contains(lowerQuery);
        if (restoCard.pnlRestoCard4 != null) {
            restoCard.pnlRestoCard4.setVisible(matches4);
        }
        boolean matches5 = "petsa hut".contains(lowerQuery);
        if (restoCard.pnlRestoCard5 != null) {
            restoCard.pnlRestoCard5.setVisible(matches5);
        }
        boolean matches6 = "bibang's".contains(lowerQuery);
        if (restoCard.pnlRestoCard6 != null) {
            restoCard.pnlRestoCard6.setVisible(matches6);
        }
        boolean matches7 = "sinabon".contains(lowerQuery);
        if (restoCard.pnlRestoCard7 != null) {
            restoCard.pnlRestoCard7.setVisible(matches7);
        }
        boolean matches8 = "kuya's fried chicken".contains(lowerQuery);
        if (restoCard.pnlRestoCard8 != null) {
            restoCard.pnlRestoCard8.setVisible(matches8);
        }
        restoCard.scrollContentPanel.revalidate();
        view.getPnlMainBg().repaint();
    }

    private void showAllRestaurants(GUI.CustomerDashboardRestaurant restoCard, boolean visible) {
        if (restoCard.pnlRestoCard1 != null) {
            restoCard.pnlRestoCard1.setVisible(visible);
        }
        if (restoCard.lblRestoImage1 != null) {
            restoCard.lblRestoImage1.setVisible(visible);
        }
        if (restoCard.pnlRestoCard2 != null) {
            restoCard.pnlRestoCard2.setVisible(visible);
        }
        if (restoCard.lblRestoImage2 != null) {
            restoCard.lblRestoImage2.setVisible(visible);
        }
        if (restoCard.pnlRestoCard3 != null) {
            restoCard.pnlRestoCard3.setVisible(visible);
        }
        if (restoCard.lblRestoImage3 != null) {
            restoCard.lblRestoImage3.setVisible(visible);
        }
        if (restoCard.pnlRestoCard4 != null) {
            restoCard.pnlRestoCard4.setVisible(visible);
        }
        if (restoCard.pnlRestoCard5 != null) {
            restoCard.pnlRestoCard5.setVisible(visible);
        }
        if (restoCard.pnlRestoCard6 != null) {
            restoCard.pnlRestoCard6.setVisible(visible);
        }
        if (restoCard.pnlRestoCard7 != null) {
            restoCard.pnlRestoCard7.setVisible(visible);
        }
        if (restoCard.pnlRestoCard8 != null) {
            restoCard.pnlRestoCard8.setVisible(visible);
        }
        restoCard.scrollContentPanel.revalidate();
        view.getPnlMainBg().repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
