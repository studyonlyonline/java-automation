package qrlabel.factory;

import qrlabel.config.LayoutType;
import qrlabel.layout.*;

/**
 * Factory class for creating layout strategy instances based on layout type.
 * Implements the Factory design pattern for layout strategy creation.
 * 
 * @author CustomQRCodeLabelPrinter
 * @version 1.0
 */
public class LayoutStrategyFactory {
    
    /**
     * Creates a layout strategy instance based on the specified layout type.
     * 
     * @param layoutType The type of layout strategy to create
     * @return A LayoutStrategy instance for the specified type
     * @throws IllegalArgumentException if the layout type is not supported
     */
    public static LayoutStrategy createLayoutStrategy(LayoutType layoutType) {
        if (layoutType == null) {
            throw new IllegalArgumentException("Layout type cannot be null");
        }
        
        switch (layoutType) {
            case QR_ONLY:
                return new QROnlyLayout();
                
            case TOP_BOTTOM:
                return new TopBottomLayout();
                
            case LEFT_RIGHT_ROTATED:
                return new LeftRightRotatedLayout();
                
            case RIGHT_HALF_ONLY:
                return new RightHalfLayout();
                
            case LEFT_HALF_ONLY:
                return new LeftHalfLayout();
                
            case DUAL_QR_LEFT_RIGHT:
                return new DualQRLayout();
                
            case DUAL_QR_WITH_TEXT_ON_RIGHT:
                return new DualQRWithTextOnRightQRLayout();
                
            default:
                throw new IllegalArgumentException("Unsupported layout type: " + layoutType);
        }
    }
    
    /**
     * Gets the default layout strategy (QR_ONLY).
     * 
     * @return A QROnlyLayout instance
     */
    public static LayoutStrategy getDefaultLayoutStrategy() {
        return new QROnlyLayout();
    }
    
    /**
     * Determines the appropriate layout strategy based on whether text is provided.
     * 
     * @param hasText true if text is provided, false otherwise
     * @param preferredType the preferred layout type (can be null)
     * @return An appropriate LayoutStrategy instance
     */
    public static LayoutStrategy createLayoutStrategy(boolean hasText, LayoutType preferredType) {
        if (!hasText) {
            return new QROnlyLayout();
        }
        
        if (preferredType != null && preferredType != LayoutType.QR_ONLY) {
            return createLayoutStrategy(preferredType);
        }
        
        // Default to TOP_BOTTOM when text is present but no preference is specified
        return new TopBottomLayout();
    }
    
    /**
     * Checks if a layout type supports text.
     * 
     * @param layoutType The layout type to check
     * @return true if the layout supports text, false otherwise
     */
    public static boolean supportsText(LayoutType layoutType) {
        if (layoutType == null) {
            return false;
        }
        
        LayoutStrategy strategy = createLayoutStrategy(layoutType);
        return strategy.supportsText();
    }
    
    /**
     * Gets all available layout types.
     * 
     * @return An array of all available LayoutType values
     */
    public static LayoutType[] getAvailableLayoutTypes() {
        return LayoutType.values();
    }
    
    /**
     * Gets layout types that support text.
     * 
     * @return An array of LayoutType values that support text
     */
    public static LayoutType[] getTextSupportingLayoutTypes() {
        return new LayoutType[] {
            LayoutType.TOP_BOTTOM,
            LayoutType.LEFT_RIGHT_ROTATED
        };
    }
}
