package qrlabel.layout;

import qrlabel.config.QRLabelConfiguration;
import qrlabel.factory.QRCodeFactory;
import com.google.zxing.WriterException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Layout strategy that prints two different QR codes with their respective texts,
 * one on the left half and one on the right half of the label.
 * This is useful for dual-purpose labels like shipping/return labels,
 * inventory tracking with multiple references, etc.
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public class DualQRLayout implements LayoutStrategy {
    
    @Override
    public BufferedImage createLayout(BufferedImage qrCode, String text, QRLabelConfiguration config) {
        int labelWidth = config.getPaperWidthPixels();
        int labelHeight = config.getPaperHeightPixels();
        
        // Create the full label image
        BufferedImage labelImage = new BufferedImage(labelWidth, labelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = labelImage.createGraphics();
        
        // Enable anti-aliasing for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // Fill entire background with white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, labelWidth, labelHeight);
        
        try {
            // Generate both QR codes
            String leftText = config.getLeftQRText() != null ? config.getLeftQRText() : text;
            String rightText = config.getRightQRText() != null ? config.getRightQRText() : text;
            
            BufferedImage leftQR = QRCodeFactory.createQRCode(leftText, config);
            BufferedImage rightQR = QRCodeFactory.createQRCode(rightText, config);
            
            // Calculate half dimensions
            int halfWidth = labelWidth / 2;
            int margin = config.getMarginPixels();
            int spacing = config.getSpacingBetweenElements();
            
            // Calculate available space for each half
            int availableWidth = halfWidth - margin;
            int availableHeight = labelHeight - (2 * margin);
            
            // Reserve space for text if provided
            int textHeight = 0;
            if (config.isIncludeText()) {
                textHeight = estimateTextHeight(g2d, "Sample Text", availableWidth, config);
            }
            
            // Calculate QR code dimensions for each half
            int qrAreaHeight = availableHeight - textHeight - (textHeight > 0 ? spacing : 0);
            int maxQRSize = Math.min(availableWidth, qrAreaHeight);
            int qrSize = (int) (maxQRSize * config.getQrSizeRatio());
            
            // Scale QR codes
            BufferedImage scaledLeftQR = scaleImage(leftQR, qrSize, qrSize);
            BufferedImage scaledRightQR = scaleImage(rightQR, qrSize, qrSize);
            
            // Position left QR code
            int leftQRX = margin + (availableWidth - qrSize) / 2;
            int leftQRY = margin;
            g2d.drawImage(scaledLeftQR, leftQRX, leftQRY, null);
            
            // Position right QR code
            int rightQRX = halfWidth + margin + (availableWidth - qrSize) / 2;
            int rightQRY = margin;
            g2d.drawImage(scaledRightQR, rightQRX, rightQRY, null);
            
            // Draw text if enabled
            if (config.isIncludeText() && textHeight > 0) {
                // Left text
                if (leftText != null && !leftText.trim().isEmpty()) {
                    int leftTextY = leftQRY + qrSize + spacing;
                    int textAreaHeight = availableHeight - qrSize - spacing;
                    drawText(g2d, leftText, margin, leftTextY, availableWidth, textAreaHeight, config);
                }
                
                // Right text
                if (rightText != null && !rightText.trim().isEmpty()) {
                    int rightTextY = rightQRY + qrSize + spacing;
                    int textAreaHeight = availableHeight - qrSize - spacing;
                    drawText(g2d, rightText, halfWidth + margin, rightTextY, availableWidth, textAreaHeight, config);
                }
            }
            
        } catch (WriterException e) {
            // If QR generation fails, show error message
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("QR Generation Failed", 10, labelHeight / 2);
        }
        
        g2d.dispose();
        return labelImage;
    }
    
    @Override
    public String getLayoutName() {
        return "Dual QR Codes (Left & Right)";
    }
    
    @Override
    public boolean supportsText() {
        return true;
    }
    
    /**
     * Estimates the height needed for text rendering.
     */
    private int estimateTextHeight(Graphics2D g2d, String text, int availableWidth, QRLabelConfiguration config) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        
        // Start with a reasonable font size for half-width areas
        int fontSize = Math.min(config.getMaxFontSize(), availableWidth / 8);
        Font font = new Font(config.getFontFamily(), 
                           config.isBoldText() ? Font.BOLD : Font.PLAIN, 
                           fontSize);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        
        // Wrap text and calculate height
        List<String> lines = wrapText(text, fm, availableWidth);
        return lines.size() * fm.getHeight();
    }
    
    /**
     * Draws text in the specified area with optimal font sizing.
     */
    private void drawText(Graphics2D g2d, String text, int x, int y, int width, int height, QRLabelConfiguration config) {
        g2d.setColor(Color.BLACK);
        
        // Find optimal font size for half area
        int fontSize = findOptimalFontSize(g2d, text, width, height, config);
        Font font = new Font(config.getFontFamily(), 
                           config.isBoldText() ? Font.BOLD : Font.PLAIN, 
                           fontSize);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        
        // Wrap text to fit width
        List<String> lines = wrapText(text, fm, width);
        
        // Calculate starting Y position to center text vertically
        int totalTextHeight = lines.size() * fm.getHeight();
        int startY = y + (height - totalTextHeight) / 2 + fm.getAscent();
        
        // Draw each line centered in the half
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int lineWidth = fm.stringWidth(line);
            int lineX = x + (width - lineWidth) / 2; // Center in half
            int lineY = startY + (i * fm.getHeight());
            
            g2d.drawString(line, lineX, lineY);
        }
    }
    
    /**
     * Finds the optimal font size that fits the text in the given area.
     */
    private int findOptimalFontSize(Graphics2D g2d, String text, int width, int height, QRLabelConfiguration config) {
        int fontSize = Math.min(config.getMaxFontSize(), width / 6); // Start smaller for half areas
        
        while (fontSize >= config.getMinFontSize()) {
            Font font = new Font(config.getFontFamily(), 
                               config.isBoldText() ? Font.BOLD : Font.PLAIN, 
                               fontSize);
            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics();
            
            List<String> lines = wrapText(text, fm, width);
            int totalHeight = lines.size() * fm.getHeight();
            
            if (totalHeight <= height) {
                return fontSize;
            }
            
            fontSize -= 2; // Decrease font size
        }
        
        return config.getMinFontSize();
    }
    
    /**
     * Wraps text to fit within the specified width.
     */
    private List<String> wrapText(String text, FontMetrics fm, int maxWidth) {
        List<String> lines = new ArrayList<>();
        
        if (text == null || text.trim().isEmpty()) {
            return lines;
        }
        
        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder();
        
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
        
        return lines;
    }
    
    /**
     * Scales an image to the specified dimensions while maintaining quality.
     */
    private BufferedImage scaleImage(BufferedImage original, int targetWidth, int targetHeight) {
        if (original.getWidth() == targetWidth && original.getHeight() == targetHeight) {
            return original;
        }
        
        BufferedImage scaled = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaled.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        
        return scaled;
    }
}
