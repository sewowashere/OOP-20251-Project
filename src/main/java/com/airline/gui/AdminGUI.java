package com.airline.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.airline.models.*;
import com.airline.services.*;
import com.airline.persistence.UserDAOImpl;

public class AdminGUI extends JFrame {
    private JTabbedPane tabbedPane;
    private FlightManager flightManager;
    private ReservationManager reservationManager;
    private SeatManager seatManager;

    public AdminGUI() {
        // Manager'ları Singleton üzerinden alıyoruz
        flightManager = FlightManager.getInstance();
        reservationManager = ReservationManager.getInstance();
        seatManager = SeatManager.getInstance();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Admin Control Panel - Airline System");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(25, 25, 112)); // Midnight Blue

        JLabel welcomeLabel = new JLabel("  Admin Dashboard", SwingConstants.LEFT);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFocusable(false);
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginScreen();
        });

        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Tabbed Layout
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 18));

        tabbedPane.addTab("Flight Management", createFlightPanel());
        tabbedPane.addTab("Concurrency Simulation", createSimulationPanel());
        tabbedPane.addTab("Asynchronous Reports", createReportPanel());
        tabbedPane.addTab("Manage Staff", createStaffManagementPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    // SCENARIO 1: Concurrency Simulation
    private JPanel createSimulationPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel topControls = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topControls.setOpaque(false);

        JCheckBox syncCheck = new JCheckBox("Synchronized Mode (Thread Safe)");
        syncCheck.setFont(new Font("Arial", Font.BOLD, 18));

        JButton runBtn = new JButton("Run 90 Passenger Simulation");
        runBtn.setBackground(new Color(25, 25, 112));
        runBtn.setForeground(Color.WHITE);

        topControls.add(syncCheck);
        topControls.add(runBtn);

        Seat[][] simMatrix = seatManager.createSeatingArrangement("SIM-FLIGHT", 180);
        SeatMapPanel seatMap = new SeatMapPanel(simMatrix);

        JLabel occupiedLabel = new JLabel("Occupied Seats: 0 / 180");
        occupiedLabel.setFont(new Font("Arial", Font.BOLD, 22));
        occupiedLabel.setHorizontalAlignment(SwingConstants.CENTER);

        runBtn.addActionListener(e -> {
            resetMatrix(simMatrix);
            seatMap.repaint();
            occupiedLabel.setText("Occupied Seats: 0 / 180");

            boolean isSync = syncCheck.isSelected();

            // 90 Passenger Threads
            for (int i = 0; i < 90; i++) {
                new Thread(() -> {
                    reservationManager.reserveRandomSeat(simMatrix, isSync);
                    SwingUtilities.invokeLater(() -> {
                        seatMap.repaint();
                        occupiedLabel.setText("Occupied Seats: " + countOccupied(simMatrix) + " / 180");
                    });
                }).start();
            }
        });

        mainPanel.add(topControls, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(seatMap), BorderLayout.CENTER);
        mainPanel.add(occupiedLabel, BorderLayout.SOUTH);

        return mainPanel;
    }

    // SCENARIO 2: Asynchronous Report Generator
    private JPanel createReportPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JTextArea reportArea = new JTextArea("Click to generate occupancy report...");
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        reportArea.setEditable(false);

        JLabel statusLabel = new JLabel("Ready");
        JButton generateBtn = new JButton("Generate Report (Threaded)");

        generateBtn.addActionListener(e -> {
            statusLabel.setText("Preparing report...");
            generateBtn.setEnabled(false);
            reportArea.setText("");

            new Thread(() -> {
                try {
                    Thread.sleep(2000); // Gecikme simülasyonu
                    StringBuilder sb = new StringBuilder("--- SYSTEM OCCUPANCY REPORT ---\n\n");

                    flightManager.getAllFlights().forEach(f -> {
                        int reserved = seatManager.reservedSeatsCount(seatManager.getSeatsForFlight(f.getFlightNum()));
                        sb.append(String.format("Flight %d: %s -> %s | Rate: %.2f%%\n",
                                f.getFlightNum(), f.getDeparturePlace(), f.getArrivalPlace(), (reserved/180.0)*100));
                    });

                    SwingUtilities.invokeLater(() -> {
                        reportArea.setText(sb.toString());
                        statusLabel.setText("Report Generated Successfully.");
                        generateBtn.setEnabled(true);
                    });
                } catch (InterruptedException ex) { ex.printStackTrace(); }
            }).start();
        });

        mainPanel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        mainPanel.add(generateBtn, BorderLayout.SOUTH);
        mainPanel.add(statusLabel, BorderLayout.NORTH);

        return mainPanel;
    }

    // SCENARIO 3: Manage Staff Information
    private JPanel createStaffManagementPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultTableModel staffModel = new DefaultTableModel(new String[]{"Staff Username", "Role"}, 0);
        JTable staffTable = new JTable(staffModel);
        refreshStaffTable(staffModel);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add New Staff");
        JButton btnDel = new JButton("Remove Selected");
        btnDel.setBackground(Color.RED);
        btnDel.setForeground(Color.WHITE);

        btnAdd.addActionListener(e -> {
            String user = JOptionPane.showInputDialog(this, "Staff Username:");
            if (user != null && !user.isEmpty()) {
                AuthService.register(user, "1234", "STAFF");
                refreshStaffTable(staffModel);
            }
        });

        btnDel.addActionListener(e -> {
            int row = staffTable.getSelectedRow();
            if (row != -1) {
                String user = (String) staffModel.getValueAt(row, 0);
                new UserDAOImpl().deleteByUsername(user);
                refreshStaffTable(staffModel);
            }
        });

        btnPanel.add(btnAdd); btnPanel.add(btnDel);
        mainPanel.add(new JScrollPane(staffTable), BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    // Flight Management Tab
    private JPanel createFlightPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Tablo Modeli ve JTable (Üst Kısım)
        String[] columns = {"No", "Dep", "Arr", "Date", "Hour", "Dur"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        refreshFlightData(model);

        // 2. Form Paneli (Giriş Kutuları - Orta Kısım)
        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Flight Details"));

        JTextField txtNum = new JTextField();
        JTextField txtDep = new JTextField();
        JTextField txtArr = new JTextField();
        JTextField txtDate = new JTextField();
        JTextField txtHour = new JTextField();
        JTextField txtDur = new JTextField();

        formPanel.add(new JLabel("Flight No:")); formPanel.add(txtNum);
        formPanel.add(new JLabel("Departure:")); formPanel.add(txtDep);
        formPanel.add(new JLabel("Arrival:")); formPanel.add(txtArr);
        formPanel.add(new JLabel("Date (YYYY-MM-DD):")); formPanel.add(txtDate);
        formPanel.add(new JLabel("Hour:")); formPanel.add(txtHour);
        formPanel.add(new JLabel("Duration:")); formPanel.add(txtDur);

        // 3. Butonlar Paneli (Alt Kısım)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton btnAdd = new JButton("Add Flight");
        btnAdd.setBackground(new Color(0, 128, 0)); // Yeşil
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnDel = new JButton("Delete Selected Flight");
        btnDel.setBackground(new Color(220, 20, 60)); // Kırmızı
        btnDel.setForeground(Color.WHITE);
        btnDel.setFont(new Font("Arial", Font.BOLD, 14));

        // --- LISTENERS ---

        // Ekleme Butonu Aksiyonu
        btnAdd.addActionListener(e -> {
            try {
                flightManager.createAndSaveFlight(
                        Integer.parseInt(txtNum.getText()),
                        txtDep.getText(),
                        txtArr.getText(),
                        txtDate.getText(),
                        Float.parseFloat(txtHour.getText()),
                        Float.parseFloat(txtDur.getText())
                );
                refreshFlightData(model); // Tabloyu yenile
                JOptionPane.showMessageDialog(this, "Flight added successfully!");
                // Kutuları temizle
                txtNum.setText(""); txtDep.setText(""); txtArr.setText("");
                txtDate.setText(""); txtHour.setText(""); txtDur.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input Error: " + ex.getMessage());
            }
        });

        // Silme Butonu Aksiyonu
        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int fNum = (int) model.getValueAt(row, 0);
                flightManager.deleteFlightWithIntegrity(fNum);
                refreshFlightData(model);
                JOptionPane.showMessageDialog(this, "Flight and related data deleted.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a flight to delete.");
            }
        });

        buttonPanel.add(btnDel);
        buttonPanel.add(btnAdd);

        // Panelleri Birleştirme
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    // Helper Methods
    private void refreshFlightData(DefaultTableModel m) {
        m.setRowCount(0);
        flightManager.getAllFlights().forEach(f -> m.addRow(new Object[]{f.getFlightNum(), f.getDeparturePlace(), f.getArrivalPlace(), f.getDate(), f.getHour(), f.getDuration()}));
    }

    private void refreshStaffTable(DefaultTableModel m) {
        m.setRowCount(0);
        new UserDAOImpl().getAll().stream()
                .filter(u -> u.getRole().equalsIgnoreCase("STAFF"))
                .forEach(u -> m.addRow(new Object[]{u.getUsername(), u.getRole()}));
    }

    private void resetMatrix(Seat[][] m) { for (Seat[] r : m) for (Seat s : r) s.setReserved(false); }
    private int countOccupied(Seat[][] m) {
        int c = 0;
        for (Seat[] r : m) for (Seat s : r) if (s.isReserved()) c++;
        return c;
    }
}