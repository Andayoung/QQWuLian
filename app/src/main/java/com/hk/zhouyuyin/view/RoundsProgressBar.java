package com.hk.zhouyuyin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.tencent.devicedemo.R;

/**
 * Created by liangliang on 2016/5/29.
 */
public class RoundsProgressBar extends My_ProgressBar_View {
    private int aa;

    public int getmRadius() {
        return mRadius;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    private int mRadius = dp2px(30);//确定这个精度条的半径，这是初始化的值，后期还可以根据具体情况自己来更改
    //由于我们在画圆形精度条时，他必须要有两个画笔的宽度，分别对应的是精度值得弧和未达到的弧
    private int mPaintWidth;//而这个宽度是我们在后期是要用来确定最终圆的宽度的(这个圆的半宽=mRadius+mPaintWidth/2);
    //而这个mPaintWidth的值我们是根据mReachHeight和mUnreachHeight这两个值得最大值决定的


    public RoundsProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mReachHeight = (int) (mUnreachHeight);//这里我们为了让自定义圆形进度条跟好看我们让进度的弧的宽是未完成弧宽的2.5倍

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundsProgressBar);

        mRadius = (int) ta.getDimension(R.styleable.RoundsProgressBar_radius, mRadius);//获取自定义的属性

        ta.recycle();//这步必须要有，否则即使获取到了自定义属性也无法生效,这步是我们在获取完自定义属性之后必须要执行的步骤

        //接下来就是设置下我们画笔的一些属性
        paint.setStyle(Paint.Style.STROKE);//由于我们是画的圆，所以首先来设置一下画笔的风格
        paint.setAntiAlias(true);//设置画笔是否抗锯齿,为了绘制圆形这个需要我们设为true
        paint.setDither(true);//为了绘制圆形这个需要我们设为true
        paint.setStrokeCap(Paint.Cap.ROUND);//设置连接处的风格，这里由于我们是在画圆，所以设为弧形
        //以上的这些属性我们可以根据实际开发的需要来确定是否要去设置

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //接下来是去重写他的测量的方法,由于我们是在绘制圆形我们会根据半径以及mPaintWidth去计算测量
        mPaintWidth = Math.max(mReachHeight, mUnreachHeight);
        //由于是一个圆形的进度条，所以我们默认他的4个padding的值是一致的，
        //当我们的用户将progressBar设为‘wrap_content’时，我们还要为其设置一个理想的宽度
        int expect = mRadius * 2 + mPaintWidth + getPaddingLeft() + getPaddingRight();//如果用户没有把progressBar的宽度设为一个精确值，那么这个progressBar的宽度我们就设为这个
        //那么这里我们是否还需要去向条形进度条那样获取他的模式和值，然后在对模式进行判断，最后获取到明确的值呢，其实这里是不需要的，
        //在android的API中他已经给我们提供了一个方法resolveSize(),这个方法不经可以在这里用，还可以在条形精度条中用，我们只要传入一个理想的值以及
        //onMeasure()方法中的任何一个参数就可以获取到对应宽和高重新测量的的明确值
        int width = resolveSize(expect, widthMeasureSpec);//其实resolveSize()方法内部的实现过程和我们之前测量的实现过程一模一样，android给我们封装好了
        int height = resolveSize(expect, heightMeasureSpec);
        //以上的width和height是获取到了宽和高，由于我们是在画圆，所以我们要去出这两个值得最小值来定圆
        int readWidth = Math.min(width, height);//这就是这个progressBar最终的宽度

        mRadius = (readWidth - getPaddingLeft() - getPaddingRight() - mPaintWidth) / 2;
        setMeasuredDimension(readWidth, readWidth);//由于是圆形所以在确定的时候，宽和高是一样的,
        //以上我们完成了测量的全过程，接下来就是取做最后一步:重写onDraw()来完成相应的绘制图形的过程
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // super.onDraw(canvas);//由于这里我们要完全的自己去重绘这个view，所以我们不去调用他的super.onDraw(canvas);
        String text = getProgress() + "%";//获取到文本
        int textWidth = (int) paint.measureText(text);//获取到文本的宽度
        int textHeight = (int) ((paint.descent() + paint.ascent()) / 2);//获取文本的高度

        canvas.save();
        //接下来是在这两部的中间去绘制我们的图形
        canvas.translate(getPaddingLeft() + mPaintWidth / 2, getPaddingTop() + mPaintWidth / 2);//我们首先将画布一定下位置,这里的第一个参数和第二个参数为什么和我们之前在设置
        //条形进度条的时候的第一个参数和第二个不一样，是因为圆的明确精度比未完成精度多mPaintWidth/2
        paint.setStyle(Paint.Style.STROKE);
        //darw Unreach bar
        paint.setColor(mUnreachColr);
        paint.setStrokeWidth(mUnreachHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, paint);
        //draw reach bar
        paint.setColor(mReachColor);
        paint.setStrokeWidth(mReachHeight);
        //这里我们不是在画圆了，而是在画弧度
        float sweepAngle=getProgress()*1.0f/getMax()*360;//获取明确精度的弧度
        canvas.drawArc(new RectF(0,0,mRadius*2,mRadius*2),-90,sweepAngle,false,paint);//为我们的明确精度绘制弧度
        //如果new RectF(0,0,mRadius*2,mRadius*2)，他一直不变的话，我们就将他提取出来，不需要一直在这里，不然会造成内存的分配与回收
        //draw text
        paint.setColor(mTextColr);
        paint.setStyle(Paint.Style.FILL);//由于是绘制文本，所以他的填充风格要是FILL的模式
//        canvas.drawText(text,mRadius-textWidth/2,mRadius-textHeight,paint);
        canvas.restore();
    }

    public RoundsProgressBar(Context context) {
        this(context, null);
    }

    public RoundsProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public int getAa() {
        return aa;
    }

    public void setAa(int aa) {
        this.aa = aa;
    }
}
