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

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(25, 25, 112));

        JLabel welcomeLabel = new JLabel("  Admin Dashboard", SwingConstants.LEFT);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginScreen();
        });

        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 18));
        tabbedPane.addTab("Flight Management", createFlightPanel());
        tabbedPane.addTab("Concurrency Simulation", createSimulationPanel());
        tabbedPane.addTab("Asynchronous Reports", createReportPanel());
        tabbedPane.addTab("Manage Staff", createStaffManagementPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createFlightPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columns = {"No", "Dep", "Arr", "Date", "Hour", "Dur"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshFlightData(model);

        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Flight Details (Add/Edit)"));

        JTextField txtNum = new JTextField();
        JTextField txtDep = new JTextField();
        JTextField txtArr = new JTextField();
        JTextField txtDate = new JTextField();
        JTextField txtHour = new JTextField();
        JTextField txtDur = new JTextField();

        formPanel.add(new JLabel("Flight No:")); formPanel.add(txtNum);
        formPanel.add(new JLabel("Dep:")); formPanel.add(txtDep);
        formPanel.add(new JLabel("Arr:")); formPanel.add(txtArr);
        formPanel.add(new JLabel("Date (YYYY-MM-DD):")); formPanel.add(txtDate);
        formPanel.add(new JLabel("Hour (0.0-23.59):")); formPanel.add(txtHour);
        formPanel.add(new JLabel("Duration (HH.MM):")); formPanel.add(txtDur);

        // --- BUTTONS ---
        JButton btnAdd = new JButton("Add Flight");
        btnAdd.setBackground(new Color(0, 128, 0)); btnAdd.setForeground(Color.WHITE);

        JButton btnEdit = new JButton("Update Selected");
        btnEdit.setBackground(new Color(0, 112, 192)); btnEdit.setForeground(Color.WHITE);

        JButton btnDel = new JButton("Delete Selected");
        btnDel.setBackground(new Color(220, 20, 60)); btnDel.setForeground(Color.WHITE);

        // --- ADD ACTION ---
        btnAdd.addActionListener(e -> {
            try {
                validateAndSave(txtNum, txtDep, txtArr, txtDate, txtHour, txtDur, false);
                refreshFlightData(model);
                JOptionPane.showMessageDialog(this, "Flight added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // --- EDIT ACTION ---
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a flight to update!");
                return;
            }
            try {
                validateAndSave(txtNum, txtDep, txtArr, txtDate, txtHour, txtDur, true);
                refreshFlightData(model);
                JOptionPane.showMessageDialog(this, "Flight updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Update Error: " + ex.getMessage());
            }
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int fNum = (int)model.getValueAt(row, 0);

                // Önce bağlı rezervasyonları temizle (Veritabanından siler)
                reservationManager.cancelAllReservationsByFlight(String.valueOf(fNum));

                // Sonra uçuşu sil
                flightManager.deleteFlightWithIntegrity(fNum);

                refreshFlightData(model);
                JOptionPane.showMessageDialog(this, "Flight and all related bookings deleted.");
            }
        });

        // Tablodan seçim yapınca kutuları doldur (Edit kolaylığı için)
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtNum.setText(model.getValueAt(row, 0).toString());
                txtDep.setText(model.getValueAt(row, 1).toString());
                txtArr.setText(model.getValueAt(row, 2).toString());
                txtDate.setText(model.getValueAt(row, 3).toString());
                txtHour.setText(model.getValueAt(row, 4).toString());
                txtDur.setText(model.getValueAt(row, 5).toString());
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnDel); buttonPanel.add(btnEdit); buttonPanel.add(btnAdd);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        return mainPanel;
    }

    // --- ÖNEMLİ: VALIDATION VE SÜRE NORMALİZASYONU ---
