package king.steal.camara.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import king.steal.camara.R;


public class ShapeLoadingDialog {

    /**
     * 加载数据对话框
     */
    private static Dialog mDialog;
    private static View mDialogContentView;


    /**
     * 显示加载对话框
     *
     * @param context 上下文
     */
    public static Dialog showDialogForLoading(final Activity context) {
        mDialog = new Dialog(context, R.style.custom_dialog);
        mDialogContentView = LayoutInflater.from(context).inflate(R.layout.layout_dialog, null);
        mDialog.setContentView(mDialogContentView);
        mDialog.setCanceledOnTouchOutside(false);
        // 监听 Dialog 的 Key 事件
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                // 关闭当前 Activity
                context.finish();
            }
        });
        if (null != mDialog && !mDialog.isShowing()) {
            mDialog.show();
        }
        return mDialog;
    }
    /**
     * 显示加载对话框
     *
     * @param context 上下文
     */
    public static Dialog showDialogForLoading(final Activity context, String text) {
        mDialog = new Dialog(context, R.style.custom_dialog);
        mDialogContentView = LayoutInflater.from(context).inflate(R.layout.layout_dialog, null);
        mDialog.setContentView(mDialogContentView);
        mDialog.setCanceledOnTouchOutside(false);
        TextView tvContent = mDialogContentView.findViewById(R.id.tv_content);
        tvContent.setText(text);
        // 监听 Dialog 的 Key 事件
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                // 关闭当前 Activity
                context.finish();
            }
        });
        if (null != mDialog && !mDialog.isShowing()) {
            mDialog.show();
        }
        return mDialog;
    }
    public void setBackground(int color) {
        GradientDrawable gradientDrawable = (GradientDrawable) mDialogContentView.getBackground();
        gradientDrawable.setColor(color);
    }



    public static void dismiss() {
        if (null != mDialog) {
            mDialog.dismiss();
        }
    }

    public static void cancelDialogForLoading() {
        if (null != mDialog) {
            mDialog.dismiss();
        }
    }

    public Dialog getDialog() {
        return mDialog;
    }
}
