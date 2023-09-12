import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @Author Tristan Sayasit
 */

public class Main {
    public static int position = 0;

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);

        System.out.println("Please enter filename to decode: ");
        String fileName = scnr.next();

        ArrayList<String> fileLines = fileReader(fileName);

        String encodingString = "";
        for (int i = 0; i < fileLines.size() - 2; i++) {
            encodingString += fileLines.get(i) + '\n';
        }
        encodingString += fileLines.get(fileLines.size() - 2);
        String codeString = fileLines.get(fileLines.size() - 1);

        MsgTree encodingTree = new MsgTree(encodingString);

        System.out.println("character code\n-------------------------");
        encodingTree.printCodes(encodingTree, "");
        System.out.println("-------------------------\nMESSAGE:");

        while (position < codeString.length()) {
            decode(encodingTree, codeString);
        }

        double numBits = 0;
        for(int i = 0; i < encodingTree.bitsInCode.size(); i++) {
            numBits += encodingTree.bitsInCode.get(i);
        }
    }

    public static ArrayList fileReader(String fileName) {
        ArrayList<String> fileLines = new ArrayList<String>();

        try {
            File file = new File(fileName);
            Scanner scnr = new Scanner(file);

            while (scnr.hasNextLine()) {
                fileLines.add(scnr.nextLine());
            }
            scnr.close();
        } catch (Exception e) {
            System.out.println("ERROR: File not found. Enter path name correctly.");
        }
        return fileLines;
    }

    public static void decode(MsgTree codes, String msg) {
        while (codes.left != null && codes.right != null) {
            if (msg.charAt(position) == '0') {
                codes = codes.left;
            } else {
                codes = codes.right;
            }
            position++;
        }
        System.out.print(codes.payloadChar);
    }
}