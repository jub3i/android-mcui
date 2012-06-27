package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

public class LoadingView extends View {
	//enum for the various animations the LoadingView can do.
	public enum AnimMode {
        A_UP , A_DOWN, A_DOWN_UP, A_DRAW_FULL
    }
	
	//x,y coordinates for the animations start and end position.
	private final int X_START_POS = -48;
    private final int Y_START_POS = -44;
    private final int Y_END_POS = 14;
    
    //since the PTR-library uses 4 instances of LoadingLayout, we need to sync the positions
    //of the animations to make movements up and down appear smooth.
    static int currentX = -48;
    static int currentY = -44;
    
    //enum for which mode of animation the LoadingView is in.
    private AnimMode mode;
    //boolean controlling the cycling between up and down animations.
	private boolean isAnimUp;
    
	//Instantiate static resources used in the animation.
    Bitmap mainImage = BitmapFactory.decodeResource(getResources(),R.drawable.background);
    Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.backmask);
    Bitmap foreground = BitmapFactory.decodeResource(getResources(), R.drawable.foreground);
    //create resulting bitmap used to draw the LoadingView.
    Bitmap result = Bitmap.createBitmap(mainImage.getWidth(), mainImage.getHeight(), Bitmap.Config.ARGB_8888);
    //drawing resources.
    Paint paint = new Paint();
    Canvas c = new Canvas(result);

    //constructor
    public LoadingView(Context context) {
		super(context);
		init();
	}
    
    //constructor
    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
	}
	
    //helper function for constructors.
	private void init() {
		paint.setFilterBitmap(false);
		mode = AnimMode.A_DRAW_FULL;
		isAnimUp = true;
	}
	
	//public function to call the animating of the LoadingView.
	public void animDown() {
		mode = AnimMode.A_DOWN;
		invalidate();
	}
	//public function to call the animating of the LoadingView.
	public void animUp() {
		mode = AnimMode.A_UP;
		invalidate();
	}
	//public function to call the animating of the LoadingView.
	public void animDownUp() {
		mode = AnimMode.A_DOWN_UP;
		invalidate();
	}
	//public function to call the animating of the LoadingView.
	//used to reset the LoadingView.
	public void animDrawFull() {
		currentX = X_START_POS;
		currentY = Y_START_POS;
		mode = AnimMode.A_DRAW_FULL;
		invalidate();		
	}
	
	//helper function for the onDraw() method.
	//draws the static resources onto the canvas used to draw the LoadingView.
	private void drawHelper(int x, int y, Canvas c) {
		c.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        c.drawBitmap(mainImage, x, y, paint);
        paint.setXfermode(null);
        c.drawBitmap(foreground, 0, 0, paint);
		
	}
	
	//Sets the size of the view to a static 56x75 pixels.
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(56, 75);
    }
	
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //switch to control which animation is being played.
        switch (mode) {
        	//draws the glass full.
        	case A_DRAW_FULL:
        		drawHelper(currentX, currentY, c);
                canvas.drawBitmap(result, -64, -56, null);
                invalidate();
                break;
            //animates the beer level dropping.
        	case A_DOWN:
        		if (currentY <= Y_END_POS) {
        			currentX += 2;
	            	currentY++;
        		}
        		drawHelper(currentX, currentY, c);
	            canvas.drawBitmap(result, -64, -56, null);
	            if (currentY <= Y_END_POS)
	            	invalidate();
	            break;
	        //animates the beer level rising.	
        	case A_UP:
        		if (currentY >= Y_START_POS) {
        			currentX -= 2;
        			currentY--;
        		}
        		drawHelper(currentX, currentY, c);
        		canvas.drawBitmap(result, -64, -56, null);
	            if (currentY >= Y_START_POS) 
	               	invalidate();                	
	            break;
	        //animates the beer level cycling between rising and falling.	
        	case A_DOWN_UP:
        		//start by dropping the beer level to the bottom.
        		if (isAnimUp == true) {
        			if (currentY <= Y_END_POS) {
            			currentX += 2;
    	            	currentY++;
            		}
        			drawHelper(currentX, currentY, c);
    	            canvas.drawBitmap(result, -64, -56, null);
    	            if (currentY <= Y_END_POS)
    	            	invalidate();
    	            else {
    	            	isAnimUp = false;
    	            	invalidate();
    	            }
    	        }
        		//then rise the beer level, repeat there after.
        		else {
        			if (currentY >= Y_START_POS) {
            			currentX -= 2;
            			currentY--;
            		}
        			drawHelper(currentX, currentY, c);
            		canvas.drawBitmap(result, -64, -56, null);
    	            if (currentY >= Y_START_POS) 
    	               	invalidate();
    	            else {
    	            	isAnimUp = true;
    	            	invalidate();
    	            }
    	        }
        		break;
        }
    }
}