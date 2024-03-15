package qrlabel.config;

/**
 * Immutable configuration class for QR code label generation.
 * Contains all configurable parameters for paper size, printer settings,
 * layout options, and text formatting.
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public final class QRLabelConfiguration {
    
    // Paper and Printer Settings
    private final double paperWidthInches;
    private final double paperHeightInches;
    private final int printerDPI;
    private final String outputDirectory;
    
    // Layout Settings
    private final LayoutType layoutType;
    private final boolean includeText;
    
    // QR Code Settings
    private final ErrorCorrectionLevel errorCorrectionLevel;
    private final int qrMargin;
    private final double qrSizeRatio;
    
    // Text Settings
    private final String fontFamily;
    private final int minFontSize;
    private final int maxFontSize;
    private final boolean boldText;
    
    // Margins and Spacing
    private final int marginPixels;
    private final int spacingBetweenElements;
    
    // Dual QR Code Settings
    private final String leftQRText;
    private final String rightQRText;
    private final boolean dualQRMode;
    
    /**
     * Private constructor - use Builder to create instances
     */
    private QRLabelConfiguration(Builder builder) {
        this.paperWidthInches = builder.paperWidthInches;
        this.paperHeightInches = builder.paperHeightInches;
        this.printerDPI = builder.printerDPI;
        this.outputDirectory = builder.outputDirectory;
        this.layoutType = builder.layoutType;
        this.includeText = builder.includeText;
        this.errorCorrectionLevel = builder.errorCorrectionLevel;
        this.qrMargin = builder.qrMargin;
        this.qrSizeRatio = builder.qrSizeRatio;
        this.fontFamily = builder.fontFamily;
        this.minFontSize = builder.minFontSize;
        this.maxFontSize = builder.maxFontSize;
        this.boldText = builder.boldText;
        this.marginPixels = builder.marginPixels;
        this.spacingBetweenElements = builder.spacingBetweenElements;
        this.leftQRText = builder.leftQRText;
        this.rightQRText = builder.rightQRText;
        this.dualQRMode = builder.dualQRMode;
    }
    
    // Getters
    public double getPaperWidthInches() { return paperWidthInches; }
    public double getPaperHeightInches() { return paperHeightInches; }
    public int getPrinterDPI() { return printerDPI; }
    public String getOutputDirectory() { return outputDirectory; }
    public LayoutType getLayoutType() { return layoutType; }
    public boolean isIncludeText() { return includeText; }
    public ErrorCorrectionLevel getErrorCorrectionLevel() { return errorCorrectionLevel; }
    public int getQrMargin() { return qrMargin; }
    public double getQrSizeRatio() { return qrSizeRatio; }
    public String getFontFamily() { return fontFamily; }
    public int getMinFontSize() { return minFontSize; }
    public int getMaxFontSize() { return maxFontSize; }
    public boolean isBoldText() { return boldText; }
    public int getMarginPixels() { return marginPixels; }
    public int getSpacingBetweenElements() { return spacingBetweenElements; }
    public String getLeftQRText() { return leftQRText; }
    public String getRightQRText() { return rightQRText; }
    public boolean isDualQRMode() { return dualQRMode; }
    
    /**
     * Calculated property: Paper width in pixels
     */
    public int getPaperWidthPixels() {
        return (int) (paperWidthInches * printerDPI);
    }
    
    /**
     * Calculated property: Paper height in pixels
     */
    public int getPaperHeightPixels() {
        return (int) (paperHeightInches * printerDPI);
    }
    
    /**
     * Builder class for creating QRLabelConfiguration instances
     */
    public static class Builder {
        // Required parameters
        private double paperWidthInches;
        private double paperHeightInches;
        private int printerDPI;
        
        // Optional parameters with defaults
        private String outputDirectory = "./qr_labels/";
        private LayoutType layoutType = LayoutType.QR_ONLY;
        private boolean includeText = false;
        private ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        private int qrMargin = 0;
        private double qrSizeRatio = 0.8;
        private String fontFamily = "Arial";
        private int minFontSize = 12;
        private int maxFontSize = 72;
        private boolean boldText = false;
        private int marginPixels = 10;
        private int spacingBetweenElements = 10;
        
        // Dual QR Code Settings
        private String leftQRText = null;
        private String rightQRText = null;
        private boolean dualQRMode = false;
        
        public Builder paperWidthInches(double paperWidthInches) {
            this.paperWidthInches = paperWidthInches;
            return this;
        }
        
        public Builder paperHeightInches(double paperHeightInches) {
            this.paperHeightInches = paperHeightInches;
            return this;
        }
        
        public Builder printerDPI(int printerDPI) {
            this.printerDPI = printerDPI;
            return this;
        }
        
        public Builder outputDirectory(String outputDirectory) {
            this.outputDirectory = outputDirectory;
            return this;
        }
        
        public Builder layoutType(LayoutType layoutType) {
            this.layoutType = layoutType;
            return this;
        }
        
        public Builder includeText(boolean includeText) {
            this.includeText = includeText;
            return this;
        }
        
        public Builder errorCorrectionLevel(ErrorCorrectionLevel errorCorrectionLevel) {
            this.errorCorrectionLevel = errorCorrectionLevel;
            return this;
        }
        
        public Builder qrMargin(int qrMargin) {
            this.qrMargin = qrMargin;
            return this;
        }
        
        public Builder qrSizeRatio(double qrSizeRatio) {
            this.qrSizeRatio = qrSizeRatio;
            return this;
        }
        
        public Builder fontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
            return this;
        }
        
        public Builder minFontSize(int minFontSize) {
            this.minFontSize = minFontSize;
            return this;
        }
        
        public Builder maxFontSize(int maxFontSize) {
            this.maxFontSize = maxFontSize;
            return this;
        }
        
        public Builder boldText(boolean boldText) {
            this.boldText = boldText;
            return this;
        }
        
        public Builder marginPixels(int marginPixels) {
            this.marginPixels = marginPixels;
            return this;
        }
        
        public Builder spacingBetweenElements(int spacingBetweenElements) {
            this.spacingBetweenElements = spacingBetweenElements;
            return this;
        }
        
        public Builder leftQRText(String leftQRText) {
            this.leftQRText = leftQRText;
            return this;
        }
        
        public Builder rightQRText(String rightQRText) {
            this.rightQRText = rightQRText;
            return this;
        }
        
        public Builder dualQRMode(boolean dualQRMode) {
            this.dualQRMode = dualQRMode;
            return this;
        }
        
        public QRLabelConfiguration build() {
            // Validation
            if (paperWidthInches <= 0 || paperHeightInches <= 0) {
                throw new IllegalArgumentException("Paper dimensions must be positive");
            }
            if (printerDPI <= 0) {
                throw new IllegalArgumentException("Printer DPI must be positive");
            }
            if (outputDirectory == null || outputDirectory.trim().isEmpty()) {
                throw new IllegalArgumentException("Output directory cannot be null or empty");
            }
            
            return new QRLabelConfiguration(this);
        }
    }
    
    /**
     * Creates a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Factory method to create a configuration for common 4x3 inch labels
     */
    public static QRLabelConfiguration createDefault4x3Config(int printerDPI) {
        return QRLabelConfiguration.builder()
                .paperWidthInches(4.0)
                .paperHeightInches(3.0)
                .printerDPI(printerDPI)
                .build();
    }
    
    /**
     * Factory method to create a configuration for 2x1.5 inch labels
     */
    public static QRLabelConfiguration createCompact2x1_5Config(int printerDPI) {
        return QRLabelConfiguration.builder()
                .paperWidthInches(2.0)
                .paperHeightInches(1.5)
                .printerDPI(printerDPI)
                .qrSizeRatio(0.9)
                .marginPixels(5)
                .build();
    }
    
    /**
     * Factory method for compact 4x1 inch dual QR labels optimized for harsh environments
     * (chargers, industrial use with dirt/wear tolerance) with text positioned to the right of each QR
     */
    public static QRLabelConfiguration createCompact4x1DualQRConfig(int printerDPI) {
        return QRLabelConfiguration.builder()
                .paperWidthInches(4.0)
                .paperHeightInches(1.0)
                .printerDPI(printerDPI)
                .layoutType(LayoutType.DUAL_QR_WITH_TEXT_ON_RIGHT)
                .includeText(true)
                .outputDirectory("src/main/resources/qr_labels/")
                .qrSizeRatio(0.85)
                .marginPixels(0)
                .spacingBetweenElements(1)
                .qrMargin(0)
                //Use 14,28 as config for printing SE labels
                .minFontSize(14)
                .maxFontSize(28)
                //Use 8,16 as config for printing spare names text
//                .minFontSize(8)
//                .maxFontSize(16)
                .boldText(true)
                .errorCorrectionLevel(ErrorCorrectionLevel.M)
                .build();
    }
    
    @Override
    public String toString() {
        return String.format(
            "QRLabelConfiguration{paperSize=%.1f\"x%.1f\", dpi=%d, layout=%s, includeText=%s, outputDir='%s'}",
            paperWidthInches, paperHeightInches, printerDPI, layoutType, includeText, outputDirectory
        );
    }
}
