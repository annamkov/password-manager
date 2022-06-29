import java.util.Scanner;
import java.io.*;

 /*
  * A simple password manager that uses Caesar's Cipher to encrypt user credentials into a .txt file.
  * To view decrypted credentials, you must run the program.
  */

class Manager{
    private static final int SHIFT = 3;
    private static final char ASCII_LOWERCASE_A = 'a'; // ASCII-97
    private static final char ASCII_UPPERCASE_A = 'A'; // ASCII-65
    private static final int ALPHABET_SIZE = 'Z' - 'A' + 1;    //26
    private static final char ASCII_0 = '0';
    private static final String FILE = "secret.txt";

    public static void main(String args[]){
        int menu = 0;
        Scanner keyboard = new Scanner(System.in);

        try {
            File f = new File(FILE);
            if (f.createNewFile()) {  //returns false if file already exists
              System.out.println("File created: " + f.getName());
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }

        do{
            System.out.println("Press 1 to view existing passwords. \nPress 2 to add a new password. \nPress 3 to exit.");

            if (keyboard.hasNextInt()){
                menu = keyboard.nextInt();
                keyboard.nextLine();
            } else { //this part prevents an infinite loop
                keyboard.nextLine();
                continue;
            }

            switch(menu){
                case 1:
                    readFile();
                    break;
                case 2:
                    String[] input = new String[3];

                    System.out.print("Enter the service: ");
                    input[0] = keyboard.nextLine();

                    System.out.print("Enter your username: ");
                    input[1] = keyboard.nextLine();

                    System.out.print("Enter your password: ");
                    input[2] = keyboard.nextLine();

                    writeToFile(input);

                    break;
                case 3:
                    System.out.println("exiting...");
                    break;
            }

        }while(menu != 3);
        keyboard.close();
    } // end of main method

    /* */
    private static void readFile(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(FILE));
            String line = reader.readLine();
            while(line != null){
                String[] credentials = line.split("\\|"); //https://stackoverflow.com/questions/10796160/splitting-a-java-string-by-the-pipe-symbol-using-split
                System.out.println(decrypt(credentials[0]) + "|" + decrypt(credentials[1]) + "|" + decrypt(credentials[2]));
                line = reader.readLine();
            }
            reader.close();
        }catch(IOException e){
            System.out.println("Unable to read file.");
        }
    }

    /* */
    private static void writeToFile(String[] input){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true));  // boolean parameter for opening file in append mode
            writer.write(encrypt(input[0]) + "|" + encrypt(input[1]) + "|" + encrypt(input[2]));
            writer.newLine();
            writer.close();
        }catch(IOException e){
             System.out.println("Unable to write to file.");
        }
    }

    /* */
    private static String encrypt(String data){
        String encrypted = "";

        for(int i = 0; i < data.length(); i++){
            char ascii = data.charAt(i);

            if(Character.isUpperCase(ascii)){
                encrypted += (char)((ascii + SHIFT - 65) % 26 + 65);
            }else if(Character.isLowerCase(ascii)){
                encrypted += (char)((ascii + SHIFT - 97) % 26 + 97);

            }else if(Character.isDigit(ascii)){
                encrypted += (char)((ascii + SHIFT -48) % 10 +48);
            }else{ //special character
                encrypted += ascii;
            }
        }

        return encrypted;
    }

    /* */
    private static String decrypt(String data){
        String decrypted = "";

        for(int i = 0; i < data.length(); i++){
            char ascii = data.charAt(i);
            char shifted;

            if(Character.isUpperCase(ascii)){ ///***
                shifted = (char)((ascii - SHIFT - ASCII_UPPERCASE_A) % ALPHABET_SIZE + ASCII_UPPERCASE_A);
                if(shifted < ASCII_UPPERCASE_A){
                    shifted = (char)((ascii - SHIFT + ALPHABET_SIZE));
                }
                decrypted += shifted;

            }else if(Character.isLowerCase(ascii)){
                shifted = (char)((ascii - SHIFT - ASCII_LOWERCASE_A) % ALPHABET_SIZE + ASCII_LOWERCASE_A);
                if(shifted < ASCII_LOWERCASE_A){
                    shifted = (char)((ascii - SHIFT + ALPHABET_SIZE));
                }
                decrypted += shifted;

            }else if(Character.isDigit(ascii)){
                shifted = (char)((ascii - SHIFT - ASCII_0) % 10 + ASCII_0);
                if(shifted < ASCII_0){
                    shifted = (char)(ascii - SHIFT + 10);
                }

                decrypted += shifted;
            }else{ //special character
                decrypted += ascii;
            }
        }
        return decrypted;
    }
}
