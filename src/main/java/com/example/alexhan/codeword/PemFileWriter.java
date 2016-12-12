package com.example.alexhan.codeword;


import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.Key;
import java.io.FileOutputStream;

import org.spongycastle.util.io.pem.PemObject;
import org.spongycastle.util.io.pem.PemReader;
import org.spongycastle.util.io.pem.PemWriter;



/**
 * Created by Cesar-Melchor on 12/7/16.
 */

public class PemFileWriter {

    private PemObject pemObject;
    private Context con;

    public PemFileWriter (Key key, String description,Context con) {
        this.pemObject = new PemObject(description, key.getEncoded());
        this.con = con;
    }

    public void write(String filename) throws IOException {

        FileOutputStream fos = con.openFileOutput(filename, Context.MODE_PRIVATE);
        PemWriter pemWriter = new PemWriter(new OutputStreamWriter(fos));
        try {
            pemWriter.writeObject(this.pemObject);
            System.out.println("wrote file");
            System.out.println(filename);
        } finally {
            pemWriter.close();
        }
    }
}