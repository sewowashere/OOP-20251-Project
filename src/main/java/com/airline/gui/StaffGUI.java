package com.airline.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.airline.models.Seat;
import com.airline.services.*;

public class StaffGUI extends JFrame {
    private JTabbedPane tabbedPane;
    private FlightManager flightManager;
    private SeatManager seatManager;

    public StaffGUI() {
        flightManager = FlightManager.getInstance();
        seatManager = SeatManager.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Airline Staff Portal - Operator Panel");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 100, 0)); // Koyu Yeşil

        JLabel welcomeLabel = new JLabel("  Staff Portal: " + AuthService.getCurrentUser().getUsername(), SwingConstants.LEFT);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> { dispose(); new LoginScreen(); });

        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 18));

        tabbedPane.addTab("Flight Operations", createFlightPanel());
        tabbedPane.addTab("Occupancy Reports", createReportPanel());
        // AZ ÖNCE DIŞARIDA KALAN SATIRI BURAYA ALDIK:
        tabbedPane.addTab("View Seat Status", createSeatViewPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createSeatViewPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JComboBox<Integer> flightCombo = new JComboBox<>();
        flightManager.getAllFlights().forEach(f -> flightCombo.addItem(f.getFlightNum()));

        JPanel seatWrapper = new JPanel(new BorderLayout());
        JButton btnShowSeats = new JButton("Show Seat Map");

        btnShowSeats.addActionListener(e -> {
            if (flightCombo.getSelectedItem() != null) {
                int fNum = (int) flightCombo.getSelectedItem();
                Seat[][] matrix = seatManager.getSeatsForFlight(fNum);
                SeatMapPanel smp = new SeatMapPanel(matrix);
                seatWrapper.removeAll();
                seatWrapper.add(new JScrollPane(smp));
                seatWrapper.revalidate();
                seatWrapper.repaint();
            }
        });

        JPanel top = new JPanel();
        top.add(new JLabel("Select Flight:"));
        top.add(flightCombo);
        top.add(btnShowSeats);

        mainPanel.add(top, BorderLayout.NORTH);
        mainPanel.add(seatWrapper, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createFlightPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columns = {"No", "Dep", "Arr", "Date", "Hour", "Dur"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        refreshFlightData(model);

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

        JButton btnAdd = new JButton("Add Flight");
        btnAdd.setBackground(new Color(0, 128, 0));
        btnAdd.setForeground(Color.WHITE);

        JButton btnDel = new JButton("Delete Selected Flight");
        btnDel.setBackground(new Color(220, 20, 60));
        btnDel.setForeground(Color.WHITE);

        btnAdd.addActionListener(e -> {
            try {
                validateAndSave(txtNum, txtDep, txtArr, txtDate, txtHour, txtDur, false);
                refreshFlightData(model);
                JOptionPane.showMessageDialog(this, "Flight added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int fNum = (int) model.getValueAt(row, 0);
                flightManager.deleteFlightWithIntegrity(fNum);
                refreshFlightData(model);
            }
        });

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.add(btnDel); actionPanel.add(btnAdd);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(actionPanel, BorderLayout.SOUTH);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        return mainPanel;
    }

    private JPanel createReportPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        JTextArea reportArea = new JTextArea("Click to generate report...");
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        reportArea.setEditable(false);

        JLabel statusLabel = new JLabel("Ready");
        JButton generateBtn = new JButton("Generate Report (Threaded)");

        generateBtn.addActionListener(e -> {
            statusLabel.setText("Preparing report...");
            generateBtn.setEnabled(false);
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    StringBuilder sb = new StringBuilder("--- SYSTEM OCCUPANCY REPORT ---\n\n");
                    flightManager.getAllFlights().forEach(f -> {
                        int reserved = seatManager.reservedSeatsCount(seatManager.getSeatsForFlight(f.getFlightNum()));
                        sb.append(String.format("Flight %d: %s -> %s | Rate: %.2f%%\n",
                                f.getFlightNum(), f.getDeparturePlace(), f.getArrivalPlace(), (reserved/180.0)*100));
                    });
                    SwingUtilities.invokeLater(() -> {
                        reportArea.setText(sb.toString());
                        statusLabel.setText("Report Ready.");
                        generateBtn.setEnabled(true);
                    });
                } catch (Exception ex) { ex.printStackTrace(); }
            }).start();
        });

        mainPanel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        mainPanel.add(generateBtn, BorderLayout.SOUTH);
        mainPanel.add(statusLabel, BorderLayout.NORTH);
        return mainPanel;
    }

    private void refreshFlightData(DefaultTableModel m) {
        m.setRowCount(0);
        flightManager.getAllFlights().forEach(f -> m.addRow(new Object[]{
                f.getFlightNum(), f.getDeparturePlace(), f.getArrivalPlace(),
                f.getDate(), f.getHour(), f.getDuration()
        }));
    }

    // --- ÖNEMLİ: VALIDATION VE SÜRE NORMALİZASYONU ---
// StaffGUI.java içindeki metodun güncel hali
    private void validateAndSave(JTextField fNo, JTextField dep, JTextField arr, JTextField dt, JTextField hr, JTextField dur, boolean isEdit) throws Exception {
        if (fNo.getText().isEmpty() || dt.getText().isEmpty()) throw new Exception("Fill all fields!");

        String inputDate = dt.getText();
        if (!inputDate.matches("\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])")) {
            throw new Exception("Invalid Date Format (Use YYYY-MM-DD)!");
        }

        // GEÇMİŞ TARİH KONTROLÜ
        java.time.LocalDate flightDate = java.time.LocalDate.parse(inputDate);
        java.time.LocalDate today = java.time.LocalDate.now();
        if (flightDate.isBefore(today)) {
            throw new Exception("Bro, date cannot be in the past!");
        }

        float h = Float.parseFloat(hr.getText());
        if (h < 0 || h >= 24 || (h % 1) > 0.59) throw new Exception("Invalid Hour!");

        float rawDur = Float.parseFloat(dur.getText());
        int hPart = (int) rawDur;
        int mPart = Math.round((rawDur - hPart) * 100);
        if (mPart >= 60) {
            hPart += mPart / 60;
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
}