package com.olklein.ffdslicence;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfFormXObject;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfImageObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import static com.itextpdf.text.pdf.PdfName.DEST;

/**
 * Created by olklein on 06/07/2017.
 */

public class clippdf {


    /**
     * Example written by Bruno Lowagie in answer to:
     * http://stackoverflow.com/questions/26773942/itext-crop-out-a-part-of-pdf-file
     */






    public static void manipulateWDSFPdf(String src, String dest)
            throws IOException, DocumentException {
        // Creating a reader
        PdfReader reader = new PdfReader(src);
        Rectangle pagesize = reader.getPageSize(1);
        float width = pagesize.getWidth();
        float height = pagesize.getHeight();
        // step 1
        //Rectangle mediabox = new Rectangle(0, 3 * height, width, 4 * height);
        //Rectangle mediabox = new Rectangle(0,  height/2, width, height);
        //Rectangle mediabox = new Rectangle(0,  0, width, height/2); //FFD
        Rectangle mediabox = new Rectangle((float) (width*0.058),  height*((float)0.778),  (width*(float)0.848), height*((float)0.963)); // WDSF
        Document document = new Document(mediabox);
        // step 2
        FileOutputStream outFile = new FileOutputStream(dest);
        PdfWriter writer
                = PdfWriter.getInstance(document, outFile);
        // step 3
        document.open();
        // step 4
        PdfContentByte content = writer.getDirectContent();
        PdfImportedPage page = writer.getImportedPage(reader, 1);
        // adding block
        content.addTemplate(page, 1, 0, 0, 1, 0, 0);

        // step 4
        document.close();
        reader.close();

    }

    public static void manipulateFFDSPdf(String src, String dest)
            throws IOException, DocumentException {
        // Creating a reader
        PdfReader reader = new PdfReader(src);
        Rectangle pagesize = reader.getPageSize(1);
        float width = pagesize.getWidth();
        float height = pagesize.getHeight();
        // step 1
        //Rectangle mediabox = new Rectangle(0, 3 * height, width, 4 * height);
        //Rectangle mediabox = new Rectangle(0,  height/2, width, height);
        //Rectangle mediabox = new Rectangle(0,  0, width, height/2); //FFD
        Rectangle mediabox = new Rectangle((float) (width*0.121),  height*((float)0.208),  (width*(float)0.897), height*((float)0.386)); // WDSF
        Document document = new Document(mediabox);
        // step 2
        FileOutputStream outFile = new FileOutputStream(dest);
        PdfWriter writer
                = PdfWriter.getInstance(document, outFile);
        // step 3
        document.open();
        // step 4
        PdfContentByte content = writer.getDirectContent();
        PdfImportedPage page = writer.getImportedPage(reader, 1);
        // adding block
        content.addTemplate(page, 1, 0, 0, 1, 0, 0);

        // step 4
        document.close();
        reader.close();

    }



}
