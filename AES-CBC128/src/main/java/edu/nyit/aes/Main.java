/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.nyit.aes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Pegah
 */
public class Main {
    public static void main(String[] args) throws URISyntaxException {
    Scanner in = new Scanner(System.in);    
    List<String> wordlist = new ArrayList<String>();   
    String plainText = "255044462d312e350a25d0d4c5d80a34";
    String cipherText = "d06bf9d0dab8e8ef880660d2af65aa82";     
    String initializationVector = "09080706050403020100a2b2c2d2e2f2";
    boolean result = false;
    try {                  
        wordlist = readFromFile();
       
            result = false;
            System.out.println("Plaintext is: " + plainText);
            System.out.println("Ciphertext is: "+ cipherText);
            System.out.println("Initiaization Vector is: "+ initializationVector);
            System.out.println();           

            for (String secretKey : wordlist) {
                String encryptedString = encrypt(plainText, secretKey, initializationVector) ;               
                if (encryptedString.equals(cipherText)){
                    System.out.println("Alice secretKey is: " + secretKey);
                    result =true;
                    break;
                }                
            }
            if(!result)
                System.out.println("The key is not in the dictionary."); 
        
    } 
    catch (URISyntaxException e) {
        System.out.println( "Error: " + e.toString());
    }
}
    
    public static String padkey(String key){
    if(key.length() < 16){
        String paddingString = new String(new char[16 - key.length()]).replace("\0", "#");
        key += paddingString;
    }
    return key;
}
    public  static List<String> readFromFile() throws URISyntaxException{
        List<String> strings = new ArrayList<String>();
        try  
        { 
            File f = new File("keys.txt");
            FileReader fr=new FileReader(f);   
            BufferedReader br=new BufferedReader(fr); 

            String line;  
            while((line=br.readLine())!=null)  
            {  
                strings.add(line);
            }  
            fr.close();  
        }  
        catch(IOException e)  
        {  
            System.out.println("File not found.");
        }  
        return  strings;
    }
    public static String encrypt(String plainText, String key, String initializationVector) {
        String encryptedText = "";
        try {
            Cipher cipher   = Cipher.getInstance("AES/CBC/NoPadding");          
            SecretKeySpec secretKey = new SecretKeySpec(convertHexToBayteArray(key) , "AES");
            IvParameterSpec ivparameterspec = new IvParameterSpec(convertHexToBayteArray(initializationVector) );
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
            byte[] cipherText = cipher.doFinal(convertHexToBayteArray(plainText));
            encryptedText = convertByteArrayToHex(cipherText);
        } 
        catch (Exception e) {
             System.err.println("Encrypt Exception : "+e.getMessage());
        }
        return encryptedText;
    }

    public  static byte[] convertHexToBayteArray(String hexString){
        byte[] ans = new byte[hexString.length() / 2];       
 
        for (int i = 0; i < ans.length; i++) {
            int index = i * 2;
            int val = Integer.parseInt(hexString.substring(index, index + 2), 16);
            ans[i] = (byte)val;
        }             
        return ans;
    }

public static String convertByteArrayToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }
}
