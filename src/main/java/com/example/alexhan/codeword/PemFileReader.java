package com.example.alexhan.codeword;



import android.content.Context;

import org.spongycastle.util.io.pem.PemObject;
import org.spongycastle.util.io.pem.PemReader;
import org.spongycastle.util.io.pem.PemWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static org.spongycastle.asn1.x500.style.RFC4519Style.c;


/**
 * Created by Cesar-Melchor on 12/7/16.
 */

public class PemFileReader{

    private PemObject pemObject;
    private Context con;

    public PemFileReader(String filename,Context con) throws IOException {
        this.con = con;


        FileInputStream fis = con.openFileInput(filename);
        PemReader pemReader = new PemReader(new InputStreamReader(fis));
        try {
            this.pemObject = pemReader.readPemObject();
        } finally {
            pemReader.close();
        }
    }

    public PemObject getPemObject() {
        return pemObject;
    }

}
