package com.airline.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.airline.models.*;
import com.airline.services.*;
import com.airline.services.pricing.CalculatePrice;
import com.airline.persistence.ReservationDAOImpl;

public class PassengerGUI extends JFrame {
    private JTabbedPane tabbedPane;
    private FlightManager flightManager;
    private ReservationManager reservationManager;
    private SeatManager seatManager;

    private SeatMapPanel seatMapPanel;
    private JPanel seatMapWrapper;
    private Flight selectedFlight;

    public PassengerGUI() {
        flightManager = FlightManager.getInstance();
        reservationManager = ReservationManager.getInstance();
        seatManager = SeatManager.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Passenger Portal - Airline Reservation");
        setSize(1500, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 112, 192));
        JLabel userLabel = new JLabel("  Welcome, " + AuthService.getCurrentUser().getUsername(), SwingConstants.LEFT);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginScreen();
        });

        header.add(userLabel, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 18));
        tabbedPane.addTab("Search & Book Flight", createBookingPanel());
        tabbedPane.addTab("My Reservations", createMyReservationsPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createBookingPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setPreferredSize(new Dimension(500, 0));
        leftPanel.setOpaque(false);

        JPanel searchBox = new JPanel(new GridLayout(2, 2, 5, 5));
        searchBox.setOpaque(false);

        String[] cities = flightManager.getUniqueCities();
        JComboBox<String> comboFrom = new JComboBox<>(cities.length > 0 ? cities : new String[]{"No Cities"});
        JComboBox<String> comboTo = new JComboBox<>(cities.length > 0 ? cities : new String[]{"No Cities"});

        searchBox.add(new JLabel("Departure City:")); searchBox.add(comboFrom);
        searchBox.add(new JLabel("Arrival City:"));   searchBox.add(comboTo);

        DefaultTableModel flightModel = new DefaultTableModel(new String[]{"Flight No", "From", "To", "Date"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable flightTable = new JTable(flightModel);
        JButton btnSearch = new JButton("Search Flights");

        leftPanel.add(searchBox, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(flightTable), BorderLayout.CENTER);
        leftPanel.add(btnSearch, BorderLayout.SOUTH);

        seatMapWrapper = new JPanel(new BorderLayout());
        seatMapWrapper.setBackground(Color.WHITE);
        seatMapWrapper.setBorder(BorderFactory.createTitledBorder("Step 2: Select Your Seat"));
        JLabel placeholder = new JLabel("Select a flight to view seats.", SwingConstants.CENTER);
        seatMapWrapper.add(placeholder);

        JButton btnConfirm = new JButton("Book Selected Seat");
        btnConfirm.setFont(new Font("Arial", Font.BOLD, 22));
        btnConfirm.setEnabled(false);

        // --- LISTENERS ---

        btnSearch.addActionListener(e -> {
            List<Flight> results = flightManager.getAvailableFlights(
                    (String)comboFrom.getSelectedItem(),
                    (String)comboTo.getSelectedItem()
            );
            flightModel.setRowCount(0);
            results.forEach(f -> flightModel.addRow(new Object[]{f.getFlightNum(), f.getDeparturePlace(), f.getArrivalPlace(), f.getDate()}));
            if(results.isEmpty()) JOptionPane.showMessageDialog(this, "No flights found.");
        });

        flightTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && flightTable.getSelectedRow() != -1) {
                int fNum = (int) flightModel.getValueAt(flightTable.getSelectedRow(), 0);
                selectedFlight = flightManager.findFlight(fNum);
                Seat[][] matrix = seatManager.getSeatsForFlight(fNum);
                seatMapPanel = new SeatMapPanel(matrix);

                seatMapPanel.setSeatSelectionListener(selectedSeat -> {
                    CalculatePrice pc = new CalculatePrice();
                    double finalPrice = pc.getFinalPrice(selectedSeat.getPrice(), selectedSeat.getSeatClass());
                    btnConfirm.setText("Book Seat " + selectedSeat.getSeatNum() + " - Price: " + finalPrice + " TL");
                    btnConfirm.setEnabled(true);
                });

                seatMapWrapper.removeAll();
                seatMapWrapper.add(new JScrollPane(seatMapPanel));
                seatMapWrapper.revalidate();
                seatMapWrapper.repaint();
                btnConfirm.setText("Select a Seat to See Price");
                btnConfirm.setEnabled(false);
            }
        });

        // --- HAVALI ONAY VE BAGAJ EKRANI ---
        btnConfirm.addActionListener(e -> {
            Seat selected = seatMapPanel.getSelectedSeat();
            if (selected == null) return;

            CalculatePrice cp = new CalculatePrice();
            double ticketPrice = cp.getFinalPrice(selected.getPrice(), selected.getSeatClass());
            double allowance = cp.getAllowance(selected.getSeatClass());

            // 1. Bagaj Girişi
            String weightStr = JOptionPane.showInputDialog(this,
                    "Baggage Allowance for " + selected.getSeatClass() + " is " + allowance + "kg.\n" +
                            "Enter your baggage weight (kg):", "15");

            if (weightStr == null) return;

            double weight;
            try {
                weight = Double.parseDouble(weightStr);
            } catch (NumberFormatException ex) {
                weight = 15.0;
            }

            // 2. Ekstra Ücret Hesaplama
            double baggageFee = cp.calculateBaggageSurcharge(weight, selected.getSeatClass());
            double total = ticketPrice + baggageFee;

            // 3. Özet Onay Ekranı
            String summary = String.format(
                    "--- BOOKING SUMMARY ---\n" +
                            "Flight: %d (%s -> %s)\n" +
                            "Seat: %s (%s)\n" +
                            "Ticket: %.2f TL\n" +
                            "Baggage: %.1f kg (Limit: %.1f kg)\n" +
                            "Baggage Fee: %.2f TL\n" +
                            "---------------------------\n" +
                            "TOTAL: %.2f TL\n\n" +
                            "Confirm this reservation?",
                    selectedFlight.getFlightNum(), selectedFlight.getDeparturePlace(), selectedFlight.getArrivalPlace(),
                    selected.getSeatNum(), selected.getSeatClass(), ticketPrice,
                    weight, allowance, baggageFee, total
            );

            int confirm = JOptionPane.showConfirmDialog(this, summary, "Final Confirmation",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                reservationManager.createReservation(
                        String.valueOf(selectedFlight.getFlightNum()),
                        AuthService.getCurrentUser().getUsername(),
                        selected.getSeatNum(),
                        weight
                );

                JOptionPane.showMessageDialog(this, "Booking Successful!");
                seatMapPanel.repaint();
                btnConfirm.setEnabled(false);
            }
        });

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(seatMapWrapper, BorderLayout.CENTER);
        mainPanel.add(btnConfirm, BorderLayout.SOUTH);
        return mainPanel;
    }

    private JPanel createMyReservationsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultTableModel model = new DefaultTableModel(new String[]{"PNR Code", "Flight No", "Seat No", "Date"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // --- BUTONLAR PANELİ ---
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);

        // 1. DETAY GÖRÜNTÜLEME BUTONU (YENİ)
        JButton btnView = new JButton("View Ticket Details");
        btnView.setBackground(new Color(0, 112, 192));
        btnView.setForeground(Color.WHITE);
        btnView.setFont(new Font("Arial", Font.BOLD, 14));

        // 2. İPTAL ETME BUTONU
        JButton btnCancel = new JButton("Cancel Selected Reservation");
        btnCancel.setBackground(new Color(220, 20, 60));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));

        // --- BUTON AKSİYONLARI ---

        // View Ticket Aksiyonu
        btnView.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String pnrCode = (String) model.getValueAt(row, 0);
                // DAO üzerinden rezervasyonu buluyoruz
                Reservation res = new ReservationDAOImpl().findById(pnrCode);
                if (res != null) {
                    showReservationDetails(res);
                } else {
                    JOptionPane.showMessageDialog(this, "Could not load ticket details.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a reservation from the table first!");
            }
        });

        // Cancel Aksiyonu
        btnCancel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String pnrCode = (String) model.getValueAt(row, 0);
                if (reservationManager.cancelReservation(pnrCode)) {
                    JOptionPane.showMessageDialog(this, "Cancelled: " + pnrCode);
                    refreshMyReservations(model);
                }
            }
        });

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) refreshMyReservations(model);
        });

        buttonPanel.add(btnView);
        buttonPanel.add(btnCancel);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshMyReservations(model);
        return mainPanel;
    }

    private void refreshMyReservations(DefaultTableModel model) {
        model.setRowCount(0);
        User current = AuthService.getCurrentUser();
        if (current == null) return;

        String currentUserId = current.getUsername();
        List<Reservation> myRes = new ReservationDAOImpl().getReservationsByPassenger(currentUserId);
        for (Reservation r : myRes) {
            model.addRow(new Object[]{r.getReservationCode(), r.getFlightNum(), r.getSeatNum(), r.getDateOfReservation()});
        }
    }

    private void showReservationDetails(Reservation res) {
        // Uçuş bilgilerini Route nesnesi üzerinden çekiyoruz
        Flight f = flightManager.findFlight(Integer.parseInt(res.getFlightNum()));

        // Reservation içindeki tüm verileri topluyoruz
        String pnr = res.getReservationCode();
        String passenger = res.getPassengerID();
        String seat = res.getSeatNum();
        String flightInfo = f.getDeparturePlace() + " to " + f.getArrivalPlace();
        String dateInfo = f.getDate() + " at " + f.getHour();

        // Ticket ve Baggage bilgilerini güvenli çekiyoruz
        double price = (res.getTicket() != null) ? res.getTicket().getPrice() : 0.0;
        double weight = (res.getTicket() != null && res.getTicket().getBaggage() != null)
                ? res.getTicket().getBaggage().getWeight() : 0.0;

        // Pencere içeriği (HTML kullanarak daha şık yapalım)
        String htmlContent = "<html><body style='width: 300px; padding: 10px; font-family: Arial;'>" +
                "<h2 style='color: #0070C0; text-align: center;'>E-TICKET DETAILS</h2>" +
                "<hr>" +
                "<p><b>PNR Code:</b> " + pnr + "</p>" +
                "<p><b>Passenger:</b> " + passenger + "</p>" +
                "<p><b>Flight:</b> " + flightInfo + "</p>" +
                "<p><b>Date/Time:</b> " + dateInfo + "</p>" +
                "<p><b>Seat:</b> <span style='color: red;'>" + seat + "</span></p>" +
                "<hr>" +
                "<p><b>Baggage:</b> " + weight + " kg</p>" +
                "<p><b>Total Price:</b> <b style='font-size: 14px;'>" + price + " TL</b></p>" +
                "<hr>" +
                "<p style='font-size: 10px; color: gray; text-align: center;'>Status: CONFIRMED</p>" +
                "</body></html>";

        // İşte beklediğin o PENCERE (Dialog)
        JOptionPane.showMessageDialog(this,
                new JLabel(htmlContent),
                "Official Ticket: " + pnr,
                JOptionPane.PLAIN_MESSAGE);
    }

}