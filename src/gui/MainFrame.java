package gui;

 //
 // Ana sayfa
 //
 // Endpointler:
 // -Login screen
 // -Flight booking screen
 // Buralara yönlendirmesi olacak.

import javax.swing.*;
import java.awt.CardLayout;

public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    public MainFrame() {
        // Ekranları (Panelleri) oluşturuyoruz
        LoginPanel loginPanel = new LoginPanel(this); // 'this' gönderiyoruz ki geçiş yapabilsin
        //AdminPanel adminPanel = new AdminPanel(this);
        //FlightBookingPanel bookingPanel = new FlightBookingPanel(this);

        // Panelleri ana panele birer isimle ekliyoruz
        mainPanel.add(loginPanel, "LOGIN");
        //mainPanel.add(adminPanel, "ADMIN");
        //mainPanel.add(bookingPanel, "BOOKING");

        this.add(mainPanel);
        this.setSize(800, 600);
        this.setVisible(true);
    }

    // Ekranlar arası geçiş yapmak için metod
    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }


    public static void main(String[] args) {
        // Swing arayüzü her zaman Event Dispatch Thread içinde başlatılmalıdır
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
