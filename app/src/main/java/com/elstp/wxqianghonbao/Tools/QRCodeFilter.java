package com.elstp.wxqianghonbao.Tools;

import java.io.File;
import java.io.FileFilter;

public class QRCodeFilter implements FileFilter {
    public boolean accept(File file) {
        return file.getName().startsWith("mmqrcode") && file.getName().endsWith(".png");
    }
}