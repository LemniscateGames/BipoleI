package BipoleI.lib;

/** AlignedBoxes will align to their parent with the internal selfAlign
 * stored in this class regardless of parents' align. Should be used to align items within a FixedBox,
 * making sure that individual AlignedBoxes within them do not have a chance to overlap.
 */
public class AlignedBox extends ElementBox {
    private final Alignment selfAlign;

    public AlignedBox(ElementBox parent, Alignment align) {
        super(parent);
        selfAlign = align;
    }
    public AlignedBox(ElementBox parent){
        this(parent, Alignment.TOP_RIGHT);
    }

    @Override
    public int getX(){
        switch(getAlignment().x){
            case 0: return getParent().getX() + getParent().getLeftPad() + getLeftMargin();
            case 1: return getParent().getX() + getParent().getLeftPad() + (getParent().getInnerWidth() - getTotalWidth())/2 + getLeftMargin();
            case 2: return getParent().getX() + getParent().getTotalWidth() - getParent().getRightPad() - getTotalWidth() + getLeftMargin();
        }
        return 0;
    }

    @Override
    public int getY(){
        switch(getAlignment().y){
            case 0: return getParent().getY() + getParent().getTopPad() + getTopMargin();
            case 1: return getParent().getY() + getParent().getTopPad() + (getParent().getInnerHeight() - getTotalHeight())/2 + getTopMargin();
            case 2: return getParent().getY() + getParent().getTotalHeight() - getParent().getBottomPad() - getTotalHeight() + getTopMargin();
        }
        return 0;
    }

    /** Instead of getting parent's alignment, this ElementBox uses its own
     * when getAlignment is called within its methods. **/
    @Override
    public Alignment getAlignment(){
        return selfAlign;
    }
}
