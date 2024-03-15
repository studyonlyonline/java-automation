# CustomQRCodeLabelPrinter

A robust, extensible Java library for generating configurable QR code labels with multiple layout options, optimized for various paper sizes and printer resolutions.

## Features

### 🎯 **Core Capabilities**
- **Configurable QR Code Generation**: Standard QR codes with customizable error correction levels
- **Multiple Layout Strategies**: QR-only, QR with text below, QR with rotated text
- **Paper Size Flexibility**: Support for various label sizes (4x3", 2x1.5", custom sizes)
- **DPI Configuration**: Configurable printer resolution (203 DPI, 300 DPI, etc.)
- **Space Optimization**: Automatic sizing to minimize paper waste
- **High-Quality Output**: Anti-aliased rendering for crisp printing

### 🏗️ **Architecture & Design Patterns**
- **Strategy Pattern**: Pluggable layout algorithms
- **Builder Pattern**: Fluent configuration API
- **Factory Pattern**: Centralized QR code and layout creation
- **Immutable Configuration**: Thread-safe configuration objects
- **Lombok Integration**: Reduced boilerplate code

### 📐 **Layout Options**

#### 1. QR Code Only (`LayoutType.QR_ONLY`)
- Centered QR code maximizing available space
- Ideal for simple item tracking or URLs

#### 2. QR Code + Text Below (`LayoutType.TOP_BOTTOM`)
- QR code at the top, text underneath
- Automatic font sizing and text wrapping
- Perfect for product labels with descriptions

#### 3. QR Code + Rotated Text (`LayoutType.LEFT_RIGHT_ROTATED`)
- QR code on the left, text rotated 90° on the right
- Optimal for narrow labels or space-constrained applications

## Quick Start

### Basic Usage

```java
// Create configuration
QRLabelConfiguration config = QRLabelConfiguration.builder()
    .paperWidthInches(4.0)
    .paperHeightInches(3.0)
    .printerDPI(203)
    .layoutType(LayoutType.TOP_BOTTOM)
    .includeText(true)
    .outputDirectory("./labels/")
    .build();

// Create printer and generate label
CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
printer.setTextToEncode("https://example.com/product/12345");

// Save and print
printer.saveToFile("product_label.png");
printer.printLabel();
```

### Using Predefined Paper Sizes

```java
QRLabelConfiguration config = QRLabelConfiguration.builder()
    .paperSize(PaperSize.LABEL_4X3)  // Predefined 4x3 inch label
    .printerDPI(203)
    .layoutType(LayoutType.LEFT_RIGHT_ROTATED)
    .includeText(true)
    .qrSizeRatio(0.7)  // Use 70% of available space for QR
    .fontSizeRange(12, 48)
    .boldText(true)
    .build();
```

### Compact Labels

```java
QRLabelConfiguration config = QRLabelConfiguration.builder()
    .paperSize(PaperSize.LABEL_2X1_5)  // 2x1.5 inch compact label
    .printerDPI(203)
    .layoutType(LayoutType.QR_ONLY)
    .qrSizeRatio(0.9)  // Maximize QR code size
    .margins(5)        // Minimal margins
    .build();
```

## Configuration Options

### Paper & Printer Settings
```java
.paperWidthInches(4.0)           // Paper width in inches
.paperHeightInches(3.0)          // Paper height in inches
.printerDPI(203)                 // Printer resolution (203, 300, etc.)
.outputDirectory("./labels/")    // Output directory for saved files
```

### Layout Settings
```java
.layoutType(LayoutType.TOP_BOTTOM)    // Layout strategy
.includeText(true)                    // Include text on label
.qrSizeRatio(0.8)                    // QR code size ratio (0.1-1.0)
.margins(10)                         // Margin pixels
.spacing(15)                         // Spacing between elements
```

### QR Code Settings
```java
.errorCorrectionLevel(ErrorCorrectionLevel.L)  // Error correction (L, M, Q, H)
.qrMargin(0)                                   // QR code quiet zone
```

