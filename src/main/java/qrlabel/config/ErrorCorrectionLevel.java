package qrlabel.config;

/**
 * Error correction levels for QR codes.
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public enum ErrorCorrectionLevel {
    /**
     * Low error correction (~7% recovery)
     */
    L,
    
    /**
     * Medium error correction (~15% recovery)
     */
    M,
    
    /**
     * Quartile error correction (~25% recovery)
     */
    Q,
    
    /**
     * High error correction (~30% recovery)
     */
    H
}
