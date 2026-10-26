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
        // Resto Menu 1
        if (this.view.getPnlRestoCard() != null && this.view.getPnlRestoCard().getLblRestoName1() != null) {
            this.view.getPnlRestoCard().getLblRestoName1().addMouseListener(this);
        }
        // Resto Menu 2
        if (this.view.getPnlRestoCard() != null && this.view.getPnlRestoCard().getLblRestoName2() != null) {
            this.view.getPnlRestoCard().getLblRestoName2().addMouseListener(this);
        }
        // Resto Menu 3
        if (this.view.getPnlRestoCard() != null && this.view.getPnlRestoCard().getLblRestoName3() != null) {
            this.view.getPnlRestoCard().getLblRestoName3().addMouseListener(this);
        }
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
            closeAllSidebarsExcept("none");
            view.getTxtSearchBar().setText("What are you craving today?");
            view.getTxtSearchBar().setForeground(new Color(120, 120, 120));
            showAllRestaurants(view.getPnlRestoCard(), true);

        } else if (view.getPnlRightTrackOrder() != null && e.getSource() == view.getPnlRightTrackOrder().getCloseButton()) {
            closeAllSidebarsExcept("none");
            view.getTxtSearchBar().setText("What are you craving today?");
            view.getTxtSearchBar().setForeground(new Color(120, 120, 120));
            showAllRestaurants(view.getPnlRestoCard(), true);
        } // --- NAVIGATION ---
        else if (e.getSource() == view.getBtnNavLogoPanda()) {
            System.out.println("Routing: Logo action triggered.");
        } else if (e.getSource() == view.getBtnNavHome()) {
            closeAllSidebarsExcept("none");
            view.getTxtSearchBar().setText("What are you craving today?");
            view.getTxtSearchBar().setForeground(new Color(120, 120, 120));
            showAllRestaurants(view.getPnlRestoCard(), true);
        } else if (e.getSource() == view.getBtnNavExit()) {
            System.out.println("Logging out...");
        }
        view.getPnlMainBg().repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Resto Menu 1
        if (e.getSource() == view.getPnlRestoCard().getLblRestoName1()) {
            view.getPnlMenuMcDollibee().setVisible(true);
            view.getPnlMenuBrainCells().setVisible(false);
            view.getPnlMenuMangPinaasa().setVisible(false);
            closeAllSidebarsExcept("menu"); 
        } 
        // Resto Menu 2
        else if (e.getSource() == view.getPnlRestoCard().getLblRestoName2()) {
            view.getPnlMenuMcDollibee().setVisible(false);
            view.getPnlMenuBrainCells().setVisible(true);
            view.getPnlMenuMangPinaasa().setVisible(false);
            closeAllSidebarsExcept("menu");
        } 
        // Resto Menu 3
        else if (e.getSource() == view.getPnlRestoCard().getLblRestoName3()) {
            view.getPnlMenuMcDollibee().setVisible(false);
            view.getPnlMenuBrainCells().setVisible(false);
            view.getPnlMenuMangPinaasa().setVisible(true);
            closeAllSidebarsExcept("menu");
        }
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
            System.out.println("Logging out...");
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
        }   if (isProfileVisible || isTrackOrderVisible || isHistoryVisible) {
                return;
        }   if (source == view.getLeftBannerNav() 
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
        }   if (isProfileVisible || isTrackOrderVisible || isHistoryVisible) {
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
        }   animationStartTime = System.currentTimeMillis();
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
    }   view.getSearchBarContainer().setBounds(baseX, 80, 420, 35);
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
    restoCard.pnlRestoCard1.setBounds(baseX, 113, 200, 130);
    if (restoCard.lblRestoImage1 != null) {
        restoCard.lblRestoImage1.setBounds(baseX - 14, 3, 230, 190);
    }
    restoCard.pnlRestoCard2.setBounds(baseX + 220, 113, 200, 130);
    if (restoCard.lblRestoImage2 != null) {
        restoCard.lblRestoImage2.setBounds(baseX + 206, 45, 230, 150);
    }
    restoCard.pnlRestoCard3.setBounds(baseX + 440, 113, 200, 130);
    if (restoCard.lblRestoImage3 != null) {
        restoCard.lblRestoImage3.setBounds(baseX + 426, 45, 230, 150);
    }
    restoCard.pnlRestoCard4.setBounds(baseX + 660, 113, 200, 130);
    if (restoCard.lblRestoImage4 != null) {
        restoCard.lblRestoImage4.setBounds(baseX + 646, 45, 230, 150);
    }
    // == ROW 2 RESTAURANT ==
    restoCard.pnlRestoCard5.setBounds(baseX, 330,200, 130);
    if (restoCard.lblRestoImage5 != null) {
        restoCard.lblRestoImage5.setBounds(baseX - 14, 260, 230, 160);
    }
    restoCard.pnlRestoCard6.setBounds(baseX + 220, 330, 200, 130);
    if (restoCard.lblRestoImage6 != null) {
        restoCard.lblRestoImage6.setBounds(baseX + 206, 260, 230, 160);
    }
    restoCard.pnlRestoCard7.setBounds(baseX + 440, 330, 200, 130);
    if (restoCard.lblRestoImage7 != null) {
        restoCard.lblRestoImage7.setBounds(baseX + 426, 260, 230, 160);
    }
    restoCard.pnlRestoCard8.setBounds(baseX + 660,330, 200, 130);
    if (restoCard.lblRestoImage8 != null) {
        restoCard.lblRestoImage8.setBounds(baseX + 646, 260, 230, 160);
    }
    restoCard.scrollContentPanel.setPreferredSize(new Dimension(1000, 605));
    restoCard.scrollContentPanel.revalidate();
    view.getPnlMainBg().repaint();
}

    public void expandSidebar() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }   targetsExpand = true;
            currentWidth = MAX_WIDTH;
            preciseWidth = MAX_WIDTH;
            view.setLabelsVisible(true);
            applyWidthLayout(MAX_WIDTH);
    }

    public void collapseSidebar() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }   targetsExpand = false;
            currentWidth = MIN_WIDTH;
            preciseWidth = MIN_WIDTH;
            view.setLabelsVisible(false);
            applyWidthLayout(MIN_WIDTH);
    }

    private void closeAllSidebarsExcept(String exception) {
        boolean isMenuVisible = (view.getPnlMenuMcDollibee() != null && view.getPnlMenuMcDollibee().isVisible())
                             || (view.getPnlMenuBrainCells() != null && view.getPnlMenuBrainCells().isVisible())
                             || (view.getPnlMenuMangPinaasa() != null && view.getPnlMenuMangPinaasa().isVisible());

        if (exception.equals("profile") || exception.equals("history") || exception.equals("track") || exception.equals("cart") || exception.equals("none")) {
        if (view.getPnlMenuMcDollibee() != null) view.getPnlMenuMcDollibee().setVisible(false);
        if (view.getPnlMenuBrainCells() != null) view.getPnlMenuBrainCells().setVisible(false);
        if (view.getPnlMenuMangPinaasa() != null) view.getPnlMenuMangPinaasa().setVisible(false);
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
        } String lowerQuery = query.toLowerCase().trim();
        // == RESTAURANT 1 ==
        boolean smatch1 = "mcdollibee".contains(lowerQuery);
        if (restoCard.pnlRestoCard1 != null) {
            restoCard.pnlRestoCard1.setVisible(smatch1);
        }
        if (restoCard.lblRestoImage1 != null) {
            restoCard.lblRestoImage1.setVisible(smatch1);
        }
        // == RESTAURANT 2 ==
        boolean smatch2 = "brain cells milk tea".contains(lowerQuery);
        if (restoCard.pnlRestoCard2 != null) {
            restoCard.pnlRestoCard2.setVisible(smatch2);
        }
        if (restoCard.lblRestoImage2 != null) {
            restoCard.lblRestoImage2.setVisible(smatch2);
        } 
        // == RESTAURANT 3 ==
        boolean smatch3 = "mang pinaasa".contains(lowerQuery);
        if (restoCard.pnlRestoCard3 != null) {
            restoCard.pnlRestoCard3.setVisible(smatch3);
        }
        if (restoCard.lblRestoImage3 != null) {
            restoCard.lblRestoImage3.setVisible(smatch3);
        }  
        // == RESTAURANT 4 ==
        boolean smatch4 = "kamote korner".contains(lowerQuery);
        if (restoCard.pnlRestoCard4 != null) {
            restoCard.pnlRestoCard4.setVisible(smatch4);
        }
        if (restoCard.lblRestoImage4 != null) {
            restoCard.lblRestoImage4.setVisible(smatch4);
        }
        // == RESTAURANT 5 ==
        boolean smatch5 = "petsa hut".contains(lowerQuery);
        if (restoCard.pnlRestoCard5 != null) {
            restoCard.pnlRestoCard5.setVisible(smatch5);
        }
        if (restoCard.lblRestoImage5 != null) {
            restoCard.lblRestoImage5.setVisible(smatch5);
        }  
        // == RESTAURANT 6 ==
        boolean smatch6 = "bibang's".contains(lowerQuery);
        if (restoCard.pnlRestoCard6 != null) {
            restoCard.pnlRestoCard6.setVisible(smatch6);
        }
        if (restoCard.lblRestoImage6 != null) {
            restoCard.lblRestoImage6.setVisible(smatch6);
        } 
        // == RESTAURANT 7 ==
        boolean smatch7 = "sinabon".contains(lowerQuery);
        if (restoCard.pnlRestoCard7 != null) {
            restoCard.pnlRestoCard7.setVisible(smatch7);
        }
        if (restoCard.lblRestoImage7 != null) {
            restoCard.lblRestoImage7.setVisible(smatch7);
        }
        // == RESTAURANT 8 ==
        boolean smatch8 = "kuya's fried chicken".contains(lowerQuery);
        if (restoCard.pnlRestoCard8 != null) {
            restoCard.pnlRestoCard8.setVisible(smatch8);
        }
        if (restoCard.lblRestoImage8 != null) {
            restoCard.lblRestoImage8.setVisible(smatch8);
        }  
        restoCard.scrollContentPanel.revalidate();
        view.getPnlMainBg().repaint();
    }

    private void showAllRestaurants(GUI.CustomerDashboardRestaurant restoCard, boolean visible) {
        // == RESTAURANT 1 ==
        if (restoCard.pnlRestoCard1 != null) restoCard.pnlRestoCard1.setVisible(visible);
        if (restoCard.lblRestoImage1 != null) restoCard.lblRestoImage1.setVisible(visible);
        // == RESTAURANT 2 ==
        if (restoCard.pnlRestoCard2 != null) restoCard.pnlRestoCard2.setVisible(visible);
        if (restoCard.lblRestoImage2 != null) restoCard.lblRestoImage2.setVisible(visible);
        // == RESTAURANT 3 ==
        if (restoCard.pnlRestoCard3 != null) restoCard.pnlRestoCard3.setVisible(visible);
        if (restoCard.lblRestoImage3 != null) restoCard.lblRestoImage3.setVisible(visible);
        // == RESTAURANT 4 ==
        if (restoCard.pnlRestoCard4 != null) restoCard.pnlRestoCard4.setVisible(visible);
        if (restoCard.lblRestoImage4 != null) restoCard.lblRestoImage4.setVisible(visible); 
        // == RESTAURANT 5 ==
        if (restoCard.pnlRestoCard5 != null) restoCard.pnlRestoCard5.setVisible(visible);
        if (restoCard.lblRestoImage5 != null) restoCard.lblRestoImage5.setVisible(visible); 
        // == RESTAURANT 6 ==
        if (restoCard.pnlRestoCard6 != null) restoCard.pnlRestoCard6.setVisible(visible);
        if (restoCard.lblRestoImage6 != null) restoCard.lblRestoImage6.setVisible(visible); 
        // == RESTAURANT 7 ==
        if (restoCard.pnlRestoCard7 != null) restoCard.pnlRestoCard7.setVisible(visible);
        if (restoCard.lblRestoImage7 != null) restoCard.lblRestoImage7.setVisible(visible);
        // == RESTAURANT 8 ==
        if (restoCard.pnlRestoCard8 != null) restoCard.pnlRestoCard8.setVisible(visible);
        if (restoCard.lblRestoImage8 != null) restoCard.lblRestoImage8.setVisible(visible); 
    
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
