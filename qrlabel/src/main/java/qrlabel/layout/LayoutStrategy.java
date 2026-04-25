package qrlabel.layout;

import qrlabel.config.QRLabelConfiguration;
import java.awt.image.BufferedImage;

/**
 * Strategy interface for different QR code label layouts.
 * Implements the Strategy design pattern to allow different
 * arrangements of QR codes and text on labels.
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public interface LayoutStrategy {
    
    /**
     * Creates a label layout with the given QR code and optional text.
     * 
     * @param qrCode The QR code image to include in the layout
     * @param text The text to include (can be null or empty for QR-only layouts)
     * @param config The configuration containing layout parameters
     * @return A BufferedImage containing the complete label layout
     */
    BufferedImage createLayout(BufferedImage qrCode, String text, QRLabelConfiguration config);
    
    /**
     * Gets the name of this layout strategy for identification purposes.
     * 
     * @return A descriptive name for this layout strategy
     */
    String getLayoutName();
    
    /**
     * Determines if this layout strategy supports text.
     * 
     * @return true if this layout can handle text, false for QR-only layouts
     */
    boolean supportsText();
}
