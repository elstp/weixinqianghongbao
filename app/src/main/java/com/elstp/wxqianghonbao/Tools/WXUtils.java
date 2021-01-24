package com.elstp.wxqianghonbao.Tools;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

public class WXUtils {
    public static AccessibilityNodeInfo findNodeById(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        List findAccessibilityNodeInfosByViewId = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str);
        if (findAccessibilityNodeInfosByViewId == null || findAccessibilityNodeInfosByViewId.isEmpty()) {
            return null;
        }
        return (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
    }

    public static AccessibilityNodeInfo findNodeById(AccessibilityService accessibilityService, String str) {
        List windows = accessibilityService.getWindows();
        for (int size = windows.size() - 1; size >= 0; size--) {
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                List findAccessibilityNodeInfosByViewId = root.findAccessibilityNodeInfosByViewId(str);
                if (findAccessibilityNodeInfosByViewId != null && !findAccessibilityNodeInfosByViewId.isEmpty()) {
                    return (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                }
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findNodeByText(AccessibilityService accessibilityService, String str) {
        List windows = accessibilityService.getWindows();
        for (int size = windows.size() - 1; size >= 0; size--) {
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                List findAccessibilityNodeInfosByText = root.findAccessibilityNodeInfosByText(str);
                if (findAccessibilityNodeInfosByText != null && !findAccessibilityNodeInfosByText.isEmpty()) {
                    return (AccessibilityNodeInfo) findAccessibilityNodeInfosByText.get(0);
                }
            }
        }
        return null;
    }
    public static boolean findViewTextAndClick(AccessibilityService accessibilityService, String str,int index) {
        List windows = accessibilityService.getWindows();
        for (int size = windows.size() - 1; size >= 0; size--) {
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                List findAccessibilityNodeInfosByViewText = root.findAccessibilityNodeInfosByText(str);
                if (findAccessibilityNodeInfosByViewText != null && !findAccessibilityNodeInfosByViewText.isEmpty()) {
                    performClick((AccessibilityNodeInfo) findAccessibilityNodeInfosByViewText.get(index));
                    return true;
                }
            }
        }
        return false;
    }


    public static Rect findViewIdAndClickRedPacket(AccessibilityService accessibilityService, String str) {
        List windows = accessibilityService.getWindows();
        Rect window = new Rect();
        window.set(0,0,0,0);
        try{

        for (int size = windows.size() - 1; size >= 0; size--) {
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                List findAccessibilityNodeInfosByViewId = root.findAccessibilityNodeInfosByViewId(str);
                if (findAccessibilityNodeInfosByViewId != null && !findAccessibilityNodeInfosByViewId.isEmpty()) {

                    for (int i = 0 ; i<findAccessibilityNodeInfosByViewId.size();i++){
                        AccessibilityNodeInfo A =   (AccessibilityNodeInfo)findAccessibilityNodeInfosByViewId.get(i);//得到第一层
                        AccessibilityNodeInfo B =   A.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/aui").get(0);//得到红包  索引
                        //0代表里面没有已领取的字 说明红包没有被领取
                        if (B.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/aul").size() == 0){
                            System.out.println(">>>>>>>>>>没有领取");
                            AccessibilityNodeInfo D =   B.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/auj").get(0);//红包图标
                                D.getBoundsInScreen(window);//储存坐标
                                return window;
                        }else{
                            System.out.println(">>>>>>>>>>已被领取>>继续查找未领取的红包");
                        }
                    }
                    return window;
                }
            }
        }
        }catch (Exception e){
            return window;
        }
        return window;
    }

