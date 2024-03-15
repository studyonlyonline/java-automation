package qrlabel.config;

/**
 * Enum defining the available layout types for QR code labels.
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public enum LayoutType {
    /**
     * QR code on the left, text on the right rotated 90 degrees
     */
    LEFT_RIGHT_ROTATED,
    
    /**
     * QR code on the top, text below the QR code
     */
    TOP_BOTTOM,
    
    /**
     * QR code only (used when no text is provided)
     */
    QR_ONLY,
    
    /**
     * QR code and text printed only on the right half of the label,
     * leaving the left half blank
     */
    RIGHT_HALF_ONLY,
    
    /**
     * QR code and text printed only on the left half of the label,
     * leaving the right half blank
     */
    LEFT_HALF_ONLY,
    
    /**
     * Two different QR codes with their respective texts,
     * one on the left half and one on the right half
     */
    DUAL_QR_LEFT_RIGHT,
    
    /**
     * Two different QR codes with their respective texts positioned to the right,
     * one on the left half and one on the right half (QR left, text right within each half)
     */
    DUAL_QR_WITH_TEXT_ON_RIGHT
}
