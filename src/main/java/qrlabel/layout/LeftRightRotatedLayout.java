package qrlabel.layout;

import qrlabel.config.QRLabelConfiguration;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Layout strategy that places the QR code on the left and text on the right, rotated 90 degrees.
 * This layout is ideal for narrow labels where horizontal space is limited.
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public class LeftRightRotatedLayout implements LayoutStrategy {
    
    @Override
    public BufferedImage createLayout(BufferedImage qrCode, String text, QRLabelConfiguration config) {
        int labelWidth = config.getPaperWidthPixels();
        int labelHeight = config.getPaperHeightPixels();
        
        // Create the label image
        BufferedImage labelImage = new BufferedImage(labelWidth, labelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = labelImage.createGraphics();
        
        // Enable anti-aliasing for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // Fill background with white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, labelWidth, labelHeight);
        
        // Calculate available space
        int margin = config.getMarginPixels();
        int spacing = config.getSpacingBetweenElements();
        int availableWidth = labelWidth - (2 * margin);
        int availableHeight = labelHeight - (2 * margin);
        
        // Determine space allocation between QR code and text
        int textWidth = 0;
        if (text != null && !text.trim().isEmpty()) {
            // Reserve space for text (estimate based on label height for rotated text)
            textWidth = estimateTextWidth(g2d, text, availableHeight, config);
        }
        
        // Calculate QR code dimensions
        int qrAreaWidth = availableWidth - textWidth - (textWidth > 0 ? spacing : 0);
        int maxQRSize = Math.min(qrAreaWidth, availableHeight);
        int qrSize = (int) (maxQRSize * config.getQrSizeRatio());
        
        // Scale and position QR code on the left
        BufferedImage scaledQR = scaleImage(qrCode, qrSize, qrSize);
        int qrX = margin;
        int qrY = margin + (availableHeight - qrSize) / 2; // Center vertically
        
        // Draw QR code
        g2d.drawImage(scaledQR, qrX, qrY, null);
        
        // Draw rotated text if provided
        if (text != null && !text.trim().isEmpty() && textWidth > 0) {
            int textX = qrX + qrSize + spacing;
            int textAreaWidth = availableWidth - qrSize - spacing;
            drawRotatedText(g2d, text, textX, margin, textAreaWidth, availableHeight, config);
        }
        
        g2d.dispose();
        return labelImage;
    }
    
    @Override
    public String getLayoutName() {
        return "QR Left, Text Right (Rotated 90°)";
    }
    
    @Override
    public boolean supportsText() {
        return true;
    }
    
    /**
     * Estimates the width needed for rotated text rendering.
     */
    private int estimateTextWidth(Graphics2D g2d, String text, int availableHeight, QRLabelConfiguration config) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        
        // Start with a reasonable font size
        int fontSize = Math.min(config.getMaxFontSize(), availableHeight / 15); // Rough estimate
        Font font = new Font(config.getFontFamily(), 
                           config.isBoldText() ? Font.BOLD : Font.PLAIN, 
                           fontSize);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        
        // For rotated text, the "width" becomes the height when rotated
        // Estimate based on font height (which becomes width when rotated)
        return fm.getHeight() + 10; // Add some padding
    }
    
    /**
     * Draws text rotated 90 degrees clockwise in the specified area.
     */
    private void drawRotatedText(Graphics2D g2d, String text, int x, int y, int width, int height, QRLabelConfiguration config) {
        // Save the current transform
        AffineTransform originalTransform = g2d.getTransform();
        
        try {
            g2d.setColor(Color.BLACK);
            
            // Find optimal font size for rotated text
            int fontSize = findOptimalFontSizeForRotatedText(g2d, text, width, height, config);
            Font font = new Font(config.getFontFamily(), 
                               config.isBoldText() ? Font.BOLD : Font.PLAIN, 
                               fontSize);
            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics();
            
            // Wrap text to fit the available height (which becomes width when rotated)
            List<String> lines = wrapTextForRotation(text, fm, height);
            
            // Calculate positioning for rotated text
            int lineHeight = fm.getHeight();
            int totalTextWidth = lines.size() * lineHeight; // Width when rotated
            
            // Center the text block horizontally in the available width
            int startX = x + (width - totalTextWidth) / 2;
            
            // Draw each line rotated
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                int lineWidth = fm.stringWidth(line); // Height when rotated
                
                // Position for this line
                int lineX = startX + (i * lineHeight) + lineHeight / 2;
                int lineY = y + (height + lineWidth) / 2; // Center vertically
                
                // Create rotation transform (90 degrees clockwise)
                AffineTransform rotateTransform = new AffineTransform();
                rotateTransform.translate(lineX, lineY);
                rotateTransform.rotate(-Math.PI / 2); // -90 degrees (clockwise)
                
                g2d.setTransform(rotateTransform);
                g2d.drawString(line, 0, 0);
                
                // Restore transform for next line
                g2d.setTransform(originalTransform);
            }
        } finally {
            // Always restore the original transform
            g2d.setTransform(originalTransform);
        }
    }
    
    /**
     * Finds the optimal font size for rotated text that fits in the given area.
     */
    private int findOptimalFontSizeForRotatedText(Graphics2D g2d, String text, int width, int height, QRLabelConfiguration config) {
        int fontSize = config.getMaxFontSize();
        
        while (fontSize >= config.getMinFontSize()) {
            Font font = new Font(config.getFontFamily(), 
                               config.isBoldText() ? Font.BOLD : Font.PLAIN, 
                               fontSize);
            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics();
            
            List<String> lines = wrapTextForRotation(text, fm, height);
            
            // Check if text fits when rotated
            int totalWidth = lines.size() * fm.getHeight(); // Width when rotated
            int maxLineHeight = 0;
            for (String line : lines) {
                maxLineHeight = Math.max(maxLineHeight, fm.stringWidth(line)); // Height when rotated
            }
            
            if (totalWidth <= width && maxLineHeight <= height) {
                return fontSize;
            }
            
            fontSize -= 2; // Decrease font size
        }
        
        return config.getMinFontSize();
    }
    
    /**
     * Wraps text for rotation - considers the height as the available width for text.
     */
    private List<String> wrapTextForRotation(String text, FontMetrics fm, int maxHeight) {
        List<String> lines = new ArrayList<>();
        
        if (text == null || text.trim().isEmpty()) {
            return lines;
        }
        
        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder();
        
        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            
            // For rotated text, string width becomes the height
            if (fm.stringWidth(testLine) <= maxHeight) {
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
