package com.maochong.xiaojun.protocol;

import java.io.File;
import java.io.Serializable;

/**
 * @author jokin
 * @date 2018/5/24 14:27
 */
public class FileUploadFile implements Serializable {
    private static final long serialVersionUID = -1689642360199566452L;
    /**
     * 文件
     * */
    private File file;
    /**
     * 文件名
     * */
    private String fileMd5;
    /**
     * 开始位置
     * */
    private int starPos;
    /**
     * 文件字节数组
     * */
    private byte[] bytes;
    /**
     * 结尾位置
     * */
    private int endPos;
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public String getFileMd5() {
        return fileMd5;
    }
    public void setFilemd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }
    public int getStarPos() {
        return starPos;
    }
    public void setStarPos(int starPos) {
        this.starPos = starPos;
    }
    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    public int getEndPos() {
        return endPos;
    }
    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }
}
