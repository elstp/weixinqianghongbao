package com.elstp.wxqianghonbao.Tools;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import java.io.File;
import java.net.URI;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@SuppressWarnings("all")
public class WechatHelper extends AccessibilityService {
    AccessibilityNodeInfo nodeInfo;
    List<AccessibilityNodeInfo> listData;
    AccessibilityNodeInfo rootNode;
    public static String Page = null;//当前页面
    public static int Sum = 50;//重试次数
    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    public Thread websocketThread;
    public Thread missionThread;
    public static int openRedPackTime = 500,backRedPackTime = 600;
    public static boolean work = true;//是否正在运行
    public AccessibilityEvent accessibilityEvent;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this.getApplicationContext(), "服务已启动!请进入微信群聊页面保持不熄屏2333", Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (work) {
                    try {
                       GetMission();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }



    //开始任务
    private void GetMission() {
        work = false;
    //启动线程
        missionThread  = new Thread(){
            @Override
            public void run() {
                try {
                 while(true){
                     Thread.currentThread().sleep(300);
                     RedPacket();
                 }
                } catch (Exception e) {
                    e.printStackTrace();
                    work = true;
                }
            }
        };
        //线程启动
        missionThread.start();
    }
    //自动领取红包
    private boolean RedPacket() throws InterruptedException {
        System.out.println("开始领取红包");
        Rect window = WXUtils.findViewIdAndClickRedPacket((AccessibilityService) this, Common.CHAT_OPEN_REDView_ID);//取红包所在坐标 过滤已领取的红包
        if (window.bottom == 0 && window.right == 0 && window.top == 0 && window.left == 0) {
            System.out.println("领取失败");
            Wait("m1", 2, Sum, backRedPackTime);
            Wait("d84", 2, Sum, openRedPackTime);

            return false;
        } else {
            //不领取重复的红包 查找未领取的
            if (dispatchGestureClick(window.right, window.top)) {//点红包
                if (Wait("开", 1, Sum, openRedPackTime)) {//开红包
                    Wait("m1", 2, Sum, backRedPackTime);
                } else {
                    Wait("d84", 2, Sum, openRedPackTime);
                    return false;
                }
            } else {
                System.out.println("_A2");
                return false;
            }
        }
        return false;
    }




    //方法等待2
    private boolean Wait2(String comm, int sum, int sleep) {
        try {
            int resum = 0;
            for (; ; ) {
                Thread.sleep(sleep);
                if (resum >= sum) {
                    return false;
                } else {
                    if (ActionByText(comm)) {
                        Thread.sleep(sleep);
                        return true;
                    }
                }
                resum++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //方法等待
    private boolean Wait(String comm, int type, int sum, int sleep) {
        try {
            Thread.currentThread().sleep(sleep);
            int resum = 0;
            for (; ; ) {

                if (resum >= sum) {
                    return false;
                } else {
                    if (ActionByTextXYPath(comm, type)) {

                        return true;
                    }
                }
                resum++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

    }




    //发送消息
    private boolean sendMessage(String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(msg);

        Boolean isok2 = Wait(Common.CHAT_Home_GROUPMSG_TextBox2_ID, 2, Sum, 300);
        SystemClock.sleep(250);
        Boolean isok1 = WXUtils.findViewByIdAndPasteContent(this, Common.CHAT_Home_GROUPMSG_TextBox_ID, sb.toString());
        SystemClock.sleep(250);
        Boolean isok3 = Wait(Common.CHAT_Home_GROUPMSG_TextBoxSend2_ID, 2, Sum, 300);
        SystemClock.sleep(250);

        AccessibilityNodeInfo textInfo3 = findFirst(AbstractTF.newId(Common.CHAT_PACKAGE, Common.CHAT_Home_GROUPMSG_TextBox2_ID));//ID
        if (textInfo3 != null) {
            Rect window = new Rect();
            textInfo3.getBoundsInScreen(window);
            dispatchGestureClick(window.right, window.top - 25);
        }


        System.out.println("激活:" + isok2 + " 点击" + isok1 + "输入文本:" + isok3);
        if (isok1 == true && isok2 == true && isok3 == true) {
            return true;
        } else {

            return false;
        }

    }


    /**
     * 通过Text获取坐标进行点击
     *
     * @param id
     * @param type 1=备注 2= ID 3 = text
     */
    public boolean ActionByTextXYPath(String id, int type) {
        AccessibilityNodeInfo textInfo3 = null;
        switch (type) {
            case 1:
                textInfo3 = findFirst(AbstractTF.newContentDescription(id, true));
                break;
            case 2:
                textInfo3 = findFirst(AbstractTF.newId(Common.CHAT_PACKAGE, id));//ID
                break;
            case 3:
                textInfo3 = findFirst(AbstractTF.newText(id, true));//text
                break;
        }
        Rect window = new Rect();
        if (textInfo3 != null) {
            textInfo3.getBoundsInScreen(window);
            if (dispatchGestureClick(window.left + 10, window.top + 5)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 通过获取text 找到坐标 然后进行坐标点击
     */
    public boolean ActionByTextPath(AccessibilityEvent event, String str) {
        rootNode = getRootInActiveWindow();
        if (rootNode == null) {
            System.out.println(">>>rootNode = null");
            return false;
        }
        List<CharSequence> textList = event.getText();
        if (textList != null) {
            listData = rootNode.findAccessibilityNodeInfosByText(str);
            nodeInfo = findAccessibilityNodeInListByText(listData, str);
            if (nodeInfo != null) {
                Rect window = new Rect();
                nodeInfo.getBoundsInScreen(window);
                if (dispatchGestureClick(window.left, window.top)) {
                     System.out.println("点击成功");
                    return true;
                } else {
                     System.out.println("点击失败");
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 通过获取当前事件的当前窗口 取窗口text属性 并点击
     */
    public boolean ActionByText(String str) {
        rootNode = getRootInActiveWindow();
        if (rootNode == null) {
            return false;
        }
        rootNode = getRootInActiveWindow();
        listData = rootNode.findAccessibilityNodeInfosByText(str);
        nodeInfo = findAccessibilityNodeInListByText(listData, str);
        boolean IS3 = clickNodeInfo(nodeInfo);
        return IS3;
    }


    /**
     * 横滑屏
     */
    public boolean TransverseSlidingCcreen() {
        Path path2 = new Path();
        path2.moveTo(0, 400);
        path2.lineTo(400, 400);
        final GestureDescription.StrokeDescription sd = new GestureDescription.StrokeDescription(path2, 0, 500);
        //横滑
        return dispatchGesture(new GestureDescription.Builder().addStroke(sd).build(), null, null);
    }


    private void undoQrcode() {

    }

    private boolean pressDownHome() {
        return performGlobalAction(GLOBAL_ACTION_HOME);
    }

    private boolean pressDownBack() {
        return performGlobalAction(GLOBAL_ACTION_BACK);
    }

    private void sleep() {
        sleep(500);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private AccessibilityNodeInfo findAccessibilityNodeInListByText(List<AccessibilityNodeInfo> listData, String text) {
        if (listData == null) {
            return null;
        }
        for (AccessibilityNodeInfo info : listData) {
            CharSequence charSequence = info.getText();
            if (charSequence != null) {
                String msg = charSequence.toString();
                if (msg.equals(text)) {
                    return info;
                }
            }
        }
        return null;
    }

    private boolean clickNodeInfo(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                return nodeInfo.performAction(16);
            }
        }
        return false;
    }

    private boolean isTextInWindow(String text, AccessibilityNodeInfo node) {
        List<AccessibilityNodeInfo> list = node.findAccessibilityNodeInfosByText(text);
        AccessibilityNodeInfo info = findAccessibilityNodeInListByText(list, text);
        if (info != null) {
            return true;
        }
        return false;
    }

/**	(必要)系统需要中断AccessibilityService反馈时，将调用此方法。AccessibilityService反馈包括服务发起的震动、音频等行为。*/
    @Override
    public void onInterrupt() {

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        try{

                if (websocketThread!=null)
                   // websocketThread.stop();
                    websocketThread.interrupt();//标记线程终止

                if (missionThread!=null)
                  //  missionThread.stop();
                 missionThread.interrupt();//标记线程终止
            Toast.makeText(this.getApplicationContext(), "服务已关闭!", Toast.LENGTH_SHORT).show();
            System.gc();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 点击该控件
     *
     * @return true表示点击成功
     */
    public static boolean clickView(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            } else {
                AccessibilityNodeInfo parent = nodeInfo.getParent();
                if (parent != null) {
                    boolean b = clickView(parent);
                    parent.recycle();
                    if (b) return true;
                }
            }
        }
        return false;
    }

    /**
     * 查找第一个匹配的控件
     *
     * @param tfs 匹配条件，多个AbstractTF是&&的关系，如：
     *            AbstractTF.newContentDescription("表情", true),AbstractTF.newClassName(AbstractTF.ST_IMAGEVIEW)
     *            表示描述内容是'表情'并且是imageview的控件
     */
    @Nullable
    public AccessibilityNodeInfo findFirst(@NonNull AbstractTF... tfs) {
        if (tfs.length == 0) throw new InvalidParameterException("AbstractTF不允许传空");

        AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
        if (rootInfo == null) return null;

        int idTextTFCount = 0, idTextIndex = 0;
        for (int i = 0; i < tfs.length; i++) {
            if (tfs[i] instanceof AbstractTF.IdTextTF) {
                idTextTFCount++;
                idTextIndex = i;
            }
        }
        switch (idTextTFCount) {
            case 0://id或text数量为0，直接循环查找
                AccessibilityNodeInfo returnInfo = findFirstRecursive(rootInfo, tfs);
                rootInfo.recycle();
                return returnInfo;
            case 1://id或text数量为1，先查出对应的id或text，然后再查其他条件
                if (tfs.length == 1) {
                    AccessibilityNodeInfo returnInfo2 = ((AbstractTF.IdTextTF) tfs[idTextIndex]).findFirst(rootInfo);
                    rootInfo.recycle();
                    return returnInfo2;
                } else {
                    List<AccessibilityNodeInfo> listIdText = ((AbstractTF.IdTextTF) tfs[idTextIndex]).findAll(rootInfo);
                    if (Utils.isEmptyArray(listIdText)) {
                        break;
                    }
                    AccessibilityNodeInfo returnInfo3 = null;
                    for (AccessibilityNodeInfo info : listIdText) {//遍历找到匹配的
                        if (returnInfo3 == null) {
                            boolean isOk = true;
                            for (AbstractTF tf : tfs) {
                                if (!tf.checkOk(info)) {
                                    isOk = false;
                                    break;
                                }
                            }
                            if (isOk) {
                                returnInfo3 = info;
                            } else {
                                info.recycle();
                            }
                        } else {
                            info.recycle();
                        }
                    }
                    rootInfo.recycle();
                    return returnInfo3;
                }
            default:
                throw new RuntimeException("由于时间有限，并且多了也没什么用，所以IdTF和TextTF只能有一个");
        }
        rootInfo.recycle();
        return null;
    }

    /**
     * @param tfs 由于是递归循环，会忽略IdTF和TextTF
     */
    public static AccessibilityNodeInfo findFirstRecursive(AccessibilityNodeInfo parent, @NonNull AbstractTF... tfs) {
        if (parent == null) return null;
        if (tfs.length == 0) throw new InvalidParameterException("AbstractTF不允许传空");

        for (int i = 0; i < parent.getChildCount(); i++) {
            AccessibilityNodeInfo child = parent.getChild(i);
            if (child == null) continue;
            boolean isOk = true;
            for (AbstractTF tf : tfs) {
                if (!tf.checkOk(child)) {
                    isOk = false;
                    break;
                }
            }
            if (isOk) {
                return child;
            } else {
                AccessibilityNodeInfo childChild = findFirstRecursive(child, tfs);
                child.recycle();
                if (childChild != null) {
                    return childChild;
                }
            }
        }
        return null;
    }

    /**
     * 查找全部匹配的控件
     *
     * @param tfs 匹配条件，多个AbstractTF是&&的关系，如：
     *            AbstractTF.newContentDescription("表情", true),AbstractTF.newClassName(AbstractTF.ST_IMAGEVIEW)
     *            表示描述内容是'表情'并且是imageview的控件
     */
    @NonNull
    public List<AccessibilityNodeInfo> findAll(@NonNull AbstractTF... tfs) {
        if (tfs.length == 0) throw new InvalidParameterException("AbstractTF不允许传空");

        ArrayList<AccessibilityNodeInfo> list = new ArrayList<>();
        AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
        if (rootInfo == null) return list;

        int idTextTFCount = 0, idTextIndex = 0;
        for (int i = 0; i < tfs.length; i++) {
            if (tfs[i] instanceof AbstractTF.IdTextTF) {
                idTextTFCount++;
                idTextIndex = i;
            }
        }
        switch (idTextTFCount) {
            case 0://id或text数量为0，直接循环查找
                findAllRecursive(list, rootInfo, tfs);
                break;
            case 1://id或text数量为1，先查出对应的id或text，然后再循环
                List<AccessibilityNodeInfo> listIdText = ((AbstractTF.IdTextTF) tfs[idTextIndex]).findAll(rootInfo);
                if (Utils.isEmptyArray(listIdText)) {
                    break;
                }
                if (tfs.length == 1) {
                    list.addAll(listIdText);
                } else {
                    for (AccessibilityNodeInfo info : listIdText) {
                        boolean isOk = true;
                        for (AbstractTF tf : tfs) {
                            if (!tf.checkOk(info)) {
                                isOk = false;
                                break;
                            }
                        }
                        if (isOk) {
                            list.add(info);
                        } else {
                            info.recycle();
                        }
                    }
                }
                break;
            default:
                throw new RuntimeException("由于时间有限，并且多了也没什么用，所以IdTF和TextTF只能有一个");
        }
        rootInfo.recycle();
        return list;
    }

    /**
     * @param tfs 由于是递归循环，会忽略IdTF和TextTF
     */
    public static void findAllRecursive(List<AccessibilityNodeInfo> list, AccessibilityNodeInfo parent, @NonNull AbstractTF... tfs) {
        if (parent == null || list == null) return;
        if (tfs.length == 0) throw new InvalidParameterException("AbstractTF不允许传空");

        for (int i = 0; i < parent.getChildCount(); i++) {
            AccessibilityNodeInfo child = parent.getChild(i);
            if (child == null) continue;
            boolean isOk = true;
            for (AbstractTF tf : tfs) {
                if (!tf.checkOk(child)) {
                    isOk = false;
                    break;
                }
            }
            if (isOk) {
                list.add(child);
            } else {
                findAllRecursive(list, child, tfs);
                child.recycle();
            }
        }
    }

    /**
     * 立即发送移动的手势
     * 注意7.0以上的手机才有此方法，请确保运行在7.0手机上
     *
     * @param path  移动路径
     * @param mills 持续总时间
     */
    @RequiresApi(24)
    public void dispatchGestureMove(Path path, long mills) {
        dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription
                (path, 0, mills)).build(), null, null);
    }

    /**
     * 点击指定位置
     * 注意7.0以上的手机才有此方法，请确保运行在7.0手机上
     */
    @RequiresApi(24)
    public boolean dispatchGestureClick(int x, int y) {
        try {
            Path path = new Path();
            path.moveTo(x, y);
            boolean issucc = false;
            issucc = dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription
                    (path, 0, 100)).build(), null, null);
            return issucc;

        } catch (Exception e) {
            return false;
        }
    }


    //获取文件夹下的文件名
    public static String getFileName(String fileAbsolutePath) {
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        String filename = null;
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                filename = subFile[iFileLength].getName();
                System.out.println("文件名 ： " + filename);
                return filename;
            }
        }
        return filename;
    }

    /**
     * 解析二维码
     *
     * @return 解析的结果
     */
/*    private String decode(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);

            if (bitmap != null) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int[] pixels = new int[width * height];
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                // 新建一个RGBLuminanceSource对象
                RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                // 将图片转换成二进制图片
                BinaryBitmap binaryBitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
                Map<DecodeHintType, Object> hints = new HashMap<>();
                hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
                hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
                hints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);

                Result result = new QRCodeReader().decode(binaryBitmap, hints);

                return result.getText();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /**
     * 由于太多,最好回收这些AccessibilityNodeInfo
     */
    public static void recycleAccessibilityNodeInfo(List<AccessibilityNodeInfo> listInfo) {
        if (Utils.isEmptyArray(listInfo)) return;

        for (AccessibilityNodeInfo info : listInfo) {
            info.recycle();
        }
    }

    //消息队列
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    Toast.makeText(getApplicationContext(), "文件或文件夹不存在", Toast.LENGTH_LONG).show();
                    break;
                case 1:

                    Toast.makeText(getApplicationContext(), "删除成功！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public void DeleteFile(File file) {
        if (file.exists() == false) {

            mHandler.sendEmptyMessage(0);
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }

    /**
     * 取两个文本之间的文本值
     *
     * @param text  源文本 比如：欲取全文本为 12345
     * @param left  文本前面
     * @param right 后面文本
     * @return 返回 String
     */
    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }


}
