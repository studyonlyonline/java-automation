package qrlabel.examples;

import qrlabel.CustomQRCodeLabelPrinter;
import qrlabel.config.QRLabelConfiguration;
import qrlabel.config.LayoutType;
import qrlabel.config.PaperSize;
import qrlabel.config.ErrorCorrectionLevel;

import java.io.IOException;
import java.awt.print.PrinterException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Example class demonstrating the usage of CustomQRCodeLabelPrinter.
 * 
 * This class shows various configuration options and usage patterns:
 * - Different paper sizes and layouts
 * - Text inclusion options
 * - Error handling
 * - Interactive usage
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public class CustomQRCodeLabelPrinterExample {
    
    public static void main(String[] args) {
        System.out.println("=== CustomQRCodeLabelPrinter Examples ===\n");
        
        // Example 1: Basic QR-only label (4x3 inches, 203 DPI)
        // example1_BasicQROnly();
        
        // Example 2: QR with text below (Top-Bottom layout)
        // example2_QRWithTextBelow();
        
        // Example 3: QR with rotated text (Left-Right layout)
        //  example3_QRWithRotatedText();
        
        // Example 4: Compact label (2x1.5 inches)
        // example4_CompactLabel();
        
        // Example 5: High-resolution label (300 DPI)
        // example5_HighResolutionLabel();
        
        // Example 6: Compact label with text (2x1.5 inches)
        // example6_CompactLabelWithText();
        
        // Example 7: Right-half label (4x1.5 inches, print only on right half)
        // example7_RightHalfLabel();
        
        // Example 8: Left-half label (4x1.5 inches, print only on left half)
        // example8_LeftHalfLabel();
        
        // Example 9: Dual QR codes (4x1.5 inches, different QR codes on left and right)
//         example9_DualQRLabel();
        
        // NEW BATCH PROCESSING EXAMPLES
        
        // Example 10: Basic batch processing (regular layouts)
        // example10_BatchProcessing();
        
        // Example 11: Dual QR batch processing
//         example11_DualQRBatchProcessing();
        
        // Example 12: Custom filename batch processing
        // example12_CustomFilenameBatch();
        
        // Example 14: Compact 4x1 inch dual QR for chargers
        example14_Compact4x1DualQRForChargers();
        
        // Interactive example
        // interactiveExample();
    }
    
    /**
     * Example 1: Basic QR-only label
     */
    private static void example1_BasicQROnly() {
        System.out.println("--- Example 1: Basic QR-Only Label ---");
        
        try {
            // Create configuration for 4x3 inch label with QR code only
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(4.0)
                    .paperHeightInches(3.0)
                    .printerDPI(203)
                    .layoutType(LayoutType.QR_ONLY)
                    .includeText(false)
                    .outputDirectory("./examples/")
                    .qrSizeRatio(0.8)
                    .marginPixels(10)
                    .build();
            
            // Create printer and generate label
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            printer.setTextToEncode("https://github.com/example/project");
            
            // Save the label
            String filename = printer.saveToFile();
            System.out.println("✅ Basic QR-only label saved: " + filename);
            System.out.println(printer.getInfo());
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 1: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Example 2: QR with text below (Top-Bottom layout)
     */
    private static void example2_QRWithTextBelow() {
        System.out.println("--- Example 2: QR with Text Below ---");
        
        try {
            // Create configuration for QR code with text below
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(4.0)
                    .paperHeightInches(3.0)
                    .printerDPI(203)
                    .layoutType(LayoutType.TOP_BOTTOM)
                    .includeText(true)
                    .outputDirectory("./examples/")
                    .qrSizeRatio(0.7) // Smaller QR to make room for text
                    .minFontSize(12)
                    .maxFontSize(48)
                    .boldText(true)
                    .spacingBetweenElements(15)
                    .build();
            
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            printer.setTextToEncode("Product Code: ABC-123-XYZ");
            
            String filename = printer.saveToFile();
            System.out.println("✅ QR with text below saved: " + filename);
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 2: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Example 3: QR with rotated text (Left-Right layout)
     */
    private static void example3_QRWithRotatedText() {
        System.out.println("--- Example 3: QR with Rotated Text ---");
        
        try {
            // Create configuration for QR code with rotated text
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(4.0)
                    .paperHeightInches(3.0)
                    .printerDPI(203)
                    .layoutType(LayoutType.LEFT_RIGHT_ROTATED)
                    .includeText(true)
                    .outputDirectory("./examples/")
                    .qrSizeRatio(0.6) // Smaller QR to make room for rotated text
                    .fontFamily("Arial")
                    .minFontSize(10)
                    .maxFontSize(36)
                    .boldText(false)
                    .errorCorrectionLevel(ErrorCorrectionLevel.M)
                    .build();
            
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            printer.setTextToEncode("A1-R1-L2");
            
            String filename = printer.saveToFile();
            System.out.println("✅ QR with rotated text saved: " + filename);
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 3: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Example 4: Compact label (2x1.5 inches)
     */
    private static void example4_CompactLabel() {
        System.out.println("--- Example 4: Compact Label ---");
        
        try {
            // Create configuration for compact label
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(2.0)
                    .paperHeightInches(1.5)
                    .printerDPI(203)
                    .layoutType(LayoutType.QR_ONLY)
                    .includeText(true)
                    .outputDirectory("./examples/")
                    .qrSizeRatio(0.9) // Use most of the space
                    .marginPixels(5) // Smaller margins for compact labels
                    .qrMargin(1) // Minimal QR margin
                    .build();
            
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            printer.setTextToEncode("ITEM-001");
            
            String filename = printer.saveToFile();
            System.out.println("✅ Compact label saved: " + filename);
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 4: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Example 5: High-resolution label (300 DPI)
     */
    private static void example5_HighResolutionLabel() {
        System.out.println("--- Example 5: High-Resolution Label ---");
        
        try {
            // Create configuration for high-resolution printing
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(4.0)
                    .paperHeightInches(3.0)
                    .printerDPI(300) // High resolution
                    .layoutType(LayoutType.TOP_BOTTOM)
                    .includeText(true)
                    .outputDirectory("./examples/")
                    .qrSizeRatio(0.75)
                    .minFontSize(16)
                    .maxFontSize(64) // Larger fonts for high DPI
                    .boldText(true)
                    .errorCorrectionLevel(ErrorCorrectionLevel.H) // High error correction
                    .build();
            
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            printer.setTextToEncode("https://www.example.com/product/12345");
            
            String filename = printer.saveToFile();
            System.out.println("✅ High-resolution label saved: " + filename);
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 5: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Interactive example allowing user input
     */
    private static void interactiveExample() {
        System.out.println("--- Interactive Example ---");
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Get user input
            System.out.print("Enter text for QR code: ");
            String text = scanner.nextLine();
            
            if (text.trim().isEmpty()) {
                System.out.println("No text entered. Skipping interactive example.");
                return;
            }
            
            System.out.print("Include text on label? (y/n): ");
            boolean includeText = scanner.nextLine().toLowerCase().startsWith("y");
            
            LayoutType layoutType = LayoutType.QR_ONLY;
            if (includeText) {
                System.out.println("Choose layout:");
                System.out.println("1. Text below QR code");
                System.out.println("2. Text rotated 90° to the right of QR code");
                System.out.print("Enter choice (1 or 2): ");
                
                String choice = scanner.nextLine();
                if ("2".equals(choice)) {
                    layoutType = LayoutType.LEFT_RIGHT_ROTATED;
                } else {
                    layoutType = LayoutType.TOP_BOTTOM;
                }
            }
            
            // Create configuration based on user input
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(4.0)
                    .paperHeightInches(3.0)
                    .printerDPI(203)
                    .layoutType(layoutType)
                    .includeText(includeText)
                    .outputDirectory("./examples/")
                    .qrSizeRatio(includeText ? 0.7 : 0.8)
                    .minFontSize(12)
                    .maxFontSize(48)
                    .boldText(true)
                    .build();
            
            // Create and save label
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            printer.setTextToEncode(text);
            
            String filename = printer.saveToFile();
            System.out.println("✅ Interactive label saved: " + filename);
            
            // Ask about printing
            System.out.print("Print the label? (y/n): ");
            if (scanner.nextLine().toLowerCase().startsWith("y")) {
                try {
                    printer.printLabel();
                } catch (PrinterException e) {
                    System.err.println("❌ Printing failed: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error in interactive example: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Demonstrates error handling scenarios
     */
    private static void demonstrateErrorHandling() {
        System.out.println("--- Error Handling Examples ---");
        
        // Example: Invalid configuration
        try {
            QRLabelConfiguration.builder()
                    .paperWidthInches(-1.0) // Invalid
                    .paperHeightInches(3.0)
                    .printerDPI(203)
                    .build();
        } catch (IllegalArgumentException e) {
            System.out.println("✅ Caught expected error: " + e.getMessage());
        }
        
        // Example: Invalid text
        try {
            QRLabelConfiguration config = QRLabelConfiguration.createDefault4x3Config(203);
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            printer.setTextToEncode(""); // Empty text
        } catch (IllegalArgumentException e) {
            System.out.println("✅ Caught expected error: " + e.getMessage());
        }
        
        // Example: Generate without setting text
        try {
            QRLabelConfiguration config = QRLabelConfiguration.createDefault4x3Config(203);
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            printer.generateLabel(); // No text set
        } catch (IllegalStateException e) {
            System.out.println("✅ Caught expected error: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Example 6: Compact label with text (2x1.5 inches)
     */
    private static void example6_CompactLabelWithText() {
        System.out.println("--- Example 6: Compact Label with Text ---");
        
        try {
            // Create configuration for compact label with text
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(2.0)
                    .paperHeightInches(1.5)
                    .printerDPI(203)
                    .layoutType(LayoutType.TOP_BOTTOM) // Text below QR for compact space
                    .includeText(true)
                    .outputDirectory("src/main/resources/qr_labels/")
                    .qrSizeRatio(0.8) // Smaller QR to make room for text
                    .marginPixels(1) // Very small margins for compact labels
                    .spacingBetweenElements(1) // Minimal spacing
                    .fontFamily("Arial")
                    .minFontSize(5)
                    .maxFontSize(50) // Smaller fonts for compact labels
                    .boldText(true)
                    .errorCorrectionLevel(ErrorCorrectionLevel.L)
                    .build();
            
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            printer.setTextToEncode("SC-0000001");
            
            String filename = printer.saveToFile();
            System.out.println("✅ Compact label with text saved: " + filename);
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 6: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Example 7: Right-half label (4x1.5 inches, print only on right half)
     */
    private static void example7_RightHalfLabel() {
        System.out.println("--- Example 7: Right-Half Label (Left Half Blank) ---");
        
        try {
            // Create configuration for 4x1.5 inch label with printing only on right half
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(4.0)
                    .paperHeightInches(1.5)
                    .printerDPI(203)
                    .layoutType(LayoutType.RIGHT_HALF_ONLY)
                    .includeText(true)
                    .outputDirectory("src/main/resources/qr_labels/")
                    .qrSizeRatio(0.7) // Optimize for right half space
                    .marginPixels(5) // Small margins
                    .spacingBetweenElements(8) // Minimal spacing
                    .fontFamily("Arial")
                    .minFontSize(8)
                    .maxFontSize(24) // Appropriate for smaller area
                    .boldText(true)
                    .errorCorrectionLevel(ErrorCorrectionLevel.M)
                    .build();
            
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            printer.setTextToEncode("RIGHT-HALF-001");
            
            String filename = printer.saveToFile();
            System.out.println("✅ Right-half label saved: " + filename);
            System.out.println("📝 Left 2\" is blank, right 2\" contains QR + text");
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 7: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Example 8: Left-half label (4x1.5 inches, print only on left half)
     */
    private static void example8_LeftHalfLabel() {
        System.out.println("--- Example 8: Left-Half Label (Right Half Blank) ---");
        
        try {
            // Create configuration for 4x1.5 inch label with printing only on left half
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(4.0)
                    .paperHeightInches(1.5)
                    .printerDPI(203)
                    .layoutType(LayoutType.LEFT_HALF_ONLY)
                    .includeText(true)
                    .outputDirectory("src/main/resources/qr_labels/")
                    .qrSizeRatio(0.7) // Optimize for left half space
                    .marginPixels(5) // Small margins
                    .spacingBetweenElements(8) // Minimal spacing
                    .fontFamily("Arial")
                    .minFontSize(8)
                    .maxFontSize(24) // Appropriate for smaller area
                    .boldText(true)
                    .errorCorrectionLevel(ErrorCorrectionLevel.M)
                    .build();
            
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            printer.setTextToEncode("LEFT-HALF-001");
            
            String filename = printer.saveToFile();
            System.out.println("✅ Left-half label saved: " + filename);
            System.out.println("📝 Right 2\" is blank, left 2\" contains QR + text");
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 8: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Example 9: Dual QR codes (4x1.5 inches, different QR codes on left and right)
     */
    private static void example9_DualQRLabel() {
        System.out.println("--- Example 9: Dual QR Codes (Left & Right Different) ---");
        
        try {
            // Create configuration for 4x1.5 inch label with dual QR codes
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(4.0)
                    .paperHeightInches(1.5)
                    .printerDPI(203)
                    .layoutType(LayoutType.DUAL_QR_LEFT_RIGHT)
                    .includeText(true)
                    .outputDirectory("src/main/resources/qr_labels/")
                    .qrSizeRatio(0.8) // Smaller QR codes to fit both
                    .marginPixels(3) // Minimal margins
                    .spacingBetweenElements(5) // Minimal spacing
                    .fontFamily("Arial")
                    .minFontSize(16)
                    .maxFontSize(30) // Smaller fonts for dual layout
                    .boldText(true)
                    .errorCorrectionLevel(ErrorCorrectionLevel.M)
                    .leftQRText("SE/0051")
                    .rightQRText("A1-R1-L1")
                    .dualQRMode(true)
                    .build();
            
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            
            // For dual QR mode, set main text and both dual texts
            printer.setTextToEncode("DUAL-QR-MAIN"); // Required for base functionality
            printer.setDualQRTexts("LEFT-QR-DATA-001", "RIGHT-QR-DATA-002");
            
            String filename = printer.saveToFile();
            System.out.println("✅ Dual QR label saved: " + filename);
            System.out.println("📝 Left QR: LEFT-QR-DATA-001");
            System.out.println("📝 Right QR: RIGHT-QR-DATA-002");
            System.out.println("🎯 Two different scannable QR codes on one label!");
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 9: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Example 10: Basic batch processing for regular layouts
     */
    private static void example10_BatchProcessing() {
        System.out.println("--- Example 10: Basic Batch Processing ---");
        
        try {
            // Create configuration for TOP_BOTTOM layout
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(4.0)
                    .paperHeightInches(3.0)
                    .printerDPI(203)
                    .layoutType(LayoutType.TOP_BOTTOM)
                    .includeText(true)
                    .outputDirectory("src/main/resources/qr_labels/")
                    .qrSizeRatio(0.7)
                    .minFontSize(12)
                    .maxFontSize(48)
                    .boldText(true)
                    .spacingBetweenElements(10)
                    .build();
            
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            
            // Create a list of QR texts to process
            List<String> qrTexts = Arrays.asList(
                "A1-R1-L1",
                "A1-R1-L2", 
                "A1-R1-L3",
                "B2-R2-L1",
                "B2-R2-L2"
            );
            
            System.out.printf("🚀 Processing batch of %d QR codes...\n", qrTexts.size());
            
            // Process the batch
            List<String> filenames = printer.processBatch(qrTexts);
            
            System.out.println("\n📋 Generated Files:");
            for (int i = 0; i < filenames.size(); i++) {
                System.out.printf("   %d. %s (QR: %s)\n", i + 1, filenames.get(i), qrTexts.get(i));
            }
            
            System.out.printf("\n✅ Batch processing complete! %d labels generated with custom filenames.\n", filenames.size());
            System.out.println("📝 Files saved with format: qr_label_<qr_text>.png");
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 10: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * Example 11: Dual QR batch processing (alternating pairs)
     */
    private static void example11_DualQRBatchProcessing() {
        System.out.println("--- Example 11: Dual QR Batch Processing ---");
        
        try {
            // Create configuration for dual QR layout
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(4.0)
                    .paperHeightInches(1.5)
                    .printerDPI(203)
                    .layoutType(LayoutType.DUAL_QR_LEFT_RIGHT)
                    .includeText(true)
                    .outputDirectory("src/main/resources/qr_labels/")
                    .qrSizeRatio(0.8)
                    .marginPixels(3)
                    .spacingBetweenElements(5)
                    .fontFamily("Arial")
                    .minFontSize(16)
                    .maxFontSize(30)
                    .boldText(true)
                    .errorCorrectionLevel(ErrorCorrectionLevel.M)
                    .build();
            
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            
            // Create a list of QR texts (will be paired alternately)
            List<String> qrTexts = Arrays.asList(
                "SE/0038",
                "SE/0039"
            );
            
            System.out.printf("🚀 Processing dual QR batch of %d texts (%d labels)...\n", 
                qrTexts.size(), (qrTexts.size() + 1) / 2);
            
            // Show pairing logic
            System.out.println("\n📊 Pairing Logic:");
            for (int i = 0; i < qrTexts.size(); i += 2) {
                String left = qrTexts.get(i);
                String right = (i + 1 < qrTexts.size()) ? qrTexts.get(i + 1) : left;
                int labelNumber = (i / 2) + 1;
                System.out.printf("   Label %d: [%s | %s]\n", labelNumber, left, right);
            }
            
            // Process the batch
            List<String> filenames = printer.processBatch(qrTexts);
            
            System.out.println("\n📋 Generated Dual QR Files:");
            for (int i = 0; i < filenames.size(); i++) {
                System.out.printf("   %d. %s\n", i + 1, filenames.get(i));
            }
            
            System.out.printf("\n✅ Dual QR batch processing complete! %d dual labels generated.\n", filenames.size());
            System.out.println("🎯 Each label contains two different scannable QR codes!");
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 11: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * Example 12: Custom filename batch processing with different layouts
     */
    private static void example12_CustomFilenameBatch() {
        System.out.println("--- Example 12: Custom Filename Batch Processing ---");
        
        try {
            // Test with QR_ONLY layout
            System.out.println("🔹 Testing QR_ONLY layout:");
            testBatchLayout(LayoutType.QR_ONLY, Arrays.asList("ITEM/001", "ITEM/002", "SPECIAL:CHARS*TEST"));
            
            // Test with LEFT_RIGHT_ROTATED layout
            System.out.println("\n🔹 Testing LEFT_RIGHT_ROTATED layout:");
            testBatchLayout(LayoutType.LEFT_RIGHT_ROTATED, Arrays.asList("ROT-001", "ROT-002"));
            
            // Test with RIGHT_HALF_ONLY layout
            System.out.println("\n🔹 Testing RIGHT_HALF_ONLY layout:");
            testBatchLayout(LayoutType.RIGHT_HALF_ONLY, Arrays.asList("RH-001", "RH-002"));
            
            System.out.println("\n✅ All layout types tested successfully!");
            System.out.println("📝 Notice how invalid filename characters are sanitized:");
            System.out.println("   • '/' becomes '_'");
            System.out.println("   • ':' becomes '_'");
            System.out.println("   • '*' becomes '_'");
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 12: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * Helper method to test batch processing with different layouts
     */
    private static void testBatchLayout(LayoutType layoutType, List<String> texts) throws IOException {
        QRLabelConfiguration config = QRLabelConfiguration.builder()
                .paperWidthInches(4.0)
                .paperHeightInches(3.0)
                .printerDPI(203)
                .layoutType(layoutType)
                .includeText(true)
                .outputDirectory("src/main/resources/qr_labels/")
                .qrSizeRatio(0.7)
                .build();
        
        CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
        List<String> filenames = printer.processBatch(texts);
        
        System.out.printf("   Generated %d files: %s\n", filenames.size(), String.join(", ", filenames));
    }
    
    /**
     * Example 13: Batch processing with printing demonstration
     */
    private static void example13_BatchWithPrinting() {
        System.out.println("--- Example 13: Batch Processing with Printing ---");
        
        try {
            QRLabelConfiguration config = QRLabelConfiguration.builder()
                    .paperWidthInches(2.0)
                    .paperHeightInches(1.5)
                    .printerDPI(203)
                    .layoutType(LayoutType.TOP_BOTTOM)
                    .includeText(true)
                    .outputDirectory("src/main/resources/qr_labels/")
                    .qrSizeRatio(0.8)
                    .marginPixels(1)
                    .spacingBetweenElements(1)
                    .build();
            
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            
            List<String> qrTexts = Arrays.asList("PRINT-TEST-001", "PRINT-TEST-002");
            
            System.out.printf("🖨️ Processing batch with printing enabled...\n");
            System.out.println("⚠️ Note: Printing may fail if no printer is available - this is expected in demo mode");
            
            // Process with printing enabled
            List<String> filenames = printer.processBatch(qrTexts, true);
            
            System.out.printf("✅ Batch with printing complete! %d labels processed.\n", filenames.size());
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 13: " + e.getMessage());
            // This is expected if no printer is available
            System.out.println("ℹ️ Printing errors are normal in development environments without printers");
        }
        
        System.out.println();
    }
    
    /**
     * Example 14: Compact 4x1 inch dual QR labels for chargers (dirt/wear resistant)
     */
    private static void example14_Compact4x1DualQRForChargers() {
        System.out.println("--- Example 14: Compact 4x1 Dual QR for Chargers ---");
        
        try {
            // Use the new factory method for compact 4x1 dual QR configuration
            QRLabelConfiguration config = QRLabelConfiguration.createCompact4x1DualQRConfig(203);
            
            CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
            
            // Charger ID batch processing with alternating pairs
            List<String> chargerIds = Arrays.asList(
//                    "B-SW-0001",
//                    "B-SW-0002",
//                    "B-SW-0003",
//                    "B-SW-0004",
//                    "B-SW-0005",
//                    "B-SW-0006",
//                    "B-SW-0007",
//                    "B-SW-0008",
//                    "B-SW-0009",
//                    "B-SW-0010",
//                    "B-SW-0011",
//                    "B-SW-0012",
//                    "B-SW-0013",
//                    "B-SW-0014",
//                    "B-SW-0015",
//                    "B-SW-0016",
//                    "B-SW-0017",
//                    "B-SW-0018",
//                    "B-SW-0019",
//                    "B-SW-0020",
//                    "B-SW-0021",
//                    "B-SW-0022",
//                    "B-SW-0023",
//                    "B-SW-0024",
//                    "B-SW-0025",
//                    "B-SW-0026",
//                    "B-SW-0027",
//                    "B-SW-0028",
//                    "B-SW-0029",
//                    "B-SW-0030",
//                    "B-SW-0031",
//                    "B-SW-0032",
//                    "B-SW-0033",
//                    "B-SW-0034",
//                    "B-SW-0035",
//                    "B-SW-0036",
//                    "B-SW-0037",
//                    "B-SW-0038",
//                    "B-SW-0039",
//                    "B-SW-0040",
//                    "B-SW-0041",
//                    "B-SW-0042",
//                    "B-SW-0043",
//                    "B-SW-0044",
//                    "B-SW-0045",
//                    "B-SW-0046",
//                    "B-SW-0047",
//                    "B-SW-0048",
//                    "B-SW-0049",
//                    "B-SW-0050",
//                    "B-SW-0051",
//                    "B-SW-0052",
//                    "B-SW-0053",
//                    "B-SW-0054",
//                    "B-SW-0055",
//                    "B-SW-0056",
//                    "B-SW-0057",
//                    "B-SW-0058",
//                    "B-SW-0059",
//                    "B-SW-0060",
//                    "B-SW-0061",
//                    "B-SW-0062",
//                    "B-SW-0063",
//                    "B-SW-0064",
//                    "B-SW-0065",
//                    "B-SW-0066",
//                    "B-SW-0067",
//                    "B-SW-0068",
//                    "B-SW-0069",
//                    "B-SW-0070",
//                    "B-SW-0071",
//                    "B-SW-0072",
//                    "B-SW-0073",
//                    "B-SW-0074",
//                    "B-SW-0075",
//                    "B-SW-0076",
//                    "B-SW-0077",
//                    "B-SW-0078",
//                    "B-SW-0079",
//                    "B-SW-0080",
//                    "B-SW-0081",
//                    "B-SW-0082",
//                    "B-SW-0083",
//                    "B-SW-0084",
//                    "B-SW-0085",
//                    "B-SW-0086",
//                    "B-SW-0087",
//                    "B-SW-0088",
//                    "B-SW-0089",
//                    "B-SW-0090",
//                    "B-SW-0091",
//                    "B-SW-0092",
//                    "B-SW-0093",
//                    "B-SW-0094",
//                    "B-SW-0095",
//                    "B-SW-0096",
//                    "B-SW-0097",
//                    "B-SW-0098",
//                    "B-SW-0099"
                    "SE/0201",
                    "SE/0202",
                    "SE/0203",
                    "SE/0204",
                    "SE/0205",
                    "SE/0206",
                    "SE/0207",
                    "SE/0208",
                    "SE/0209",
                    "SE/0210",
                    "SE/0211",
                    "SE/0212",
                    "SE/0213",
                    "SE/0214",
                    "SE/0215",
                    "SE/0216",
                    "SE/0217",
                    "SE/0218",
                    "SE/0219",
                    "SE/0220",
                    "SE/0221",
                    "SE/0222",
                    "SE/0223",
                    "SE/0224",
                    "SE/0225",
                    "SE/0226",
                    "SE/0227",
                    "SE/0228",
                    "SE/0229",
                    "SE/0230",
                    "SE/0231",
                    "SE/0232",
                    "SE/0233",
                    "SE/0234",
                    "SE/0235",
                    "SE/0236",
                    "SE/0237",
                    "SE/0238",
                    "SE/0239",
                    "SE/0240",
                    "SE/0241",
                    "SE/0242",
                    "SE/0243",
                    "SE/0244",
                    "SE/0245",
                    "SE/0246",
                    "SE/0247",
                    "SE/0248",
                    "SE/0249",
                    "SE/0250"


//                "B-Orient /Backlit Panl /Eternal /3CT /Sq /10w",
//                "B-Spare /SJ /Charging Cable /Heavy 4mm/3 Pin",
//                 "B-Ceat /Tyre /3.10-42J(Tubeless)",
//                 "B-Orient /LED Bulb /B22 /CW /18w"
                // "B - Spare / ASD - BJ / Horn",
                // "B - Spare / Benling / Aura Motor Disc Type",
                // "B - Spare / Benling / Controller - Old Model",
                // "B - Spare / Benling / Display Meter",
                // "B - Spare / Chager / SRP / 72v 10A",
                // "B - Spare / Charger / SRP / 48v 6Amp",
                // "B - Spare / Charger / SRP / 60v 6Amp",
                // "B - Spare / Charger / SRP / 72v 6Amp",
                // "B - Spare / MI / Aura Front Mudguard(NP)",
                // "B - Spare / MI / Aura Front Panel (NP)",
                // "B - Spare / MI / Aura Lockset",
                // "B - Spare / MI / Falcon Lockset",
                // "B - Spare / MI / Motor Sensor",
                // "B - Spare / Prakant / Throotle / Aura Type",
                // "B - Spare / Prakant / Throttle Grip",
                // "B - Spare / RAS - BJ / Horn",
                // "B - Spare / RAS - BJ / LED Bulb 3in1",
                // "B - Spare / SJ / Brake Cable / S / 80 inch",
                // "B - Spare / SJ / Brake Cable / S / 82 inch",
                // "B - Spare / SJ / Brake Shoes",
                // "B - Spare / SJ / Charging Socket",
                // "B - Spare / SJ / Chrome Cap",
                // "B - Spare / SJ / Cone Set",
                // "B - Spare / SJ / CY / Lock",
                // "B - Spare / SJ / D / Brake Cable / 82 inch",
                // "B - Spare / SJ / Disc Pad",
                // "B - Spare / SJ / Disc Patti",
                // "B - Spare / SJ / Disc Pump + Sensor + Lever / LH",
                // "B - Spare / SJ / Disc Pump + Sensor + Lever / RH",
                // "B - Spare / SJ / Drum Plate / 110mm",
                // "B - Spare / SJ / Emergency Switch",
                // "B - Spare / SJ / Horn Switch / New Type",
                // "B - Spare / SJ / Horn Switch / Old Type",
                // "B - Spare / SJ / Komaki Throttle ( Double Push)",
                // "B - Spare / SJ / Ladies Footrest",
                // "B - Spare / SJ / LED Bulb 2 Pin",
                // "B - Spare / SJ / Mirror Set",
                // "B - Spare / SJ / Motor Wire / 48 inch",
                // "B - Spare / SJ / New / Metal Cap With Inner",
                // "B - Spare / SJ / NYX / LH / Lever with Sensor",
                // "B - Spare / SJ / NYX / RH / Lever with Sensor",
                // "B - Spare / SJ / Ola Disc Pad / Front",
                // "B - Spare / SJ / Ola Disc Pad / Rear",
                // "B - Spare / SJ / RD / Brake Sensor / LH",
                // "B - Spare / SJ / Rear Tyre Mudguard",
                // "B - Spare / SJ / Side Mirror Set (HS)",
                // "B - Spare / SJ / Sq / Brake Sensor / LH",
                // "B - Spare / SJ / Switch / Emergency",
                // "B - Spare / SJ / Switch / Headlight",
                // "B - Spare / SJ / Switch / Indicator",
                // "B - Spare / SJ / Throotle 1+2+3 / F&R",
                // "B - Spare / SJ / Throttle",
                // "Extra - Spare / Disc Brake Lever / RH",
                // "Extra - Spare / Old / Metal Cap With Inner",
                // "Extra -Spare / Old / Plastic Cap With Inner",
                // "Extra -Spare / Tri / Brake Sensor / LH",
                // "Extra -Spare / Tri / Brake Sensor / RH",
                // "Hardware / Patti",
                // "M - SKF / Bearing / 6201 - 2Z",
                // "M - SKF / Bearing / 6204 - 2Z",
                // "B - Crompton / LED Bulb / B22 / CW / 9w",
                // "B - L&T / MCB / SP / 10A",
                // "B - L&T / MCB / SP / 16A",
                // "B - L&T / MCB / SP / 20A",
                // "B - L&T / MCB / SP / 25A",
                // "B - L&T / MCB / SP / 32A"


                    // "B-Spare /SJ /Charging Cable /Heavy 4mm/3 Pin",
                    // "B-Ceat /Tyre /3.10-42J (Tubeless)",
                    // "B-Spare /ASD-BJ /Horn",
                    // "B-Spare /Benling /Aura Motor Disc Type",
                    // "B-Spare /Benling /Controller-Old Model",
                    // "B-Spare /Benling /Display Meter",
                    // "B-Spare /Chager /SRP /72v 10A",
                    // "B-Spare /Charger /SRP /48v 6Amp",
                    // "B-Spare /Charger /SRP /60v 6Amp",
                    // "B-Spare /Charger /SRP /72v 6Amp",
                    // "B-Spare /MI /Aura Front Mudguard(NP)",
                    // "B-Spare /MI /Aura Front Panel (NP)",
                    // "B-Spare /MI /Aura Lockset",
                    // "B-Spare /MI /Falcon Lockset",
                    // "B-Spare /MI /Motor Sensor",
                    // "B-Spare /Prakant /Throotle /Aura Type",
                    // "B-Spare /Prakant /Throttle Grip",
                    // "B-Spare /RAS-BJ /Horn",
                    // "B-Spare /RAS-BJ /LED Bulb 3in1",
                    // "B-Spare /SJ /Brake Shoes",
                    // "B-Spare /SJ /Charging Socket",
                    // "B-Spare /SJ /Chrome Cap",
                    // "B-Spare /SJ /Cone Set",
                    // "B-Spare /SJ /CY /Lock",
                    // "B-Spare /SJ /D /Brake Cable /82 inch",
                    // "B-Spare /SJ /Disc Pad",
                    // "B-Spare /SJ /Disc Patti",
                    // "B-Spare /SJ /Disc Pump + Sensor + Lever /LH",
                    // "B-Spare /SJ /Disc Pump + Sensor + Lever /RH",
                    // "B-Spare /SJ /Drum Plate /110mm",
                    // "B-Spare /SJ /Emergency Switch",
                    // "B-Spare /SJ /Horn Switch /New Type",
                    // "B-Spare /SJ /Horn Switch /Old Type",
                    // "B-Spare /SJ /Komaki Throttle ( Double Push)",
                    // "B-Spare /SJ /Ladies Footrest",
                    // "B-Spare /SJ /LED Bulb 2 Pin",
                    // "B-Spare /SJ /Mirror Set",
                    // "B-Spare /SJ /Motor Wire /48 inch",
                    // "B-Spare /SJ /New /Metal Cap With Inner",
                    // "B-Spare /SJ /NYX /LH /Lever with Sensor",
                    // "B-Spare /SJ /NYX /RH /Lever with Sensor",
                    // "B-Spare /SJ /Ola Disc Pad /Front",
                    // "B-Spare /SJ /Ola Disc Pad /Rear",
                    // "B-Spare /SJ /RD /Brake Sensor /LH",
                    // "B-Spare /SJ /Rear Tyre Mudguard",
                    // "B-Spare /SJ /Side Mirror Set (HS)",
                    // "B-Spare /SJ /Sq /Brake Sensor /LH",
                    // "B-Spare /SJ /Switch /Emergency",
                    // "B-Spare /SJ /Switch /Headlight",
                    // "B-Spare /SJ /Switch /Indicator",
                    // "B-Spare /SJ /Throotle 1+2+3 /F&R",
                    // "B-Spare /SJ /Throttle",
//                    "Extra-Spare /Disc Brake Lever /RH",
//                    "Extra-Spare /Old /Metal Cap With Inner"
                    // "Extra-Spare /Old /Plastic Cap With Inner",
                    // "Extra-Spare /Tri /Brake Sensor /LH",
                    // "Extra-Spare /Tri /Brake Sensor /RH",
                    // "Hardware /Patti",
                    // "M-SKF /Bearing /6201-2Z",
                    // "M-SKF /Bearing /6204-2Z",
                    // "B-Crompton /LED Bulb /B22 /CW /9w",
                    // "B-L&T /MCB /SP /10A",
                    // "B-L&T /MCB /SP /16A",
                    // "B-L&T /MCB /SP /20A",
                    // "B-L&T /MCB /SP /25A",
                    // "B-L&T /MCB /SP /32A"
            );
            
            System.out.printf("🔌 Processing compact charger labels: %d codes (%d labels)...\n", 
                chargerIds.size(), (chargerIds.size() + 1) / 2);
            
            // Show the pairing strategy
            System.out.println("\n📊 Charger Label Pairing:");
            for (int i = 0; i < chargerIds.size(); i += 2) {
                String left = chargerIds.get(i);
                String right = (i + 1 < chargerIds.size()) ? chargerIds.get(i + 1) : left;
                int labelNumber = (i / 2) + 1;
                System.out.printf("   Label %d: [%s | %s]\n", labelNumber, left, right);
            }
            
            // Process the batch using existing alternating pairs logic
            List<String> filenames = printer.processBatch(chargerIds);
            
            System.out.println("\n📋 Generated Compact Charger Labels:");
            for (int i = 0; i < filenames.size(); i++) {
                System.out.printf("   %d. %s\n", i + 1, filenames.get(i));
            }
            
            System.out.printf("\n✅ Compact dual QR batch complete! %d charger labels (4×1 inch) generated.\n", filenames.size());
            System.out.println("🏭 Optimized for charger environments with Level M error correction (15% dirt tolerance)");
            System.out.println("📏 Each half: 2×1 inch area with 85% QR coverage and dirt-resistant design");
            
        } catch (Exception e) {
            System.err.println("❌ Error in Example 14: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }
}
