package qrlabel.layout;

import qrlabel.config.QRLabelConfiguration;
import qrlabel.factory.QRCodeFactory;
import com.google.zxing.WriterException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Layout strategy that prints two different QR codes with their respective texts
 * positioned to the right of each QR code within each half of the label.
 * 
 * Layout within each 2×1 area:
 * ┌─────┬─────┐
 * │ QR  │TEXT │ ← Each half: QR on left, text on right
 * │     │     │
 * └─────┴─────┘
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public class DualQRWithTextOnRightQRLayout implements LayoutStrategy {
    
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
            
            // Calculate half dimensions with center margin to prevent overlap
            int centerMargin = 8; // 8 pixels spacing between left and right halves
            int halfWidth = (labelWidth - centerMargin) / 2;
            int margin = config.getMarginPixels();
            int spacing = config.getSpacingBetweenElements();
            
            // Calculate available space for each half
            int availableHalfWidth = halfWidth - margin;
            int availableHeight = labelHeight - (2 * margin);
            
            // Within each half: divide horizontally between QR and text
            int qrAreaWidth = (int) (availableHalfWidth * 0.5);  // 50% for QR (more space for text)
            int textAreaWidth = availableHalfWidth - qrAreaWidth - spacing; // Remaining for text
            
            // Calculate QR code size (square, fitting within QR area)
            int maxQRSize = Math.min(qrAreaWidth, availableHeight);
            int qrSize = (int) (maxQRSize * config.getQrSizeRatio());
            
            // Scale QR codes
            BufferedImage scaledLeftQR = scaleImage(leftQR, qrSize, qrSize);
            BufferedImage scaledRightQR = scaleImage(rightQR, qrSize, qrSize);
            
            // Position left QR code (left side of left half)
            int leftQRX = margin + (qrAreaWidth - qrSize) / 2;
            int leftQRY = margin + (availableHeight - qrSize) / 2;
            g2d.drawImage(scaledLeftQR, leftQRX, leftQRY, null);
            
            // Position right QR code (left side of right half, accounting for center margin)
            int rightQRX = halfWidth + centerMargin + margin + (qrAreaWidth - qrSize) / 2;
            int rightQRY = margin + (availableHeight - qrSize) / 2;
            g2d.drawImage(scaledRightQR, rightQRX, rightQRY, null);
            
            // Draw text if enabled (to the right of each QR code)
            if (config.isIncludeText()) {
                // Left text (right side of left half)
                if (leftText != null && !leftText.trim().isEmpty()) {
                    int leftTextX = margin + qrAreaWidth + spacing;
                    int leftTextY = margin;
                    drawText(g2d, leftText, leftTextX, leftTextY, textAreaWidth, availableHeight, config);
                }
                
                // Right text (right side of right half, accounting for center margin)
                if (rightText != null && !rightText.trim().isEmpty()) {
                    int rightTextX = halfWidth + centerMargin + margin + qrAreaWidth + spacing;
                    int rightTextY = margin;
                    drawText(g2d, rightText, rightTextX, rightTextY, textAreaWidth, availableHeight, config);
                }
            }
            
        } catch (WriterException e) {
            // If QR generation fails, show error message
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("QR Generation Failed", 10, labelHeight / 2);
        }
        
        g2d.dispose();
        return labelImage;
    }
    
    @Override
    public String getLayoutName() {
        return "Dual QR with Text on Right";
    }
    
    @Override
    public boolean supportsText() {
        return true;
    }
    
    /**
     * Draws text in the specified area with optimal font sizing.
     * Uses clipping to enforce hard boundaries and prevent text overflow.
     */
    private void drawText(Graphics2D g2d, String text, int x, int y, int width, int height, QRLabelConfiguration config) {
        // Save original clip to restore later
        Shape originalClip = g2d.getClip();
        
        // Set clip region to enforce hard boundaries - text CANNOT exceed this area
        g2d.setClip(x, y, width, height);
        
        g2d.setColor(Color.BLACK);
        
        // Find optimal font size for text area
        int fontSize = findOptimalFontSize(g2d, text, width, height, config);
        Font font = new Font(config.getFontFamily(), 
                           config.isBoldText() ? Font.BOLD : Font.PLAIN, 
                           fontSize);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        
        // Calculate dynamic character limit based on font size and area
        int dynamicCharLimit = calculateDynamicCharacterLimit(fm, width, height);
        
        // Limit text to calculated character limit
        String limitedText = text.length() > dynamicCharLimit ? 
            text.substring(0, dynamicCharLimit) + "..." : text;
        
        // Wrap text using character-based wrapping (breaks words if needed)
        List<String> lines = wrapTextByCharacters(limitedText, fm, width);
        
        // Calculate starting Y position to center text vertically
        int totalTextHeight = lines.size() * fm.getHeight();
        int startY = y + (height - totalTextHeight) / 2 + fm.getAscent();
        
        // Draw each line centered in the text area with boundary enforcement
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int lineWidth = fm.stringWidth(line);
            
            // Calculate center position, but ensure it doesn't go negative
            int centeredX = x + (width - lineWidth) / 2;
            // Enforce left boundary: text must start at or after x
            int lineX = Math.max(x, centeredX);
            int lineY = startY + (i * fm.getHeight());
            
            g2d.drawString(line, lineX, lineY);
        }
        
        // Restore original clip region
        g2d.setClip(originalClip);
    }
    
    /**
     * Finds the optimal font size that fits the text in the given area using character-based wrapping.
     */
    private int findOptimalFontSize(Graphics2D g2d, String text, int width, int height, QRLabelConfiguration config) {
        int fontSize = Math.min(config.getMaxFontSize(), width / 4); // Start smaller for text areas
        
        while (fontSize >= config.getMinFontSize()) {
            Font font = new Font(config.getFontFamily(), 
                               config.isBoldText() ? Font.BOLD : Font.PLAIN, 
                               fontSize);
            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics();
            
            // Calculate dynamic character limit and apply it to text
            int dynamicCharLimit = calculateDynamicCharacterLimit(fm, width, height);
            String limitedText = text.length() > dynamicCharLimit ? 
                text.substring(0, dynamicCharLimit) + "..." : text;
            
            // Use character-based wrapping to check fit
            List<String> lines = wrapTextByCharacters(limitedText, fm, width);
            int totalHeight = lines.size() * fm.getHeight();
            
            if (totalHeight <= height) {
                return fontSize;
            }
            
            fontSize -= 2; // Decrease font size
        }
        
        return config.getMinFontSize();
    }
    
    
    /**
     * Calculates dynamic character limit based on font size and available text area.
     */
    private int calculateDynamicCharacterLimit(FontMetrics fm, int width, int height) {
        // Use 'M' as average character width reference
        int avgCharWidth = fm.charWidth('M');
        
        // Calculate how many characters fit per line
        int charactersPerLine = Math.max(1, width / avgCharWidth);
        
        // Calculate how many lines fit in height
        int availableLines = Math.max(1, height / fm.getHeight());
        
        // Total character limit with safety buffer
        int totalCharLimit = (charactersPerLine * availableLines) - 3; // Reserve space for "..."
        
        // Minimum of 5 characters to ensure something is always shown
        return Math.max(5, totalCharLimit);
    }
    
    /**
     * Wraps text using character-based approach, breaking words if necessary.
     */
    private List<String> wrapTextByCharacters(String text, FontMetrics fm, int maxWidth) {
        List<String> lines = new ArrayList<>();
        
        if (text == null || text.trim().isEmpty()) {
            return lines;
        }
        
        StringBuilder currentLine = new StringBuilder();
        
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            String testLine = currentLine.toString() + currentChar;
            
            // Check if adding this character would exceed width
            if (fm.stringWidth(testLine) <= maxWidth) {
                currentLine.append(currentChar);
            } else {
                // Current line is full, start new line
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder();
                    currentLine.append(currentChar);
                } else {
                    // Even single character is too wide (shouldn't happen with proper font sizing)
                    currentLine.append(currentChar);
                }
            }
        }
        
        // Add final line if not empty
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
