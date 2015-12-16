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
public class EditTextWithDrawable extends EditText implements TextWatcher {
    private Drawable defaultDrawableLeft;
    private Drawable defaultDrawableRight;
    private Drawable drawableLeftEnable;
    private Drawable drawableRightEnable;
    private OnEditTextDrawableClickListener onEditTextDrawableClickListener;
    private boolean isDrawableLeftEnable;
    private boolean isDrawableRightEnable;

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
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditTextWithDrawable, 0, 0);
        try {
            defaultDrawableLeft = ta.getDrawable(R.styleable.EditTextWithDrawable_defaultDrawableLeft);
            defaultDrawableRight = ta.getDrawable(R.styleable.EditTextWithDrawable_defaultDrawableRight);
            drawableLeftEnable = ta.getDrawable(R.styleable.EditTextWithDrawable_drawableLeftEnable);
            drawableRightEnable = ta.getDrawable(R.styleable.EditTextWithDrawable_drawableRightEnable);
            drawLeft(defaultDrawableLeft);
            drawRight(defaultDrawableRight);
            setTextChangeDrawable();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }
    }

    private void setTextChangeDrawable() {
        this.addTextChangedListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        return super.dispatchTouchEvent(event);
    }

    public void clear(){
        setText("");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventX, eventY;
        Rect rect = null;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            eventX = (int) event.getRawX();
            eventY = (int) event.getRawY();
            rect = new Rect();
            getGlobalVisibleRect(rect);
            if (defaultDrawableLeft != null) {
                int dw = defaultDrawableLeft.getMinimumWidth();
                rect.right = rect.left + dw + getPaddingLeft();
                if (rect.contains(eventX, eventY) && onEditTextDrawableClickListener != null && isDrawableLeftEnable) {
                    onEditTextDrawableClickListener.onDrawableLeftClickListener();
                }
            }
            if (defaultDrawableRight != null) {
                int dw = defaultDrawableRight.getMinimumWidth();
                rect.left = rect.right - dw - getPaddingRight();
                if (rect.contains(eventX, eventY) && onEditTextDrawableClickListener != null && isDrawableRightEnable) {
                    onEditTextDrawableClickListener.onDrawableRightClickListener();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void drawLeft(Drawable drawableLeft) {
        if (drawableLeft != null) {
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
            this.setCompoundDrawables(drawableLeft, null, null, null);

        }
    }


    public void drawRight(Drawable drawableRight) {
        if (drawableRight != null) {
            drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
            this.setCompoundDrawables(null, null, drawableRight, null);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString())) {
            drawLeft(defaultDrawableLeft);
            drawRight(defaultDrawableRight);
            if (defaultDrawableLeft != null) {
                isDrawableLeftEnable = false;
            }
            if (defaultDrawableRight != null) {
                isDrawableRightEnable = false;
            }

        } else {
            drawLeft(drawableLeftEnable);
            drawRight(drawableRightEnable);
            if (defaultDrawableLeft != null) {
                isDrawableLeftEnable = true;
            }
            if (defaultDrawableRight != null) {
                isDrawableRightEnable = true;
            }
        }
    }
}
