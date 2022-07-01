package com.matrix_maeny.pdfreader.pdfs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

public class PDFModel {

    private String pdfDame;
    private String pdfPath;

    public PDFModel(String pdfDame,String pdfPath) {
        this.pdfDame = pdfDame;
        this.pdfPath = pdfPath;

    }


    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getPdfName() {
        return pdfDame;
    }

    public void setPdfDame(String pdfDame) {
        this.pdfDame = pdfDame;
    }




}
