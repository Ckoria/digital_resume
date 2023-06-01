package com.onthemove.commons.utils;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.onthemove.interfaces.AdapterItemTouchListner;
import com.onthemove.interfaces.ViewHolderItemTouchListner;

public class SwipeAndDrag extends ItemTouchHelper.Callback {

    private AdapterItemTouchListner adapterItemTouchListner;

    public SwipeAndDrag(AdapterItemTouchListner adapterItemTouchListner) {
        this.adapterItemTouchListner = adapterItemTouchListner;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//        return 0;

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        int movementFlags = makeMovementFlags(dragFlags,swipeFlags);

       return movementFlags;
    }

    @Override public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, @NonNull RecyclerView.ViewHolder target) {

        int oldPosition = source.getAdapterPosition();
        int newPosition = target.getAdapterPosition();

        adapterItemTouchListner.itemMove(oldPosition,newPosition);

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        int position = viewHolder.getAdapterPosition();

        adapterItemTouchListner.swipeItemDismiss(position);
    }




    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE){

            ViewHolderItemTouchListner viewHolderItemTouchListner = (ViewHolderItemTouchListner) viewHolder;
            viewHolderItemTouchListner.itemSelected();
        }
    }


    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        ViewHolderItemTouchListner viewHolderItemTouchListner = (ViewHolderItemTouchListner) viewHolder;
        viewHolderItemTouchListner.itemClear();
    }



    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }


    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
