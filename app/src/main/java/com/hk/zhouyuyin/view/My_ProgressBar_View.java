package com.hk.zhouyuyin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.tencent.devicedemo.R;

public class My_ProgressBar_View extends ProgressBar {
    //一般来说我们都是重写他的带有一个参数的和带有两个参数的构造方法，
    //并且在一个参数的构造器中调用带有两个参数的构造器

    //接下来我们在这里面就去获取自定义view的相关属性了
    private static final int DEFAULT_TEXT_SIZE = 10;//sp
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_UNREACH_COLOR = 0xFF3D6DA;
    private static final int DEFAULT_UNREACH_HEIGHT = 7;//dp
    private static final int DEFAULT_REACH_HEIGHT = 7;//dp
    private static final int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_TEXT_OFFSET = 10;//dp

    //声明出我们的自定义属性,在这个类中我们定义的是自定义条形进度条，为了后期可以继承这个类子啊自定义圆形进度条,
    //我们要让这些属性为公有的(public)或者有益的(protected)
    public int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    public int mTextColr = DEFAULT_TEXT_COLOR;
    public int mUnreachColr = DEFAULT_UNREACH_COLOR;
    public int mUnreachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);
    public int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    public int mReachColor = DEFAULT_TEXT_COLOR;
    public int mTextOffSet = dp2px(DEFAULT_TEXT_OFFSET);

    public Paint paint = new Paint();//用于绘制的画笔
    //这里我们还需要声明一个用于获取空间的宽度减去padding的数值后的到的真正的宽度
    public int mRealWidth;//这个宽度我们要在测量onMeasure()方法中对其赋值，在绘制onDraw()方法中进行使用

    public My_ProgressBar_View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //我们在带有是三个参数的构造方法中完成相应的逻辑代码
        //在这里我们我们自定义一个用于获取自定义属性的方法
        obtainStyledAttrs(attrs);
    }

    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.My_ProgressBar_View);//这个对象是android给我们提供的专门用来获取自定义
        //属性文件中的属性的对象

        mTextSize = (int) ta.getDimension(R.styleable.My_ProgressBar_View_progress_text_offset, mTextSize);
        mTextColr = ta.getColor(R.styleable.My_ProgressBar_View_progress_text_color, mTextColr);
        mTextOffSet = (int) ta.getDimension(R.styleable.My_ProgressBar_View_progress_text_offset, mTextOffSet);
        mUnreachColr = ta.getColor(R.styleable.My_ProgressBar_View_progress_unreach_color, mUnreachColr);
        mUnreachHeight = (int) ta.getDimension(R.styleable.My_ProgressBar_View_progress_unreach_height, mUnreachHeight);
        mReachHeight = (int) ta.getDimension(R.styleable.My_ProgressBar_View_progress_reach_height, mReachHeight);
        mReachColor = ta.getColor(R.styleable.My_ProgressBar_View_progress_reach_color, mReachColor);


        ta.recycle();
        paint.setTextSize(mTextSize);//这里之所以要设置画笔的文本的字体大小，是为了在后期我们测量时利于自定义测量progressBar
    }
    //这样我们就完成了第一大步自定义属性的声明与获取
    //接下来我们来做第二大步完成测量onMeassure()


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //这个方法中一共有两个参数，分别是宽度的MeasureSpec和高度的MeasureSpec
        //在测量中一共分为三种模式,而这些模式的值我们要获得，这个值都封装在上面的两个参数中，我们通过MeasureSpec这个类拿到他所传入的这个值得模式和值得大小
        //int widthMode = MeasureSpec.getMode(widthMeasureSpec);//获取到宽度的模式
        int widthValue = MeasureSpec.getSize(widthMeasureSpec);//获取到宽度的值得大小
        //当我们拿到宽度的模式之后可以与我们的三种模式进行比较，然后在各种模式中进行相应的逻辑代码的处理
        //由于这里宽度的测量有点特殊，所以我们先进行高度的测量
        //为什么说宽度在测量时比较特殊呢：在正常情况下我们宽度测量和高度测量的流程是一样的，
        //因为这是一个精度条，他的宽度，即精度是由用户自己去定义的，所以他的宽度我们不需要做任何处理，我们只需要获取到这个宽度在最后用他来确定view的宽和高就可以了
        //为什么不去处理呢，是因为这个值是由用户给的，用户给多少我们就显示多少，所以我们认为他的宽度的模式一定为MeasureSpec.EXACTLY，所以直接返回widthValue就可以了
        //这里需要注意的是如果用户将这个宽度的模式强制的用MeasureSpec.AT_MOST，我们就不能这么做了，这时的宽度我们是无法获取的

        //我们定义一个用于测量高度的方法
        int height = measureHeight(heightMeasureSpec);

        //当我们的在测量完成之后一定要调用setMeasuredDimension()方法，来确定我们测量已经完成，从而确定view的宽和高
        setMeasuredDimension(widthValue, height);

        //这个就是我们之前定义的全局的宽度，用于表示这个精度条的宽度，之后在onDraw()方法中会用到这个值
        mRealWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {//完成了第二步之后我们就可以去做第三步了，用于去调用onDraw（）方法来重绘View
       // super.onDraw(canvas);//由于这里我们要完全的自己去重绘这个view，所以我们不去调用他的super.onDraw(canvas);

        //首先我们在绘制的时候他的起点一定是在getpaddingLeft(),并且他的绘制一共分为三个部分，左边，右边，和中间的文本区域
        //如果我们的文本到达了尽头我们就不需要再去绘制右边的部分了，所以我们需要去定义一个标志位，用于判断当前左边的宽度和文本的宽度以及mTextOffSet的值是否大于我们整个控件的宽度
        //如果大于我们就没有必要再去绘制右边的部分了
        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);//我们首先将画布一定下位置
        //这里我们去定义一个布尔变量用于去判断右边的部分是否需要绘制
        boolean flag=false;//默认情况下去绘制
        String text=getProgress()+"%";//获取我们需要绘制的文本
        int textWidth= (int) paint.measureText(text);//获取到文本的宽度
        float radio=getProgress()*1.0f/getMax();//这个是用于获取当前精度条的刻度,我们通过这个刻度就可以获取到左边部分的末端x坐标
        float ProgressX=radio*mRealWidth;//用于表示当前的进度
        if(ProgressX+textWidth>mRealWidth){//如果这个条件满足的话，我们是不需要去绘制右边的
            ProgressX=mRealWidth-textWidth/2;//我们左边的宽度最大只能是progressBar的宽度减去文本的宽度，不然就看不到文本了
            flag=true;//不去绘制右边的的部分
        }


        float endX=radio*mRealWidth-mTextOffSet/2;
        if(endX>0){
            paint.setColor(mReachColor);
            paint.setStrokeWidth(mReachHeight);//设置画笔的笔触宽度
            canvas.drawLine(0,0,endX,0,paint);//这样就可以绘制出一条线了，从而绘制出左边的部分
        }
        //当我们绘制完左边就可以绘制文本了，
        //不过这里我们要获取文本的高度
        int y= (int) (-(paint.descent()+paint.ascent())/2);
        paint.setColor(mTextColr);
        canvas.drawText(text,ProgressX,y,paint);

        //当我们绘制完文本就开始绘制右边的部分，首先我们来判断是否要绘制右边的部分
        if(!flag){
            float start=ProgressX+mTextOffSet/2+textWidth;
            //这里为什么是加上mTextOffSet/2，因为我们的ProgressX里面已经包括的mTextOffSet/2了，所以我们只需再加一半就可以了
            paint.setColor(mUnreachColr);
            paint.setStrokeWidth(mReachHeight);//设置笔触的宽度
            canvas.drawLine(start,0,mRealWidth,0,paint);
        }
        //以上就完成了我们三部分的绘制

        canvas.restore();


    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);//获取到高度的模式
        int heightValue = MeasureSpec.getSize(heightMeasureSpec);//获取到高度的值得大小
        //这里一共有三个模式，即我们的MeasureSpec有三个常量
        //1.:MeasureSpec.EXACTLY,这种模式是用户给了我们一个很明确的值，比如用户给我我们多少dp或者match_parent
        //2.:MeasureSpec.UNSPECIFIED,这种模式下我们尺寸需要我们自己去测量
        //3.:MeasureSpec.AT_MOST,这种模式下我们尺寸也需要我们自己去测量,在这个模式下，虽然说可以自己测量但是这个不能超过我们所获取的值得大小
        //如何自己去测量呢：
        //我们一自定义progressBar为例，就是取progressBar左边，右边以及中间（文本）三者中高度的最大的那个高度来测量
        if (heightMode == MeasureSpec.EXACTLY) {
            result = heightValue;//如果为一个准确值我们就让返回值为高度的值得大小
        } else {
            int textsize= (int) (paint.descent()-paint.ascent());//由于获取字体的大小
            //为了让返回值更准确，我们要让返回值加上上边距和下边距之后再加上三者的最大值
            result=getPaddingTop()+getPaddingBottom()+Math.max(Math.max(mReachHeight,mUnreachHeight),Math.abs(textsize));
            //我们先让左边的高度与右边的高度取出最大值再与textsize比大小
            if(heightMode==MeasureSpec.AT_MOST){
                result=Math.min(result,heightValue);//既然不能超过heightValue，那么我们就取这两个值得最小值
            }
        }

        return result;
    }

    public My_ProgressBar_View(Context context) {
        this(context, null);//调用带有两个参数的构造器
    }

    public My_ProgressBar_View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);//这里我们在两个参数的构造方法中调用三个参数的构造方法
    }


    //由于这里我我们用到了将dp转化为sp以及将sp转化为do，所以我们自定义两个用于转化的辅助方法
    protected int dp2px(int dpVal) {//dp转化为px
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {//sp转化为px
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }
}
