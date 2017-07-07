package com.olklein.ffdslicence;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by olklein on 06/07/2017.
 *
 *
 *    This program is free software: you can redistribute it and/or  modify
 *    it under the terms of the GNU Affero General Public License, version 3,
 *    as published by the Free Software Foundation.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *    As a special exception, the copyright holders give permission to link the
 *    code of portions of this program with the OpenSSL library under certain
 *    conditions as described in each individual source file and distribute
 *    linked combinations including the program with the OpenSSL library. You
 *    must comply with the GNU Affero General Public License in all respects
 *    for all of the code used other than as permitted herein. If you modify
 *    file(s) with this exception, you may extend this exception to your
 *    version of the file(s), but you are not obligated to do so. If you do not
 *    wish to do so, delete this exception statement from your version. If you
 *    delete this exception statement from all source files in the program,
 *    then also delete it in the license file.
 */


class clipPDF {

    public static void clip(String src, String dest, boolean isWDSF)
            throws IOException, DocumentException {
        // Creating a reader
        PdfReader reader = new PdfReader(src);
        Rectangle pagesize = reader.getPageSize(1);
        float width = pagesize.getWidth();
        float height = pagesize.getHeight();

        Rectangle mediabox;
        if (isWDSF) {
            mediabox = new Rectangle((float) (width * 0.058), height * ((float) 0.778),
                    (width * (float) 0.848), height * ((float) 0.963)); // WDSF
        }else{
            mediabox = new Rectangle((float) (width*0.121),  height*((float)0.208),
                    (width*(float)0.897), height*((float)0.386)); // FFD
        }
        Document document = new Document(mediabox);

        FileOutputStream outFile = new FileOutputStream(dest);
        PdfWriter writer = PdfWriter.getInstance(document, outFile);

        document.open();

        PdfContentByte content = writer.getDirectContent();
        PdfImportedPage page = writer.getImportedPage(reader, 1);

        content.addTemplate(page, 1, 0, 0, 1, 0, 0);

        document.close();
        reader.close();
    }

}
