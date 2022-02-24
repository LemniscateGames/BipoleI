package lib.panels;

import lib.ui.ElementBox;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class ElementPanel extends JPanel {
    /** All elements that should be drawn when this component is painted. **/
    private final ArrayList<ElementBox> drawElements;

    public ElementPanel() {
        // ---- Create UI elements
        drawElements = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
    }

    /** Draw UI elements. Drawn after map. **/
    public void drawElements(Graphics g){
        // Draw all elements
        for (ElementBox element : drawElements){
            element.draw(g);
        }
    }

    public void addElement(ElementBox element){
        drawElements.add(element);
        element.initialize(this);
        System.out.println("added element "+element);
    }
}
