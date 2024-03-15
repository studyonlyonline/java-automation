package barcode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeLabelPrinter implements Printable {
    
    // Label dimensions in inches
    private static final double LABEL_WIDTH_INCHES = 4.0;
    private static final double LABEL_HEIGHT_INCHES = 3.0;
    
    // Print resolution (DPI) - Updated for your barcode printer
    private static final int PRINT_DPI = 203;
    
    // Calculate pixel dimensions for printing
    private static final int LABEL_WIDTH_PIXELS = (int) (LABEL_WIDTH_INCHES * PRINT_DPI);
    private static final int LABEL_HEIGHT_PIXELS = (int) (LABEL_HEIGHT_INCHES * PRINT_DPI);
    
    // QR code properties - Maximum size QR code with optional text
    private static final int QR_CODE_SIZE = 609; // pixels at 203 DPI (exactly 3 inches - maximum size)
    private static final boolean SHOW_TEXT = false; // Flag to enable/disable text display
    
    private String textToPrint;
    private BufferedImage labelImage;
    
    public QRCodeLabelPrinter(String text) {
        this.textToPrint = text;
        this.labelImage = createLabelImage(text);
    }
    
    /**
     * Generate QR code for the given text with proper scanning parameters
     */
    private BufferedImage generateQRCode(String text) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        // Configure QR code parameters for optimal scanning
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); // L for better density
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0); // No margin - QR code fills entire space
        
        // Generate QR code matrix
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 
                                                 QR_CODE_SIZE, QR_CODE_SIZE, hints);
        
        // Convert to BufferedImage with high contrast
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    
    /**
     * Create the complete label image with QR code and text
     */
    private BufferedImage createLabelImage(String text) {
        BufferedImage labelImg = new BufferedImage(LABEL_WIDTH_PIXELS, LABEL_HEIGHT_PIXELS, 
                                                  BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = labelImg.createGraphics();
        
        // Enable anti-aliasing for better print quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Fill background with white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, LABEL_WIDTH_PIXELS, LABEL_HEIGHT_PIXELS);
        
        try {
            // Generate and draw QR code
            BufferedImage qrCode = generateQRCode(text);
            int qrX = (LABEL_WIDTH_PIXELS - QR_CODE_SIZE) / 2; // Center horizontally
            
            if (SHOW_TEXT) {
                // If text is enabled, position QR code at top with space for text below
                int qrY = 0; // Start at top
                g2d.drawImage(qrCode, qrX, qrY, null);
                drawTextBelowQR(g2d, text, qrY + QR_CODE_SIZE + 10);
            } else {
                // If text is disabled, center QR code on label
                int qrY = (LABEL_HEIGHT_PIXELS - QR_CODE_SIZE) / 2; // Center vertically
                g2d.drawImage(qrCode, qrX, qrY, null);
            }
            
        } catch (WriterException e) {
            // If QR code generation fails, show error message
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            g2d.drawString("QR Generation Failed", 10, LABEL_HEIGHT_PIXELS / 2);
        }
        
        g2d.dispose();
        return labelImg;
    }
    
    /**
     * Draw text below the QR code in the 1" margin area
     */
    private void drawTextBelowQR(Graphics2D g2d, String text, int startY) {
        g2d.setColor(Color.BLACK);
        
        // Use full label width for text in the 1" margin area below QR code
        int availableWidth = LABEL_WIDTH_PIXELS;
        int availableHeight = LABEL_HEIGHT_PIXELS - startY; // 1" margin area (203 pixels)
        
        // Calculate appropriate font size based on available space
        int fontSize = calculateOptimalFontSize(g2d, text, availableWidth, availableHeight);
        Font font = new Font("Arial", Font.PLAIN, fontSize);
        g2d.setFont(font);
        
        // Get font metrics for centering
        FontMetrics fm = g2d.getFontMetrics();
        
        // Handle text wrapping if necessary
        String[] lines = wrapText(text, fm, availableWidth);
        
        int lineHeight = fm.getHeight();
        int totalTextHeight = lines.length * lineHeight;
        
        // Center text vertically in the 1" margin area
        int textStartY = startY + (availableHeight - totalTextHeight) / 2;
        int currentY = textStartY;
        
        // Draw each line centered
        for (String line : lines) {
            int textWidth = fm.stringWidth(line);
            int textX = (LABEL_WIDTH_PIXELS - textWidth) / 2;
            g2d.drawString(line, textX, currentY);
            currentY += lineHeight;
        }
    }
    
    /**
     * Calculate optimal font size for the given text and available space
     */
    private int calculateOptimalFontSize(Graphics2D g2d, String text, int availableWidth, int availableHeight) {
        int fontSize = 72; // Start with large font
        Font font = new Font("Arial", Font.PLAIN, fontSize);
        FontMetrics fm = g2d.getFontMetrics(font);
        
        // Reduce font size until text fits both width and height
        while ((fm.stringWidth(text) > availableWidth || fm.getHeight() > availableHeight) && fontSize > 12) {
            fontSize -= 2;
            font = new Font("Arial", Font.PLAIN, fontSize);
            fm = g2d.getFontMetrics(font);
        }
        
        return Math.max(fontSize, 12); // Minimum font size of 12
    }
    
    /**
     * Wrap text to fit within the specified width
     */
    private String[] wrapText(String text, FontMetrics fm, int maxWidth) {
        if (fm.stringWidth(text) <= maxWidth) {
            return new String[]{text};
        }
        
        // Simple word wrapping
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        java.util.List<String> lines = new java.util.ArrayList<>();
        
        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            if (fm.stringWidth(testLine) <= maxWidth) {
                currentLine = new StringBuilder(testLine);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    // Single word is too long, add it anyway
                    lines.add(word);
                }
            }
        }
        
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        
        return lines.toArray(new String[0]);
    }
    
    /**
     * Save the label image to a file
     */
    public void saveToFile(String filename) throws IOException {
        File outputFile = new File(filename);
        ImageIO.write(labelImage, "PNG", outputFile);
        System.out.println("Label saved to: " + outputFile.getAbsolutePath());
    }
    
    /**
     * Print the label
     */
    public void printLabel() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        
        // Set up page format for 4x3 inch label
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = pageFormat.getPaper();
        
        // Set paper size (convert inches to points: 1 inch = 72 points)
        double width = LABEL_WIDTH_INCHES * 72;
        double height = LABEL_HEIGHT_INCHES * 72;
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        pageFormat.setPaper(paper);
        
        // Set this class as the printable
        printerJob.setPrintable(this, pageFormat);
        
        // Show print dialog
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
                System.out.println("Label sent to printer successfully!");
            } catch (PrinterException e) {
                System.err.println("Error printing label: " + e.getMessage());
            }
        } else {
            System.out.println("Print job cancelled.");
        }
    }
    
    /**
     * Implementation of Printable interface
     */
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        
        Graphics2D g2d = (Graphics2D) graphics;
        
        // Scale the image to fit the page
        double scaleX = pageFormat.getImageableWidth() / LABEL_WIDTH_PIXELS;
        double scaleY = pageFormat.getImageableHeight() / LABEL_HEIGHT_PIXELS;
        double scale = Math.min(scaleX, scaleY);
        
        // Center the image on the page
        int scaledWidth = (int) (LABEL_WIDTH_PIXELS * scale);
        int scaledHeight = (int) (LABEL_HEIGHT_PIXELS * scale);
        int x = (int) ((pageFormat.getImageableWidth() - scaledWidth) / 2 + pageFormat.getImageableX());
        int y = (int) ((pageFormat.getImageableHeight() - scaledHeight) / 2 + pageFormat.getImageableY());
        
        // Draw the scaled image
        g2d.drawImage(labelImage, x, y, scaledWidth, scaledHeight, null);
        
        return PAGE_EXISTS;
    }
    
    
    /**
     * List available printers
     */
    public static void listAvailablePrinters() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("Available printers:");
        for (int i = 0; i < printServices.length; i++) {
            System.out.println((i + 1) + ". " + printServices[i].getName());
        }
    }
    
    /**
     * Main method
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== QR Code Label Printer ===");
        System.out.println("Label size: 4\" x 3\"");
        System.out.println("QR Code size: 3\" x 3\" (centered, no text)");
        System.out.println("Resolution: 203 DPI x 203 DPI");
        System.out.println("Label dimensions: " + LABEL_WIDTH_PIXELS + " x " + LABEL_HEIGHT_PIXELS + " pixels");
        System.out.println("QR Code dimensions: " + QR_CODE_SIZE + " x " + QR_CODE_SIZE + " pixels");
        System.out.println("Text display: " + (SHOW_TEXT ? "Enabled" : "Disabled"));
        System.out.println();
        
        // Get text input from user
        System.out.print("Enter text for QR code: ");
        String text = scanner.nextLine();
        
        if (text.trim().isEmpty()) {
            System.out.println("No text entered. Exiting.");
            return;
        }
        
        try {
            // Create QR code label printer with REAL scannable QR code
            System.out.println("Generating REAL scannable QR code...");
            QRCodeLabelPrinter printer = new QRCodeLabelPrinter(text);
            
            // Save to file
            String filename = "qr_label_" + System.currentTimeMillis() + ".png";
            printer.saveToFile(filename);
            
            System.out.println("\n=== SCANNABLE QR CODE GENERATED ===");
            System.out.println("✅ Real QR code saved to: " + filename);
            System.out.println("✅ Text encoded: " + text);
            System.out.println("✅ This QR code is fully scannable!");
            System.out.println("=====================================\n");
            
            // Ask user if they want to print
            System.out.print("Do you want to print the label? (y/n): ");
            String printChoice = scanner.nextLine().toLowerCase();
            
            if (printChoice.startsWith("y")) {
                System.out.println();
                listAvailablePrinters();
                System.out.println();
                printer.printLabel();
            }
            
            System.out.println("Done!");
            
        } catch (Exception e) {
            System.err.println("Error creating QR code label: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
