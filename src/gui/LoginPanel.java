package gui;

//
// Kullanıcı / Personel girişi [cite: 43]
//

import javax.swing.*;

public class LoginPanel extends JPanel {
    public LoginPanel(MainFrame frame) {
        JButton loginBtn = new JButton("Giriş Yap");

        loginBtn.addActionListener(e -> {
            // Eğer giriş başarılıysa ve adminse:
            frame.showScreen("ADMIN");
        });

        this.add(loginBtn);
    }
}
