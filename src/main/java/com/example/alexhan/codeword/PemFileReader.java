package com.example.alexhan.codeword;



import org.spongycastle.util.io.pem.PemObject;
import org.spongycastle.util.io.pem.PemReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by Cesar-Melchor on 12/7/16.
 */

public class PemFileReader{

    private PemObject pemObject;

    public PemFileReader(String filename) throws FileNotFoundException, IOException {
        PemReader pemReader = new PemReader(new InputStreamReader(new FileInputStream(filename)));
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
