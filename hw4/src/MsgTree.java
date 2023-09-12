import java.util.ArrayList;

/**
 * @Author Tristan Sayasit
 */

public class MsgTree {
    public char payloadChar;
    public MsgTree left;
    public MsgTree right;
    public static ArrayList<Integer> bitsInCode = new ArrayList<>();
    private static int staticCharIdx = 0;

    public MsgTree(String encodingString) {
        payloadChar = encodingString.charAt(staticCharIdx);

        staticCharIdx += 1;
        left = new MsgTree(encodingString.charAt(staticCharIdx));
        if (left.payloadChar == '^') {
            left = new MsgTree(encodingString);
        }

        staticCharIdx += 1;
        right = new MsgTree(encodingString.charAt(staticCharIdx));
        if (right.payloadChar == '^') {
            right = new MsgTree(encodingString);
        }
    }

    public MsgTree(char payloadChar) {
        this.payloadChar = payloadChar;
    }

    public static void printCodes(MsgTree root, String code) {
        if (root == null) {
            return;
        }

        if (root.payloadChar != '^') {
            if (root.payloadChar == '\n') {
                System.out.println("\\n" + "         " + code);
                bitsInCode.add(code.length());
            } else {
                System.out.println(root.payloadChar + "          " + code);
                bitsInCode.add(code.length());
            }
        }

        printCodes(root.left, code + "0");
        printCodes(root.right, code + "1");
    }
}