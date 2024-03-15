package qrlabel.layout;

import qrlabel.config.QRLabelConfiguration;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Layout strategy that displays only the QR code, centered on the label.
 * This layout is used when no text is provided or when explicitly requested.
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public class QROnlyLayout implements LayoutStrategy {
    
    @Override
    public BufferedImage createLayout(BufferedImage qrCode, String text, QRLabelConfiguration config) {
        int labelWidth = config.getPaperWidthPixels();
        int labelHeight = config.getPaperHeightPixels();
        
        // Create the label image
        BufferedImage labelImage = new BufferedImage(labelWidth, labelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = labelImage.createGraphics();
        
        // Enable anti-aliasing for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // Fill background with white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, labelWidth, labelHeight);
        
        // Calculate QR code size and position to maximize space usage
        int availableWidth = labelWidth - (2 * config.getMarginPixels());
        int availableHeight = labelHeight - (2 * config.getMarginPixels());
        
        // Use the smaller dimension to ensure QR code fits
        int maxQRSize = Math.min(availableWidth, availableHeight);
        int qrSize = (int) (maxQRSize * config.getQrSizeRatio());
        
        // Scale QR code if necessary
        BufferedImage scaledQR = scaleImage(qrCode, qrSize, qrSize);
        
        // Center the QR code on the label
        int qrX = (labelWidth - qrSize) / 2;
        int qrY = (labelHeight - qrSize) / 2;
        
        // Draw the QR code
        g2d.drawImage(scaledQR, qrX, qrY, null);
        
        g2d.dispose();
        return labelImage;
    }
    
    @Override
    public String getLayoutName() {
        return "QR Code Only";
    }
    
    @Override
    public boolean supportsText() {
        return false;
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
