package com.airline.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import com.airline.models.Seat;


public class SeatMapPanel extends JPanel {
    private Seat[][] seats;
    private Seat selectedSeat;
    private SeatSelectionListener selectionListener;

    private static final int SEAT_WIDTH = 50;
    private static final int SEAT_HEIGHT = 50;
    private static final int AISLE_WIDTH = 50;
    private static final int ROW_SPACING = 60;
    private static final int COLUMN_SPACING = 55;
    private static final int MARGIN = 30;

    public interface SeatSelectionListener {
        void seatSelected(Seat seat);
    }

    public SeatMapPanel(Seat[][] seatMatrix) {
        this.seats = seatMatrix;
        this.selectedSeat = null;
        setBackground(new Color(240, 240, 240));
        setPreferredSize(calculateSize());

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleSeatClick(e.getX(), e.getY());
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    private Dimension calculateSize() {
        // 30 rows x (3 cols + aisle + 3 cols) = 30 x 6 seats
        int width = MARGIN * 2 + (SEAT_WIDTH * 3) + AISLE_WIDTH + (SEAT_WIDTH * 3) + (COLUMN_SPACING * 5);
        int height = MARGIN * 2 + (ROW_SPACING * 30);
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (seats == null) return;

        // Draw title
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.BLACK);
        g2d.drawString("Airplane Seat Map (6 Columns x 30 Rows)", MARGIN, MARGIN - 5);

        // Draw column labels
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        int x = MARGIN;
        String[] leftCols = {"A", "B", "C"};
        for (String col : leftCols) {
            g2d.drawString(col, x + SEAT_WIDTH / 2 - 3, MARGIN + 15);
            x += COLUMN_SPACING;
        }
        x += AISLE_WIDTH;
        String[] rightCols = {"D", "E", "F"};
        for (String col : rightCols) {
            g2d.drawString(col, x + SEAT_WIDTH / 2 - 3, MARGIN + 15);
            x += COLUMN_SPACING;
        }

        // Draw seats
        int startY = MARGIN + 25;
        for (int row = 0; row < seats.length; row++) {
            int y = startY + (row * ROW_SPACING);

            // Row number
            g2d.drawString(String.valueOf(row + 1), MARGIN - 15, y + SEAT_HEIGHT / 2 + 3);

            int seatX = MARGIN;

            // Left side seats (A, B, C)
            for (int col = 0; col < 3; col++) {
                drawSeat(g2d, seatX, y, seats[row][col]);
                seatX += COLUMN_SPACING;
            }

            // Aisle
            g2d.setColor(new Color(200, 200, 200));
            g2d.fillRect(seatX, y, AISLE_WIDTH, SEAT_HEIGHT);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(seatX, y, AISLE_WIDTH, SEAT_HEIGHT);
            seatX += AISLE_WIDTH;

            // Right side seats (D, E, F)
            for (int col = 3; col < 6; col++) {
                drawSeat(g2d, seatX, y, seats[row][col]);
                seatX += COLUMN_SPACING;
            }
        }

        // Draw legend
        int legendY = startY + (seats.length * ROW_SPACING) + 15;
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));

        // Available
        g2d.setColor(new Color(34, 177, 76));
        g2d.fillRect(MARGIN, legendY, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(MARGIN, legendY, 12, 12);
        g2d.drawString("Available", MARGIN + 18, legendY + 10);

        // Booked
        g2d.setColor(new Color(255, 0, 0));
        g2d.fillRect(MARGIN + 120, legendY, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(MARGIN + 120, legendY, 12, 12);
        g2d.drawString("Booked", MARGIN + 136, legendY + 10);

        // Selected
        g2d.setColor(new Color(0, 112, 192));
        g2d.fillRect(MARGIN + 210, legendY, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(MARGIN + 210, legendY, 12, 12);
        g2d.drawString("Selected", MARGIN + 226, legendY + 10);
        
        g2d.setColor(new Color(145, 30, 180));
        g2d.fillRect(MARGIN + 310, legendY, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(MARGIN + 310, legendY, 12, 12);
        g2d.drawString("Business", MARGIN + 326, legendY + 10);
    }

    private void drawSeat(Graphics2D g2d, int x, int y, Seat seat) {
        Color seatColor;
        String label = "";

        if (seat == null) {
            seatColor = new Color(200, 200, 200); // Gri - Koltuk yok
        } else {
            if (selectedSeat != null && selectedSeat.equals(seat)) {
                seatColor = new Color(0, 200, 255); // Açık Mavi - Seçili
            } else if (seat.isReserved()) {
                seatColor = new Color(255, 165, 0); // Turuncu - Dolu
            } else if (seat.getSeatClass() == Seat.SeatClass.BUSINESS) {
                seatColor = new Color(145, 30, 180); // MOR - Business Class
            } else {
                seatColor = new Color(0, 112, 192); // Koyu Mavi - Economy (Boş)
            }
            label = seat.getSeatNum();
        }

        // Koltuğu çiz
        g2d.setColor(seatColor);
        g2d.fillRect(x, y, SEAT_WIDTH, SEAT_HEIGHT);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRect(x, y, SEAT_WIDTH, SEAT_HEIGHT);

        // Etiketi çiz
        if (!label.isEmpty()) {
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.setColor(Color.WHITE);
            g2d.drawString(label, x + 5, y + 13);
        }
    }

    private void handleSeatClick(int mouseX, int mouseY) {
        if (seats == null) return;

        int startY = MARGIN + 25;

        for (int row = 0; row < seats.length; row++) {
            int y = startY + (row * ROW_SPACING);
            int seatX = MARGIN;

            // Check left side seats (A, B, C)
            for (int col = 0; col < 3; col++) {
                if (clickedOnSeat(mouseX, mouseY, seatX, y)) {
                    selectSeat(seats[row][col]);
                    return;
                }
                seatX += COLUMN_SPACING;
            }

            // Skip aisle
            seatX += AISLE_WIDTH;

            // Check right side seats (D, E, F)
            for (int col = 3; col < 6; col++) {
                if (clickedOnSeat(mouseX, mouseY, seatX, y)) {
                    selectSeat(seats[row][col]);
                    return;
                }
                seatX += COLUMN_SPACING;
            }
        }
    }

    private boolean clickedOnSeat(int mouseX, int mouseY, int seatX, int seatY) {
        return mouseX >= seatX && mouseX <= seatX + SEAT_WIDTH &&
                mouseY >= seatY && mouseY <= seatY + SEAT_HEIGHT;
    }

    private void selectSeat(Seat seat) {
        if (seat == null) {
            JOptionPane.showMessageDialog(this, "Please select a flight first!");
            return;
        }
        if (seat.isReserved()) {
            JOptionPane.showMessageDialog(this, "This seat is not available!");
            return;
        }

        this.selectedSeat = seat;
        repaint();

        if (selectionListener != null) {
            selectionListener.seatSelected(seat);
        }
    }

    public void setSeatSelectionListener(SeatSelectionListener listener) {
        this.selectionListener = listener;
    }

    public Seat getSelectedSeat() {
        return selectedSeat;
    }

    public void updateSeats(Seat[][] newSeats) {
        this.seats = newSeats;
        this.selectedSeat = null;
        repaint();
    }

    public void clearSelection() {
        this.selectedSeat = null;
        repaint();
    }
}
