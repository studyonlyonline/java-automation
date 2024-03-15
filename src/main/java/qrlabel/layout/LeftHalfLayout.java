package qrlabel.layout;

import qrlabel.config.QRLabelConfiguration;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Layout strategy that prints QR code and text only on the left half of the label,
 * leaving the right half completely blank. This is useful for labels where the right
 * side needs to remain empty for other purposes (stickers, stamps, etc.).
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public class LeftHalfLayout implements LayoutStrategy {
    
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
        
        // Calculate left half dimensions
        int leftHalfStartX = 0; // Start at left edge
        int leftHalfWidth = labelWidth / 2;  // Left half width
        int leftHalfHeight = labelHeight;    // Full height
        
        // Calculate available space in left half (accounting for margins)
        int margin = config.getMarginPixels();
        int spacing = config.getSpacingBetweenElements();
        int availableWidth = leftHalfWidth - margin; // Only left margin needed
        int availableHeight = leftHalfHeight - (2 * margin);
        
        // Reserve space for text if provided
        int textHeight = 0;
        if (text != null && !text.trim().isEmpty()) {
            textHeight = estimateTextHeight(g2d, text, availableWidth, config);
        }
        
        // Calculate QR code dimensions within left half
        int qrAreaHeight = availableHeight - textHeight - (textHeight > 0 ? spacing : 0);
        int maxQRSize = Math.min(availableWidth, qrAreaHeight);
        int qrSize = (int) (maxQRSize * config.getQrSizeRatio());
        
        // Position QR code in left half
        BufferedImage scaledQR = scaleImage(qrCode, qrSize, qrSize);
        int qrX = leftHalfStartX + margin + (availableWidth - qrSize) / 2; // Center in left half
        int qrY = margin; // Top of available area
        
        // Draw QR code
        g2d.drawImage(scaledQR, qrX, qrY, null);
        
        // Draw text if provided
        if (text != null && !text.trim().isEmpty() && textHeight > 0) {
            int textX = leftHalfStartX + margin;
            int textY = qrY + qrSize + spacing;
            int textAreaHeight = availableHeight - qrSize - spacing;
            drawText(g2d, text, textX, textY, availableWidth, textAreaHeight, config);
        }
        
        g2d.dispose();
        return labelImage;
    }
    
    @Override
    public String getLayoutName() {
        return "Left Half Only (Right Half Blank)";
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
        
        // Start with a reasonable font size for the smaller left area
        int fontSize = Math.min(config.getMaxFontSize(), availableWidth / 8); // Smaller estimate for left half
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
        
        // Find optimal font size for left half area
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
        
        // Draw each line centered in left half
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int lineWidth = fm.stringWidth(line);
            int lineX = x + (width - lineWidth) / 2; // Center in left half
            int lineY = startY + (i * fm.getHeight());
            
            g2d.drawString(line, lineX, lineY);
        }
    }
    
    /**
     * Finds the optimal font size that fits the text in the given area.
     */
    private int findOptimalFontSize(Graphics2D g2d, String text, int width, int height, QRLabelConfiguration config) {
        int fontSize = Math.min(config.getMaxFontSize(), width / 6); // Start smaller for left half
        
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
