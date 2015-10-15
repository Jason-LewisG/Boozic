package comjason_lewisg.httpsgithub.boozic.Handlers;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class AnimateToolbarHandler extends Animation {
    private int mHeight;
    private int mStartHeight;
    private View mView;

    public AnimateToolbarHandler(View view, int height){
        mView = view;
        mHeight = height;
        mStartHeight = view.getHeight();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
        int newHeight = mStartHeight + (int) ((mHeight - mStartHeight) * interpolatedTime);

        mView.getLayoutParams().height = newHeight;
        mView.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight)
    {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds()
    {
        return true;
    }
}
