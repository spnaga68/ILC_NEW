package realmstudy.interfaces;

/**
 * Created by developer on 7/3/17.
 */
public interface SlideRecyclerView {

    public void setUndoOn(boolean undoOn);
    public boolean isUndoOn();
    public void pendingRemoval(int position);
    public void remove(int position);
    public boolean isPendingRemoval(int position);
}
