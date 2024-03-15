package qrlabel;

import qrlabel.config.QRLabelConfiguration;
import qrlabel.config.LayoutType;
import qrlabel.factory.QRCodeFactory;
import qrlabel.factory.LayoutStrategyFactory;
import qrlabel.layout.LayoutStrategy;
import com.google.zxing.WriterException;

import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * CustomQRCodeLabelPrinter - A configurable QR code label printer with multiple layout options.
 * 
 * This class provides a robust, extensible system for generating QR code labels with:
 * - Configurable paper sizes and printer DPI settings
 * - Multiple layout strategies (QR only, QR + text top/bottom, QR + rotated text left/right)
 * - Automatic space optimization to minimize paper waste
 * - High-quality image generation and printing capabilities
 * 
 * Key Features:
 * - Builder pattern configuration for easy setup
 * - Strategy pattern for layout flexibility
 * - Factory pattern for QR code generation
 * - Immutable configuration objects for thread safety
 * - Comprehensive error handling and validation
 * 
 * Usage Example:
 * <pre>
 * QRLabelConfiguration config = QRLabelConfiguration.builder()
 *     .paperWidthInches(4.0)
 *     .paperHeightInches(3.0)
 *     .printerDPI(203)
 *     .layoutType(LayoutType.TOP_BOTTOM)
 *     .includeText(true)
 *     .build();
 * 
 * CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
 * printer.setTextToEncode("https://example.com");
 * printer.saveToFile("my_qr_label.png");
 * printer.printLabel();
 * </pre>
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public class CustomQRCodeLabelPrinter implements Printable {
    
    private final QRLabelConfiguration configuration;
    private final LayoutStrategy layoutStrategy;
    private String textToEncode;
    private String leftQRText;
    private String rightQRText;
    private BufferedImage cachedLabelImage;
    
    /**
     * Creates a new CustomQRCodeLabelPrinter with the specified configuration.
     * 
     * @param configuration The configuration object containing all printer settings
     * @throws IllegalArgumentException if configuration is null
     */
    public CustomQRCodeLabelPrinter(QRLabelConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
        
        this.configuration = configuration;
        this.layoutStrategy = LayoutStrategyFactory.createLayoutStrategy(
            configuration.isIncludeText(), 
            configuration.getLayoutType()
        );
        this.textToEncode = null;
        this.cachedLabelImage = null;
    }
    
    /**
     * Sets the text to be encoded in the QR code.
     * 
     * @param text The text to encode (cannot be null or empty)
     * @throws IllegalArgumentException if text is null or empty
     */
    public void setTextToEncode(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text to encode cannot be null or empty");
        }
        
        if (!QRCodeFactory.canEncode(text)) {
            throw new IllegalArgumentException("Text cannot be encoded as QR code: " + text);
        }
        
        this.textToEncode = text;
        this.cachedLabelImage = null; // Invalidate cache
    }
    
    /**
     * Gets the currently set text to encode.
     * 
     * @return The text to encode, or null if not set
     */
    public String getTextToEncode() {
        return textToEncode;
    }
    
    /**
     * Sets the text for the left QR code in dual QR mode.
     * 
     * @param leftText The text to encode in the left QR code
     * @throws IllegalArgumentException if text is null or empty
     */
    public void setLeftQRText(String leftText) {
        if (leftText == null || leftText.trim().isEmpty()) {
            throw new IllegalArgumentException("Left QR text cannot be null or empty");
        }
        
        if (!QRCodeFactory.canEncode(leftText)) {
            throw new IllegalArgumentException("Left QR text cannot be encoded: " + leftText);
        }
        
        this.leftQRText = leftText;
        this.cachedLabelImage = null; // Invalidate cache
    }
    
    /**
     * Sets the text for the right QR code in dual QR mode.
     * 
     * @param rightText The text to encode in the right QR code
     * @throws IllegalArgumentException if text is null or empty
     */
    public void setRightQRText(String rightText) {
        if (rightText == null || rightText.trim().isEmpty()) {
            throw new IllegalArgumentException("Right QR text cannot be null or empty");
        }
        
        if (!QRCodeFactory.canEncode(rightText)) {
            throw new IllegalArgumentException("Right QR text cannot be encoded: " + rightText);
        }
        
        this.rightQRText = rightText;
        this.cachedLabelImage = null; // Invalidate cache
    }
    
    /**
     * Sets both left and right QR texts for dual QR mode.
     * 
     * @param leftText The text for the left QR code
     * @param rightText The text for the right QR code
     */
    public void setDualQRTexts(String leftText, String rightText) {
        setLeftQRText(leftText);
        setRightQRText(rightText);
    }
    
    /**
     * Gets the left QR text.
     * 
     * @return The left QR text, or null if not set
     */
    public String getLeftQRText() {
        return leftQRText;
    }
    
    /**
     * Gets the right QR text.
     * 
     * @return The right QR text, or null if not set
     */
    public String getRightQRText() {
        return rightQRText;
    }
    
    /**
     * Generates the complete label image with QR code and optional text.
     * 
     * @return A BufferedImage containing the complete label
     * @throws IllegalStateException if no text has been set
     * @throws RuntimeException if QR code generation fails
     */
    public BufferedImage generateLabel() {
        if (textToEncode == null) {
            throw new IllegalStateException("Text to encode must be set before generating label");
        }
        
        // Return cached image if available
        if (cachedLabelImage != null) {
            return cachedLabelImage;
        }
        
        try {
            // Generate QR code
            BufferedImage qrCode = QRCodeFactory.createQRCode(textToEncode, configuration);
            
            // Determine text to include based on configuration
            String textForLayout = configuration.isIncludeText() ? textToEncode : null;
            
            // For dual QR layouts, create a configuration with current dual QR texts
            QRLabelConfiguration configForLayout = configuration;
            if (configuration.getLayoutType() == LayoutType.DUAL_QR_LEFT_RIGHT || 
                configuration.getLayoutType() == LayoutType.DUAL_QR_WITH_TEXT_ON_RIGHT) {
                // Create configuration with current dual QR texts from printer instance
                configForLayout = QRLabelConfiguration.builder()
                        .paperWidthInches(configuration.getPaperWidthInches())
                        .paperHeightInches(configuration.getPaperHeightInches())
                        .printerDPI(configuration.getPrinterDPI())
                        .outputDirectory(configuration.getOutputDirectory())
                        .layoutType(configuration.getLayoutType())
                        .includeText(configuration.isIncludeText())
                        .errorCorrectionLevel(configuration.getErrorCorrectionLevel())
                        .qrMargin(configuration.getQrMargin())
                        .qrSizeRatio(configuration.getQrSizeRatio())
                        .fontFamily(configuration.getFontFamily())
                        .minFontSize(configuration.getMinFontSize())
                        .maxFontSize(configuration.getMaxFontSize())
                        .boldText(configuration.isBoldText())
                        .marginPixels(configuration.getMarginPixels())
                        .spacingBetweenElements(configuration.getSpacingBetweenElements())
                        .leftQRText(this.leftQRText)   // Pass current left QR text from printer
                        .rightQRText(this.rightQRText) // Pass current right QR text from printer
                        .dualQRMode(configuration.isDualQRMode())
                        .build();
            }
            
            // Create layout using strategy pattern
            cachedLabelImage = layoutStrategy.createLayout(qrCode, textForLayout, configForLayout);
            
            return cachedLabelImage;
            
        } catch (WriterException e) {
            throw new RuntimeException("Failed to generate QR code: " + e.getMessage(), e);
        }
    }
    
    /**
     * Saves the label image to a file in the configured output directory.
     * 
     * @param filename The filename to save (without path)
     * @throws IOException if file saving fails
     * @throws IllegalStateException if no text has been set
     */
    public void saveToFile(String filename) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }
        
        BufferedImage labelImage = generateLabel();
        
        // Ensure output directory exists
        Path outputDir = Paths.get(configuration.getOutputDirectory());
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }
        
        // Create full file path
        Path filePath = outputDir.resolve(filename);
        File outputFile = filePath.toFile();
        
        // Determine image format from filename extension
        String format = getImageFormat(filename);
        
        // Save the image
        boolean success = ImageIO.write(labelImage, format, outputFile);
        if (!success) {
            throw new IOException("Failed to save image in format: " + format);
        }
        
        System.out.println("Label saved to: " + outputFile.getAbsolutePath());
    }
    
    /**
     * Saves the label image with an auto-generated filename.
     * 
     * @return The generated filename
     * @throws IOException if file saving fails
     * @throws IllegalStateException if no text has been set
     */
    public String saveToFile() throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = String.format("qr_label_%s.png", timestamp);
        saveToFile(filename);
        return filename;
    }
    
    /**
     * Processes a batch of QR texts and generates multiple labels.
     * 
     * For DUAL_QR_LEFT_RIGHT layout: texts are processed in pairs (alternating left/right).
     * For other layouts: each text generates one label.
     * 
     * @param texts List of texts to encode as QR codes
     * @return List of generated filenames
     * @throws IOException if file operations fail
     * @throws IllegalArgumentException if texts list is invalid
     */
    public List<String> processBatch(List<String> texts) throws IOException {
        return processBatch(texts, false);
    }
    
    /**
     * Processes a batch of QR texts and generates multiple labels with optional printing.
     * 
     * @param texts List of texts to encode as QR codes
     * @param printAll Whether to print all generated labels
     * @return List of generated filenames
     * @throws IOException if file operations fail
     * @throws PrinterException if printing fails (when printAll is true)
     * @throws IllegalArgumentException if texts list is invalid
     */
    public List<String> processBatch(List<String> texts, boolean printAll) throws IOException {
        validateBatchInput(texts);
        
        List<String> generatedFilenames = new ArrayList<>();
        Map<String, Integer> filenameCounters = new HashMap<>();
        
        if (configuration.getLayoutType() == LayoutType.DUAL_QR_LEFT_RIGHT || 
            configuration.getLayoutType() == LayoutType.DUAL_QR_WITH_TEXT_ON_RIGHT) {
            // Process pairs for dual QR layouts
            generatedFilenames.addAll(processDualQRBatch(texts, printAll, filenameCounters));
        } else {
            // Process individual texts for regular layouts
            generatedFilenames.addAll(processRegularBatch(texts, printAll, filenameCounters));
        }
        
        System.out.printf("✅ Batch processing complete: %d labels generated\n", generatedFilenames.size());
        if (printAll) {
            System.out.printf("🖨️ All labels sent to printer\n");
        }
        
        return generatedFilenames;
    }
    
    /**
     * Saves a label with custom filename based on the QR text.
     * 
     * @param qrText The QR text to use for filename generation
     * @return The generated filename
     * @throws IOException if file saving fails
     */
    public String saveToFileWithCustomName(String qrText) throws IOException {
        String sanitizedText = sanitizeFilename(qrText);
        String filename = String.format("qr_label_%s.png", sanitizedText);
        
        // Handle duplicate filenames
        Path outputDir = Paths.get(configuration.getOutputDirectory());
        Path filePath = outputDir.resolve(filename);
        int counter = 1;
        while (Files.exists(filePath)) {
            filename = String.format("qr_label_%s_%d.png", sanitizedText, counter);
            filePath = outputDir.resolve(filename);
            counter++;
        }
        
        saveToFile(filename);
        return filename;
    }
    
    /**
     * Processes regular (non-dual QR) batch layouts.
     */
    private List<String> processRegularBatch(List<String> texts, boolean printAll, Map<String, Integer> filenameCounters) throws IOException {
        List<String> filenames = new ArrayList<>();
        
        for (int i = 0; i < texts.size(); i++) {
            String text = texts.get(i);
            
            try {
                // Set the text and generate the label
                setTextToEncode(text);
                
                // Generate filename with counter handling
                String filename = generateBatchFilename(text, filenameCounters);
                saveToFile(filename);
                filenames.add(filename);
                
                System.out.printf("✅ Label %d/%d: %s -> %s\n", i + 1, texts.size(), text, filename);
                
                // Print if requested
                if (printAll) {
                    try {
                        printLabelSilent();
                    } catch (PrinterException e) {
                        System.err.printf("⚠️ Failed to print label %s: %s\n", filename, e.getMessage());
                    }
                }
                
            } catch (Exception e) {
                System.err.printf("❌ Failed to process text '%s': %s\n", text, e.getMessage());
                throw new IOException("Batch processing failed at item " + (i + 1) + ": " + e.getMessage(), e);
            }
        }
        
        return filenames;
    }
    
    /**
     * Processes dual QR batch layouts (alternating left/right pairs).
     */
    private List<String> processDualQRBatch(List<String> texts, boolean printAll, Map<String, Integer> filenameCounters) throws IOException {
        List<String> filenames = new ArrayList<>();
        
        for (int i = 0; i < texts.size(); i += 2) {
            String leftText = texts.get(i);
            String rightText = (i + 1 < texts.size()) ? texts.get(i + 1) : leftText; // Use same text if odd number
            
            try {
                // Set dual QR texts
                setTextToEncode(leftText); // Required for base functionality
                setDualQRTexts(leftText, rightText);
                
                // Generate filename using both texts
                String combinedText = leftText + "_" + rightText;
                String filename = generateBatchFilename(combinedText, filenameCounters);
                saveToFile(filename);
                filenames.add(filename);
                
                int labelNumber = (i / 2) + 1;
                int totalLabels = (texts.size() + 1) / 2;
                System.out.printf("✅ Dual Label %d/%d: [%s | %s] -> %s\n", 
                    labelNumber, totalLabels, leftText, rightText, filename);
                
                // Print if requested
                if (printAll) {
                    try {
                        printLabelSilent();
                    } catch (PrinterException e) {
                        System.err.printf("⚠️ Failed to print dual label %s: %s\n", filename, e.getMessage());
                    }
                }
                
            } catch (Exception e) {
                System.err.printf("❌ Failed to process dual QR [%s | %s]: %s\n", leftText, rightText, e.getMessage());
                throw new IOException("Dual QR batch processing failed at pair " + ((i / 2) + 1) + ": " + e.getMessage(), e);
            }
        }
        
        return filenames;
    }
    
    /**
     * Validates batch input parameters.
     */
    private void validateBatchInput(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            throw new IllegalArgumentException("Texts list cannot be null or empty");
        }
        
        for (int i = 0; i < texts.size(); i++) {
            String text = texts.get(i);
            if (text == null || text.trim().isEmpty()) {
                throw new IllegalArgumentException("Text at index " + i + " cannot be null or empty");
            }
            
            if (!QRCodeFactory.canEncode(text)) {
                throw new IllegalArgumentException("Text at index " + i + " cannot be encoded as QR code: " + text);
            }
        }
    }
    
    /**
     * Sanitizes text for use in filenames by removing/replacing invalid characters.
     */
    private String sanitizeFilename(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "empty";
        }
        
        // Replace invalid filename characters with underscores
        String sanitized = text.replaceAll("[/\\\\:*?\"<>|]", "_");
        
        // Remove any remaining control characters
        sanitized = sanitized.replaceAll("\\p{Cntrl}", "");
        
        // Limit length to prevent filesystem issues (keep first 200 chars)
        if (sanitized.length() > 200) {
            sanitized = sanitized.substring(0, 200);
        }
        
        // Ensure it's not empty after sanitization
        if (sanitized.trim().isEmpty()) {
            sanitized = "sanitized_text";
        }
        
        return sanitized.trim();
    }
    
    /**
     * Generates a unique filename for batch processing with counter handling.
     */
    private String generateBatchFilename(String text, Map<String, Integer> counters) {
        String sanitizedText = sanitizeFilename(text);
        String baseFilename = "qr_label_" + sanitizedText;
        
        // Check if we've seen this base filename before
        Integer counter = counters.get(baseFilename);
        if (counter == null) {
            counters.put(baseFilename, 1);
            return baseFilename + ".png";
        } else {
            counter++;
            counters.put(baseFilename, counter);
            return baseFilename + "_" + counter + ".png";
        }
    }
    
    /**
     * Prints the label using the system's default printer.
     * 
     * @throws PrinterException if printing fails
     * @throws IllegalStateException if no text has been set
     */
    public void printLabel() throws PrinterException {
        BufferedImage labelImage = generateLabel();
        
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        
        // Set up page format for the configured label size
        PageFormat pageFormat = createPageFormat(printerJob);
        
        // Set this class as the printable
        printerJob.setPrintable(this, pageFormat);
        
        // Show print dialog
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
                System.out.println("Label sent to printer successfully!");
            } catch (PrinterException e) {
                System.err.println("Error printing label: " + e.getMessage());
                throw e;
            }
        } else {
            System.out.println("Print job cancelled.");
        }
    }
    
    /**
     * Prints the label without showing the print dialog.
     * 
     * @throws PrinterException if printing fails
     * @throws IllegalStateException if no text has been set
     */
    public void printLabelSilent() throws PrinterException {
        BufferedImage labelImage = generateLabel();
        
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = createPageFormat(printerJob);
        printerJob.setPrintable(this, pageFormat);
        
        printerJob.print();
        System.out.println("Label sent to printer successfully!");
    }
    
    /**
     * Implementation of Printable interface for printing support.
     */
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        
        if (cachedLabelImage == null) {
            try {
                generateLabel();
            } catch (Exception e) {
                throw new PrinterException("Failed to generate label for printing: " + e.getMessage());
            }
        }
        
        Graphics2D g2d = (Graphics2D) graphics;
        
        // Calculate scaling to fit the page while maintaining aspect ratio
        double scaleX = pageFormat.getImageableWidth() / cachedLabelImage.getWidth();
        double scaleY = pageFormat.getImageableHeight() / cachedLabelImage.getHeight();
        double scale = Math.min(scaleX, scaleY);
        
        // Calculate centered position
        int scaledWidth = (int) (cachedLabelImage.getWidth() * scale);
        int scaledHeight = (int) (cachedLabelImage.getHeight() * scale);
        int x = (int) ((pageFormat.getImageableWidth() - scaledWidth) / 2 + pageFormat.getImageableX());
        int y = (int) ((pageFormat.getImageableHeight() - scaledHeight) / 2 + pageFormat.getImageableY());
        
        // Draw the scaled image
        g2d.drawImage(cachedLabelImage, x, y, scaledWidth, scaledHeight, null);
        
        return PAGE_EXISTS;
    }
    
    /**
     * Gets information about the current configuration and state.
     * 
     * @return A string containing printer information
     */
    public String getInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== CustomQRCodeLabelPrinter Info ===\n");
        info.append("Configuration: ").append(configuration.toString()).append("\n");
        info.append("Layout Strategy: ").append(layoutStrategy.getLayoutName()).append("\n");
        info.append("Text to Encode: ").append(textToEncode != null ? textToEncode : "Not set").append("\n");
        info.append("Label Dimensions: ").append(configuration.getPaperWidthPixels())
            .append(" x ").append(configuration.getPaperHeightPixels()).append(" pixels\n");
        info.append("Paper Size: ").append(configuration.getPaperWidthInches())
            .append("\" x ").append(configuration.getPaperHeightInches()).append("\"\n");
        info.append("Printer DPI: ").append(configuration.getPrinterDPI()).append("\n");
        info.append("=====================================");
        return info.toString();
    }
    
    /**
     * Creates a PageFormat for printing based on the configuration.
     */
    private PageFormat createPageFormat(PrinterJob printerJob) {
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = pageFormat.getPaper();
        
        // Convert inches to points (1 inch = 72 points)
        double width = configuration.getPaperWidthInches() * 72;
        double height = configuration.getPaperHeightInches() * 72;
        
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        pageFormat.setPaper(paper);
        
        return pageFormat;
    }
    
    /**
     * Determines the image format from the filename extension.
     */
    private String getImageFormat(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "JPEG";
            case "png":
                return "PNG";
            case "bmp":
                return "BMP";
            case "gif":
                return "GIF";
            default:
                return "PNG"; // Default to PNG
        }
    }
}
