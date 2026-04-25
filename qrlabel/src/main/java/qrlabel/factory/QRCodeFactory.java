package qrlabel.factory;

import qrlabel.config.QRLabelConfiguration;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for creating QR code images with configurable parameters.
 * Uses the ZXing library for QR code generation.
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public class QRCodeFactory {
    
    /**
     * Creates a QR code image for the given text using the provided configuration.
     * 
     * @param text The text to encode in the QR code
     * @param size The desired size of the QR code in pixels (width and height)
     * @param config The configuration containing QR code parameters
     * @return A BufferedImage containing the QR code
     * @throws WriterException if QR code generation fails
     */
    public static BufferedImage createQRCode(String text, int size, QRLabelConfiguration config) 
            throws WriterException {
        
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
        
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        // Configure QR code parameters
        Map<EncodeHintType, Object> hints = createHints(config);
        
        // Generate QR code matrix
        BitMatrix bitMatrix = qrCodeWriter.encode(
            text, 
            BarcodeFormat.QR_CODE, 
            size, 
            size, 
            hints
        );
        
        // Convert to BufferedImage with high contrast
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    
    /**
     * Creates a QR code image with default size based on configuration.
     * 
     * @param text The text to encode in the QR code
     * @param config The configuration containing QR code parameters and paper size
     * @return A BufferedImage containing the QR code
     * @throws WriterException if QR code generation fails
     */
    public static BufferedImage createQRCode(String text, QRLabelConfiguration config) 
            throws WriterException {
        
        // Calculate optimal QR code size based on paper dimensions
        int availableWidth = config.getPaperWidthPixels() - (2 * config.getMarginPixels());
        int availableHeight = config.getPaperHeightPixels() - (2 * config.getMarginPixels());
        
        // Use the smaller dimension and apply the size ratio
        int maxSize = Math.min(availableWidth, availableHeight);
        int qrSize = (int) (maxSize * config.getQrSizeRatio());
        
        return createQRCode(text, qrSize, config);
    }
    
    /**
     * Creates encoding hints for QR code generation based on configuration.
     */
    private static Map<EncodeHintType, Object> createHints(QRLabelConfiguration config) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        
        // Error correction level
        hints.put(EncodeHintType.ERROR_CORRECTION, config.getErrorCorrectionLevel());
        
        // Character encoding
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        
        // Margin (quiet zone)
        hints.put(EncodeHintType.MARGIN, config.getQrMargin());
        
        return hints;
    }
    
    /**
     * Validates if the given text can be encoded as a QR code.
     * 
     * @param text The text to validate
     * @return true if the text can be encoded, false otherwise
     */
    public static boolean canEncode(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        try {
            QRCodeWriter writer = new QRCodeWriter();
            writer.encode(text, BarcodeFormat.QR_CODE, 100, 100);
            return true;
        } catch (WriterException e) {
            return false;
        }
    }
    
    /**
     * Estimates the minimum size needed for a QR code with the given text.
     * 
     * @param text The text to encode
     * @param config The configuration containing QR code parameters
     * @return The estimated minimum size in pixels
     */
    public static int estimateMinimumSize(String text, QRLabelConfiguration config) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        
        // Basic estimation based on text length and error correction level
        int baseSize = 21; // Minimum QR code size (version 1)
        int textLength = text.length();
        
        // Rough estimation - more complex text needs larger QR codes
        if (textLength > 25) baseSize += 4;
        if (textLength > 50) baseSize += 4;
        if (textLength > 100) baseSize += 8;
        if (textLength > 200) baseSize += 8;
        
        // Adjust for error correction level
        switch (config.getErrorCorrectionLevel()) {
            case H: baseSize += 8; break;  // High error correction needs more space
            case Q: baseSize += 4; break;  // Quartile error correction
            case M: baseSize += 2; break;  // Medium error correction
            case L: break;                 // Low error correction (default)
        }
        
        return baseSize;
    }
}
