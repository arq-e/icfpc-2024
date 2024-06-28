package parsing;

public class Parser {
    static String dict = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_`|~ \n";
    

    public static void parse(String s) {
        String[] strs = s.split(" ");
        for (String str : strs) {
            switch(str.charAt(0)) {
                case 'S':
                    String res = parseString(str);
                    System.out.println(res);
                    break;
                default:
                    break;
            }
        }
    }

    public static String parseString(String s) {
        StringBuilder sb = new StringBuilder();
        for (char ch : s.substring(1).toCharArray()) {
            int pos = (int) ch;
            if (pos < 33) {
                System.out.println(ch);
            }
            sb.append(dict.charAt(ch - 33));
        }

        return sb.toString();
    }

    public static String convertString(String s) {
        StringBuilder sb = new StringBuilder();
        for (char ch : s.toCharArray()) {
            char newC = (char)(dict.indexOf(ch) + 33);
            sb.append((char)(dict.indexOf(ch) + 33));
        }
        return sb.toString();
    }
}