### Text Settings
```java
.fontFamily("Arial")          // Font family
.fontSizeRange(12, 72)       // Min and max font sizes
.boldText(true)              // Bold text rendering
```

## Available Paper Sizes

| Size | Dimensions | Use Case |
|------|------------|----------|
| `LABEL_4X3` | 4" × 3" | Standard shipping labels |
| `LABEL_4X6` | 4" × 6" | Large shipping labels |
| `LABEL_2X1` | 2" × 1" | Small product labels |
| `LABEL_2X1_5` | 2" × 1.5" | Compact product labels |
| `LABEL_3X2` | 3" × 2" | Medium product labels |
| `CUSTOM` | Custom | Use with `.paperWidthInches()` and `.paperHeightInches()` |

## Examples

### Example 1: Product Label with Description
```java
QRLabelConfiguration config = QRLabelConfiguration.builder()
    .paperSize(PaperSize.LABEL_4X3)
    .printerDPI(203)
    .layoutType(LayoutType.TOP_BOTTOM)
    .includeText(true)
    .qrSizeRatio(0.7)
    .fontSizeRange(14, 36)
    .boldText(true)
    .build();

CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
printer.setTextToEncode("Product: ABC-123 | SKU: XYZ789");
printer.saveToFile("product_abc123.png");
```

### Example 2: Inventory Label with Rotated Text
```java
QRLabelConfiguration config = QRLabelConfiguration.builder()
    .paperWidthInches(4.0)
    .paperHeightInches(3.0)
    .printerDPI(203)
    .layoutType(LayoutType.LEFT_RIGHT_ROTATED)
    .includeText(true)
    .qrSizeRatio(0.6)
    .fontFamily("Arial")
    .fontSizeRange(10, 32)
    .errorCorrectionLevel(ErrorCorrectionLevel.M)
    .build();

CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
printer.setTextToEncode("INV-2024-001234");
printer.saveToFile("inventory_001234.png");
```

### Example 3: High-Resolution Label
```java
QRLabelConfiguration config = QRLabelConfiguration.builder()
    .paperWidthInches(4.0)
    .paperHeightInches(3.0)
    .printerDPI(300)  // High resolution
    .layoutType(LayoutType.TOP_BOTTOM)
    .includeText(true)
    .qrSizeRatio(0.75)
    .fontSizeRange(18, 64)  // Larger fonts for high DPI
    .errorCorrectionLevel(ErrorCorrectionLevel.H)  // High error correction
    .build();
```

## Factory Methods

For common configurations, use the provided factory methods:

```java
// Standard 4x3 inch label
QRLabelConfiguration config = QRLabelConfiguration.createDefault4x3Config(203);

// Compact 2x1.5 inch label
QRLabelConfiguration config = QRLabelConfiguration.createCompact2x1_5Config(203);
```

## Error Handling

The library provides comprehensive error handling:

```java
try {
    CustomQRCodeLabelPrinter printer = new CustomQRCodeLabelPrinter(config);
    printer.setTextToEncode("https://example.com");
    printer.saveToFile("label.png");
} catch (IllegalArgumentException e) {
    // Invalid configuration or text
    System.err.println("Configuration error: " + e.getMessage());
} catch (IllegalStateException e) {
    // Text not set before generation
    System.err.println("State error: " + e.getMessage());
} catch (IOException e) {
    // File saving failed
    System.err.println("IO error: " + e.getMessage());
} catch (PrinterException e) {
    // Printing failed
    System.err.println("Printer error: " + e.getMessage());
}
```

## Dependencies

### Required Dependencies
```gradle
dependencies {
    // ZXing library for QR code generation
    implementation 'com.google.zxing:core:3.5.2'
    implementation 'com.google.zxing:javase:3.5.2'
    
    // Lombok for reducing boilerplate code
    compileOnly 'org.projectlombok:lombok:1.18.30'
    annotationProcessor 'org.projectlombok:lombok:1.18.30'
}
```