    public static boolean findViewIdAndRemoveListClick(AccessibilityService accessibilityService, String str) {
        List windows = accessibilityService.getWindows();
        for (int size = windows.size() - 1; size >= 0; size--) {
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                List findAccessibilityNodeInfosByViewId = root.findAccessibilityNodeInfosByViewId(str);
                if (findAccessibilityNodeInfosByViewId != null && !findAccessibilityNodeInfosByViewId.isEmpty()) {
                    System.out.println(">>>>>成员长度:"+findAccessibilityNodeInfosByViewId.size());

                    for (int i = 0 ; i< findAccessibilityNodeInfosByViewId.size(); i++){

                        AccessibilityNodeInfo ListView =  (AccessibilityNodeInfo)findAccessibilityNodeInfosByViewId.get(i);




                        AccessibilityNodeInfo BB =  ListView.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a1i").get(0);

                        System.out.println(">>>>>>>成员1"+BB.toString());
                        AccessibilityNodeInfo BB_A = BB.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/enl").get(0);
                        System.out.println(">>>>>>>成员1_VALUE>"+BB_A.toString());

                        if (i == 1){
                            AccessibilityNodeInfo BB_B = BB.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/enl").get(0);
                            System.out.println(">>>>>>>成员2_VALUE>"+BB_B.toString());
                        }

                        System.out.println("INDEX>>>>>>>>>"+i);

                    }

                    //performClick((AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0));
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean findViewIdAndClick(AccessibilityService accessibilityService, String str) {
        List windows = accessibilityService.getWindows();
        for (int size = windows.size() - 1; size >= 0; size--) {
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                List findAccessibilityNodeInfosByViewId = root.findAccessibilityNodeInfosByViewId(str);
                if (findAccessibilityNodeInfosByViewId != null && !findAccessibilityNodeInfosByViewId.isEmpty()) {
                    performClick((AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0));
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean findViewIdAndClick(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        List findAccessibilityNodeInfosByViewId = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str);
        if (findAccessibilityNodeInfosByViewId == null || findAccessibilityNodeInfosByViewId.isEmpty()) {
            return false;
        }
        performClick((AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0));
        return true;
    }

    public static boolean findLastViewIdAndClick(AccessibilityService accessibilityService, String str) {
        List windows = accessibilityService.getWindows();
        for (int size = windows.size() - 1; size >= 0; size--) {
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                List findAccessibilityNodeInfosByViewId = root.findAccessibilityNodeInfosByViewId(str);
                if (findAccessibilityNodeInfosByViewId != null && !findAccessibilityNodeInfosByViewId.isEmpty()) {
                    performClick((AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(findAccessibilityNodeInfosByViewId.size() - 1));
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean findLastViewIdAndClick(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        List findAccessibilityNodeInfosByViewId = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str);
        if (findAccessibilityNodeInfosByViewId == null || findAccessibilityNodeInfosByViewId.isEmpty()) {
            return false;
        }
        performClick((AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(findAccessibilityNodeInfosByViewId.size() - 1));
        return true;
    }

    public static boolean findViewIdAndCheck(AccessibilityService accessibilityService, String str) {
        List windows = accessibilityService.getWindows();
        boolean z = false;
        for (int size = windows.size() - 1; size >= 0; size--) {
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                for (AccessibilityNodeInfo accessibilityNodeInfo : root.findAccessibilityNodeInfosByViewId(str)) {
                    if (!accessibilityNodeInfo.isChecked()) {
                        performClick(accessibilityNodeInfo);
                    }
                    z = true;
                }
                if (z) {
                    break;
                }
            }
        }
        return z;
    }

    public static String findTextById(AccessibilityService accessibilityService, String str) {
        List windows = accessibilityService.getWindows();
        int size = windows.size();
        while (true) {
            size--;
            if (size < 0) {
                return null;
            }
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                Iterator it = root.findAccessibilityNodeInfosByViewId(str).iterator();
                if (it.hasNext()) {
                    CharSequence text = ((AccessibilityNodeInfo) it.next()).getText();
                    if (text == null) {
                        return null;
                    }
                    return text.toString();
                }
            }
        }
    }

    public static String findTextById(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        Iterator it = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str).iterator();
        if (!it.hasNext()) {
            return null;
        }
        CharSequence text = ((AccessibilityNodeInfo) it.next()).getText();
        if (text == null) {
            return null;
        }
        return text.toString();
    }

    public static String findTextByIdEx(AccessibilityService accessibilityService, String str) {
        Iterator it = accessibilityService.getRootInActiveWindow().findAccessibilityNodeInfosByViewId(str).iterator();
        if (!it.hasNext()) {
            return null;
        }
        CharSequence text = ((AccessibilityNodeInfo) it.next()).getText();
        if (text == null) {
            return null;
        }
        return text.toString();
    }

    public static String findDescById(AccessibilityService accessibilityService, String str) {
        List windows = accessibilityService.getWindows();
        int size = windows.size();
        while (true) {
            size--;
            if (size < 0) {
                return null;
            }
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                Iterator it = root.findAccessibilityNodeInfosByViewId(str).iterator();
                if (it.hasNext()) {
                    CharSequence contentDescription = ((AccessibilityNodeInfo) it.next()).getContentDescription();
                    if (contentDescription == null) {
                        return null;
                    }
                    return contentDescription.toString();
                }
            }
        }
    }

    public static String findDescById(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        Iterator it = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str).iterator();
        if (!it.hasNext()) {
            return null;
        }
        CharSequence contentDescription = ((AccessibilityNodeInfo) it.next()).getContentDescription();
        if (contentDescription == null) {
            return null;
        }
        return contentDescription.toString();
    }

    public static AccessibilityNodeInfo findNodeByChildTextStartsWith(AccessibilityService accessibilityService, String str, String str2, String str3) {
        List windows = accessibilityService.getWindows();
        for (int size = windows.size() - 1; size >= 0; size--) {
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                for (AccessibilityNodeInfo accessibilityNodeInfo : root.findAccessibilityNodeInfosByViewId(str)) {
                    List findAccessibilityNodeInfosByViewId = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str2);
                    if (findAccessibilityNodeInfosByViewId != null && !findAccessibilityNodeInfosByViewId.isEmpty()) {
                        CharSequence text = ((AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0)).getText();
                        if (text != null && text.toString().startsWith(str3)) {
                            return accessibilityNodeInfo;
                        }
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findNodeByChildTextStartsWith(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2, String str3) {
        for (AccessibilityNodeInfo accessibilityNodeInfo2 : accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str)) {
            List findAccessibilityNodeInfosByViewId = accessibilityNodeInfo2.findAccessibilityNodeInfosByViewId(str2);
            if (findAccessibilityNodeInfosByViewId != null && !findAccessibilityNodeInfosByViewId.isEmpty()) {
                CharSequence text = ((AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0)).getText();
                if (text != null && text.toString().startsWith(str3)) {
                    return accessibilityNodeInfo2;
                }
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findNodeByChildText(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2, String str3) {
        for (AccessibilityNodeInfo accessibilityNodeInfo2 : accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str)) {
            List findAccessibilityNodeInfosByViewId = accessibilityNodeInfo2.findAccessibilityNodeInfosByViewId(str2);
            if (findAccessibilityNodeInfosByViewId != null && !findAccessibilityNodeInfosByViewId.isEmpty()) {
                CharSequence text = ((AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0)).getText();
                if (text != null && text.toString().equals(str3)) {
                    return accessibilityNodeInfo2;
                }
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findNodeByClassNameWithParentId(AccessibilityService accessibilityService, String str, String str2) {
        List windows = accessibilityService.getWindows();
        for (int size = windows.size() - 1; size >= 0; size--) {
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                for (AccessibilityNodeInfo accessibilityNodeInfo : root.findAccessibilityNodeInfosByViewId(str)) {
                    int i = 0;
                    while (true) {
                        if (i < accessibilityNodeInfo.getChildCount()) {
                            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                            CharSequence className = child.getClassName();
                            if (className != null && className.toString().equals(str2)) {
                                return child;
                            }
                            i++;
                        }
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static boolean findTextAndClick(AccessibilityService accessibilityService, String str) {
        List windows = accessibilityService.getWindows();
        for (int size = windows.size() - 1; size >= 0; size--) {
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                List findAccessibilityNodeInfosByText = root.findAccessibilityNodeInfosByText(str);
                if (findAccessibilityNodeInfosByText != null && !findAccessibilityNodeInfosByText.isEmpty()) {
                    performClick((AccessibilityNodeInfo) findAccessibilityNodeInfosByText.get(0));
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean findViewByIdAndPasteContent(AccessibilityService accessibilityService, String str, String str2) {
        List windows = accessibilityService.getWindows();
        for (int size = windows.size() - 1; size >= 0; size--) {
            AccessibilityNodeInfo root = ((AccessibilityWindowInfo) windows.get(size)).getRoot();
            if (root != null) {
                List findAccessibilityNodeInfosByViewId = root.findAccessibilityNodeInfosByViewId(str);
                if (findAccessibilityNodeInfosByViewId != null && !findAccessibilityNodeInfosByViewId.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, str2);
                    ((AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0)).performAction(2097152, bundle);
                    return true;
                }
            }
        }
        return false;
    }

    public static void performBack(AccessibilityService accessibilityService) {
        if (accessibilityService != null && VERSION.SDK_INT >= 16) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            accessibilityService.performGlobalAction(1);
        }
    }

    public static void performClick(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo != null) {
            if (accessibilityNodeInfo.isClickable()) {
                accessibilityNodeInfo.performAction(16);
            } else {
                performClick(accessibilityNodeInfo.getParent());
            }
        }
    }

    public static AccessibilityNodeInfo findViewByClassName(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            CharSequence className = child.getClassName();
            if (className != null && className.toString().equals(str)) {
                return child;
            }
        }
        return null;
    }

    public static void findDialogAndClick(AccessibilityService accessibilityService, String str, String str2) {
        AccessibilityNodeInfo rootInActiveWindow = accessibilityService.getRootInActiveWindow();
        if (rootInActiveWindow != null) {
            List findAccessibilityNodeInfosByText = rootInActiveWindow.findAccessibilityNodeInfosByText(str);
            List findAccessibilityNodeInfosByText2 = rootInActiveWindow.findAccessibilityNodeInfosByText(str2);
            if (!findAccessibilityNodeInfosByText.isEmpty() && !findAccessibilityNodeInfosByText2.isEmpty()) {
                Iterator it = findAccessibilityNodeInfosByText.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    AccessibilityNodeInfo accessibilityNodeInfo = (AccessibilityNodeInfo) it.next();
                    if (accessibilityNodeInfo != null && str.equals(accessibilityNodeInfo.getText())) {
                        performClick(accessibilityNodeInfo);
                        break;
                    }
                }
            }
        }
    }




    public static void forceClick(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("input tap ");
        sb.append(String.valueOf(i));
        sb.append(" ");
        sb.append(String.valueOf(i2));
        String sb2 = sb.toString();
        try {
            OutputStream outputStream = Runtime.getRuntime().exec("su").getOutputStream();
            outputStream.write(sb2.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}