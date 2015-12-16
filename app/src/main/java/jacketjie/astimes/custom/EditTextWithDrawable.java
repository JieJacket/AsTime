package jacketjie.astimes.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import jacketjie.astimes.R;
import jacketjie.astimes.utils.interfaces.OnEditTextDrawableClickListener;

/**
 * Created by Administrator on 2015/12/16.
 * 自定义edittext
 */
public class EditTextWithDrawable extends EditText{
    private Drawable drawableLeft;
    private Drawable drawableRight;
    private Drawable drawableLeftEnable;
    private Drawable drawableRightEnable;
    private OnEditTextDrawableClickListener onEditTextDrawableClickListener;

    public void setOnEditTextDrawableClickListener(OnEditTextDrawableClickListener onEditTextDrawableClickListener) {
        this.onEditTextDrawableClickListener = onEditTextDrawableClickListener;
    }

    public EditTextWithDrawable(Context context) {
        super(context);
    }

    public EditTextWithDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextWithDrawable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditTextWithDrawable,0,0);
        try{
            drawableLeft = ta.getDrawable(R.styleable.EditTextWithDrawable_drawableLeft);
            drawableRight = ta.getDrawable(R.styleable.EditTextWithDrawable_drawableRight);
            drawableLeftEnable = ta.getDrawable(R.styleable.EditTextWithDrawable_drawableLeftEnable);
            drawableRightEnable = ta.getDrawable(R.styleable.EditTextWithDrawable_drawableRightEnable);
            setDrawableLeft(drawableLeft);
            setDrawableRight(drawableRight);
            setTextChangeDrawable();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            ta.recycle();
        }
    }

    private void setTextChangeDrawable() {
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())){
                    setDrawableLeft(drawableLeft);
                    setDrawableRight(drawableRight);
                }else{
                    setDrawableLeft(drawableLeftEnable);
                    setDrawableRight(drawableRightEnable);
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventX,eventY;
        Rect rect = null;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                eventX = (int) event.getRawX();
                eventY = (int) event.getRawY();
                rect = new Rect();
                getGlobalVisibleRect(rect);
                if (drawableLeft != null && onEditTextDrawableClickListener != null){
                    int dw = drawableLeft.getMinimumWidth();
                    int dh = drawableLeft.getMinimumHeight();
                    rect.right = rect.left + dw;
                    if (rect.contains(eventX,eventY)){
                        onEditTextDrawableClickListener.onDrawableLeftClickListener();
                    }
                }
                if (drawableRight != null && onEditTextDrawableClickListener != null){
                    int dw = drawableRight.getMinimumWidth();
                    int dh = drawableRight.getMinimumHeight();
                    rect.left = rect.left - dw;
                    if (rect.contains(eventX,eventY)){
                        onEditTextDrawableClickListener.onDrawableLeftClickListener();
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:

                break;

        }
        return super.dispatchTouchEvent(event);
    }

    public void setDrawableLeft(Drawable drawableLeft) {
        this.drawableRight = drawableLeft;
        if (drawableLeft != null){
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
            this.setCompoundDrawables(drawableLeft, null, null, null);

        }
    }


    public void setDrawableRight(Drawable drawableRight) {
        this.drawableRight = drawableRight;
        if (drawableRight != null){
            drawableRight.setBounds(0,0,drawableRight.getMinimumWidth(),drawableRight.getMinimumHeight());
            this.setCompoundDrawables(null,null,drawableRight,null);
        }
    }

}
