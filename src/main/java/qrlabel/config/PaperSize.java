package qrlabel.config;

/**
 * Enum defining common paper sizes for QR code labels.
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public enum PaperSize {
    /**
     * 4x3 inch label (common for shipping labels)
     */
    LABEL_4X3(4.0, 3.0),
    
    /**
     * 4x6 inch label (standard shipping label)
     */
    LABEL_4X6(4.0, 6.0),
    
    /**
     * 2x1 inch label (small product label)
     */
    LABEL_2X1(2.0, 1.0),
    
    /**
     * 2x1.5 inch label (compact product label)
     */
    LABEL_2X1_5(2.0, 1.5),
    
    /**
     * 3x2 inch label (medium product label)
     */
    LABEL_3X2(3.0, 2.0),
    
    /**
     * Custom size (use with custom width and height)
     */
    CUSTOM(0.0, 0.0);
    
    private final double widthInches;
    private final double heightInches;
    
    PaperSize(double widthInches, double heightInches) {
        this.widthInches = widthInches;
        this.heightInches = heightInches;
    }
    
    public double getWidthInches() {
        return widthInches;
    }
    
    public double getHeightInches() {
        return heightInches;
    }
}
