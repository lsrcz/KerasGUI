package GUI;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * this attribute set stores different color's attribute set
 * used in Editor to highlight specific text
 *
 * @author Nianqi Liu
 */
public enum MyAttributeSet {


    cyanAttributeSet, orangeAttributeSet, greenAttributeSet, redAttributeSet, grayAttributeSet, blackAttributeSet;

    public static SimpleAttributeSet getAttribute(MyAttributeSet attribute) {
        String font = "TimesRoman";
        int style = Font.PLAIN;
        int size = 20;
        switch (attribute) {
            case cyanAttributeSet:
                return initSimpleAttribute(new SimpleAttributeSet(),
                        Color.CYAN, new Font(font, style, size));
            case orangeAttributeSet:
                return initSimpleAttribute(new SimpleAttributeSet(),
                        Color.ORANGE, new Font(font, style, size));
            case greenAttributeSet:
                return initSimpleAttribute(new SimpleAttributeSet(),
                        Color.GREEN, new Font(font, style, size));
            case redAttributeSet:
                return initSimpleAttribute(new SimpleAttributeSet(),
                        Color.RED, new Font(font, style, size));
            case grayAttributeSet:
                return initSimpleAttribute(new SimpleAttributeSet(),
                        Color.GRAY, new Font(font, style, size));
            case blackAttributeSet:
                return initSimpleAttribute(new SimpleAttributeSet(),
                        Color.BLACK, new Font(font, style, size));
            default:
                return initSimpleAttribute(new SimpleAttributeSet(),
                        Color.BLACK, new Font(font, style, size));
        }
    }

    private static SimpleAttributeSet initSimpleAttribute(SimpleAttributeSet attributeSet, Color color, Font font) {
        StyleConstants.setFontSize(attributeSet, font.getSize());
        StyleConstants.setForeground(attributeSet, color);
        return attributeSet;
    }
}
