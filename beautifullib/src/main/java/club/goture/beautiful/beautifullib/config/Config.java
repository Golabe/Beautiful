package club.goture.beautiful.beautifullib.config;

import club.goture.beautiful.beautifullib.enums.ZipType;

public class Config {

    private ZipType zipType;
    private int ignoreMinPixel;
    private int maxPixel;
    private boolean enablePixelZip;
    private boolean enableQualityZip;
    private int quality;
    private boolean enableOriginal;
    private String zipCacheDir;
    private int ignoreSize;
    private boolean enableProgress;

    public ZipType getZipType() {
        return zipType;
    }

    public void setZipType(ZipType zipType) {
        this.zipType = zipType;
    }

    public int getIgnoreMinPixel() {
        return ignoreMinPixel;
    }

    public void setIgnoreMinPixel(int ignoreMinPixel) {
        this.ignoreMinPixel = ignoreMinPixel;
    }



    public int getMaxPixel() {
        return maxPixel;
    }

    public void setMaxPixel(int maxPixel) {
        this.maxPixel = maxPixel;
    }

    public boolean isEnablePixelZip() {
        return enablePixelZip;
    }

    public void setEnablePixelZip(boolean enablePixelZip) {
        this.enablePixelZip = enablePixelZip;
    }

    public boolean isEnableQualityZip() {
        return enableQualityZip;
    }

    public void setEnableQualityZip(boolean enableQualityZip) {
        this.enableQualityZip = enableQualityZip;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public boolean isEnableOriginal() {
        return enableOriginal;
    }

    public void setEnableOriginal(boolean enableOriginal) {
        this.enableOriginal = enableOriginal;
    }

    public String getZipCacheDir() {
        return zipCacheDir;
    }

    public void setZipCacheDir(String zipCacheDir) {
        this.zipCacheDir = zipCacheDir;
    }

    public int getIgnoreSize() {
        return ignoreSize;
    }

    public void setIgnoreSize(int ignoreSize) {
        this.ignoreSize = ignoreSize;
    }

    public boolean isEnableProgress() {
        return enableProgress;
    }

    public void setEnableProgress(boolean enableProgress) {
        this.enableProgress = enableProgress;
    }
}