// AdminGUI.java içindeki metodun güncel hali
    private void validateAndSave(JTextField fNo, JTextField dep, JTextField arr, JTextField dt, JTextField hr, JTextField dur, boolean isEdit) throws Exception {
        if (fNo.getText().isEmpty() || dt.getText().isEmpty()) throw new Exception("Fill all fields!");

        // 1. Tarih Formatı Kontrolü (YYYY-MM-DD)
        String inputDate = dt.getText();
        if (!inputDate.matches("\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])")) {
            throw new Exception("Invalid Date Format (Use YYYY-MM-DD)!");
        }

        // 2. GEÇMİŞ TARİH KONTROLÜ (YENİ)
        java.time.LocalDate flightDate = java.time.LocalDate.parse(inputDate);
        java.time.LocalDate today = java.time.LocalDate.now();
        if (flightDate.isBefore(today)) {
            throw new Exception("Bruh! You cannot add a flight to the past (Before " + today + ")!");
        }

        // 3. Saat Kontrolü (0.00-23.59)
        float h = Float.parseFloat(hr.getText());
        if (h < 0 || h >= 24 || (h % 1) > 0.59) throw new Exception("Invalid Hour (0.00-23.59)!");

        // 4. Süre Normalizasyonu (1.99 -> 2.39 Çözümü)
        float rawDur = Float.parseFloat(dur.getText());
        int hPart = (int) rawDur;
        int mPart = Math.round((rawDur - hPart) * 100);
        if (mPart >= 60) {
            hPart += mPart / 60; // Not: Buradaki değişken adı hPart olmalı, yukarıdakine uyumlu
            mPart = mPart % 60;
        }
        float normalizedDur = hPart + (mPart / 100.0f);

        if (isEdit) {
            flightManager.deleteFlightWithIntegrity(Integer.parseInt(fNo.getText()));
        }

        flightManager.createAndSaveFlight(
                Integer.parseInt(fNo.getText()), dep.getText(), arr.getText(),
                inputDate, h, normalizedDur
        );
    }

    private JPanel createSimulationPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel topControls = new JPanel();
        JCheckBox syncCheck = new JCheckBox("Synchronized Mode");
        JButton runBtn = new JButton("Run 90 Passenger Simulation");
        Seat[][] simMatrix = seatManager.createSeatingArrangement("SIM-FLIGHT", 180);
        SeatMapPanel seatMap = new SeatMapPanel(simMatrix);
        JLabel occupiedLabel = new JLabel("Occupied Seats: 0 / 180");
        runBtn.addActionListener(e -> {
            resetMatrix(simMatrix);
            for (int i = 0; i < 90; i++) {
                new Thread(() -> {
                    reservationManager.reserveRandomSeat(simMatrix, syncCheck.isSelected());
                    SwingUtilities.invokeLater(() -> {
                        seatMap.repaint();
                        occupiedLabel.setText("Occupied Seats: " + countOccupied(simMatrix) + " / 180");
                    });
                }).start();
            }
        });
        topControls.add(syncCheck); topControls.add(runBtn);
        mainPanel.add(topControls, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(seatMap), BorderLayout.CENTER);
        mainPanel.add(occupiedLabel, BorderLayout.SOUTH);
        return mainPanel;
    }

    private JPanel createReportPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JTextArea reportArea = new JTextArea("Click to generate report...");
        JButton generateBtn = new JButton("Generate Report (Threaded)");
        generateBtn.addActionListener(e -> {
            generateBtn.setEnabled(false);
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    StringBuilder sb = new StringBuilder("--- SYSTEM OCCUPANCY REPORT ---\n\n");
                    flightManager.getAllFlights().forEach(f -> {
                        int reserved = seatManager.reservedSeatsCount(seatManager.getSeatsForFlight(f.getFlightNum()));
                        sb.append(String.format("Flight %d: Rate: %.2f%%\n", f.getFlightNum(), (reserved/180.0)*100));
                    });
                    SwingUtilities.invokeLater(() -> { reportArea.setText(sb.toString()); generateBtn.setEnabled(true); });
                } catch (Exception ex) {}
            }).start();
        });
        mainPanel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        mainPanel.add(generateBtn, BorderLayout.SOUTH);
        return mainPanel;
    }

    private JPanel createStaffManagementPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        DefaultTableModel staffModel = new DefaultTableModel(new String[]{"Staff Username", "Role"}, 0);
        JTable staffTable = new JTable(staffModel);
        refreshStaffTable(staffModel);
        JButton btnAdd = new JButton("Add New Staff");
        btnAdd.addActionListener(e -> {
            String user = JOptionPane.showInputDialog(this, "Staff Username:");
            if (user != null && !user.isEmpty()) {
                AuthService.register(user, "1234", "STAFF");
                refreshStaffTable(staffModel);
            }
        });
        mainPanel.add(new JScrollPane(staffTable), BorderLayout.CENTER);
        mainPanel.add(btnAdd, BorderLayout.SOUTH);
        return mainPanel;
    }

    private void refreshFlightData(DefaultTableModel m) {
        m.setRowCount(0);
        flightManager.getAllFlights().forEach(f -> m.addRow(new Object[]{f.getFlightNum(), f.getDeparturePlace(), f.getArrivalPlace(), f.getDate(), f.getHour(), f.getDuration()}));
    }

    private void refreshStaffTable(DefaultTableModel m) {
        m.setRowCount(0);
        new UserDAOImpl().getAll().stream().filter(u -> u.getRole().equalsIgnoreCase("STAFF"))
                .forEach(u -> m.addRow(new Object[]{u.getUsername(), u.getRole()}));
    }

    private void resetMatrix(Seat[][] m) { for (Seat[] r : m) for (Seat s : r) s.setReserved(false); }
    private int countOccupied(Seat[][] m) {
        int c = 0;
        for (Seat[] r : m) for (Seat s : r) if (s.isReserved()) c++;
        return c;
    }
}