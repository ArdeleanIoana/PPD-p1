package repository;

import model.Tax;

import java.io.*;
import java.util.Vector;

public class TaxRepository {
    private String filePath;

    public TaxRepository(String filePath) {
        try {
            this.filePath = filePath;
            FileOutputStream fileOut = new FileOutputStream(filePath);
            fileOut.write("".getBytes());
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addTax(Tax tax) {
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath, true);
            outputStream.write(tax.toFileString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized Vector<String> getAllTax() {
        Vector<String> result = new Vector<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
