/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jamesframework.james.asc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {

    public static CoreSubsetData read(String file) {
        
        try {
            Scanner sc = new Scanner(new File(file));
            
            String[] values;
            
            values = nextLine(sc);
            int n = values.length;
            
            double[][] dist = new double[n][n];
            for (int r = 0; r < n; r++) {
                values = nextLine(sc);
                for (int c = 0; c < n; c++) {
                    double d = Double.parseDouble(values[c]);
                    dist[r][c] = d;
                }
            }
            
            return new CoreSubsetData(dist);
            
        } catch (FileNotFoundException ex) {
            
            System.err.println("File not found: " + file);
            System.exit(1);
            
        }
        
        return null;
        
    }
    
    private static String[] nextLine(Scanner sc) {
        return sc.next().split(",");
    }
    
}