### Java Version
- **Minimum**: Java 17
- **Recommended**: Java 17 or higher

## Package Structure

```
qrlabel/
├── CustomQRCodeLabelPrinter.java     # Main API class
├── config/
│   ├── QRLabelConfiguration.java     # Configuration with Lombok
│   ├── LayoutType.java               # Layout type enum
│   └── PaperSize.java               # Paper size enum
├── layout/
│   ├── LayoutStrategy.java          # Strategy interface
│   ├── QROnlyLayout.java           # QR-only layout
│   ├── TopBottomLayout.java        # QR + text below
│   └── LeftRightRotatedLayout.java # QR + rotated text
├── factory/
│   ├── QRCodeFactory.java          # QR code generation
│   └── LayoutStrategyFactory.java  # Layout strategy creation
└── examples/
    └── CustomQRCodeLabelPrinterExample.java  # Usage examples
```

## Best Practices

### 1. **Configuration Reuse**
```java
// Create configuration once, reuse for multiple labels
QRLabelConfiguration config = QRLabelConfiguration.builder()
    .paperSize(PaperSize.LABEL_4X3)
    .printerDPI(203)
    .layoutType(LayoutType.TOP_BOTTOM)
    .includeText(true)
    .build();

// Reuse configuration for multiple printers
CustomQRCodeLabelPrinter printer1 = new CustomQRCodeLabelPrinter(config);
CustomQRCodeLabelPrinter printer2 = new CustomQRCodeLabelPrinter(config);
```

### 2. **Optimal QR Size Ratios**
- **QR Only**: 0.8-0.9 (maximize QR code size)
- **QR + Text Below**: 0.6-0.7 (leave room for text)
- **QR + Rotated Text**: 0.5-0.6 (accommodate side text)

### 3. **Font Size Guidelines**
- **203 DPI**: 12-48pt fonts
- **300 DPI**: 16-64pt fonts
- **Compact Labels**: Use smaller font ranges

### 4. **Error Correction Levels**
- **L (Low)**: 7% recovery, smaller QR codes
- **M (Medium)**: 15% recovery, balanced
- **Q (Quartile)**: 25% recovery, good for damaged labels
- **H (High)**: 30% recovery, maximum reliability

## Performance Considerations

### Memory Usage
- Images are cached after generation for reuse
- Call `generateLabel()` once, then `saveToFile()` multiple times if needed

### File I/O
- Output directories are created automatically
- Supported formats: PNG (default), JPEG, BMP, GIF

### Printing
- Use `printLabelSilent()` for batch printing without dialogs
- Page format is automatically configured based on paper size

## Troubleshooting

### Common Issues

**1. Package Declaration Errors**
```
The declared package "qrlabel.config" does not match the expected package
```
- **Solution**: These are IDE warnings that resolve after proper build

**2. Lombok Not Working**
```
lombok cannot be resolved
```
- **Solution**: Ensure Lombok plugin is installed in your IDE
- **Alternative**: Run `./gradlew build` to compile properly

**3. QR Code Generation Fails**
```
Text cannot be encoded as QR code
```
- **Solution**: Check text length and special characters
- **Tip**: Use `QRCodeFactory.canEncode(text)` to validate first

**4. Text Too Large for Label**
```
Text doesn't fit in allocated space
```
- **Solution**: Increase `qrSizeRatio` or decrease font size range
- **Tip**: Use shorter text or larger paper size

## License

This project is part of the JavaAutomation learning repository.

## Contributing

When extending the library:

1. **New Layout Strategies**: Implement `LayoutStrategy` interface
2. **New Paper Sizes**: Add to `PaperSize` enum
3. **New Features**: Follow existing design patterns
4. **Testing**: Add examples to `CustomQRCodeLabelPrinterExample`

## Version History

- **v1.0**: Initial release with core functionality
  - Three layout strategies
  - Configurable paper sizes and DPI
  - Lombok integration
  - Comprehensive examples and documentation
