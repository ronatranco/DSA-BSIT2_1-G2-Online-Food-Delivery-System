/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Order;

import java.awt.*;
import javax.swing.*;
/**
 *
 * @author RJD
 */
public class CustomerOrders extends JFrame{
    
    private Color PURPLE_DARK = new Color(74, 20, 140);
    private Color PURPLE_LIGHT = new Color(156, 39, 176);
    private Color YELLOW_ACCENT = new Color(255, 193, 7);
    private Color BG_LIGHT = new Color(248, 245, 250);
    private Color TEXT_DARK = new Color(30, 30, 30);   
   
    private Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    

    private JLabel hdrCustomerOrders, ord1, ord2;
    private JButton addbtnord1, addbtnord2, minbtnord1, minbtnord2;
    private JComboBox<String> comboord1, comboord2;
    private JTextArea OrdSummary;
    private static final String[] sizes = {"Small", "Medium", "Large"};
    
    
    
    
    CustomerOrders(){
        setTitle("Order Summary");
        setSize(1024, 720);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        
        addbtnord1 = new JButton("Customer Orders:");
        addbtnord1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addbtnord1.setBounds(250, 400, 150, 50);
        addbtnord1.setBackground(YELLOW_ACCENT);
        add(addbtnord1);
        
        
        
   
        
    }
    
}
