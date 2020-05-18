package org.roc.algorithm;

/**
 * @author roc
 * @since 2020/4/29 10:00
 */
public class MaxString {

    public static void main(String[] args) {

//       method1();
//        method2();

        method3();

    }

    private static String method3() {

        char lastChar = 0;
        int lastTime = 0;

        char currentChar = 0;
        int currentTime = 0;

        String str = "aabbccddeeefff";
        char[] arrays = str.toCharArray();
        for (int i = 0; i < arrays.length; i++) {
            if (i == 0) {
                currentChar = arrays[0];
                currentTime = 1;
            } else if (arrays[i] == arrays[i - 1]) {
                currentTime++;
            } else {
                if (currentTime > lastTime) {
                    lastTime = currentTime;
                    lastChar = currentChar;
                }
                currentChar = arrays[i];
                currentTime = 1;
            }
        }
        if (currentTime > lastTime) {
            System.out.println(currentChar + "-> " + currentTime);
        } else {
            System.out.println(lastChar + "-> " + lastTime);
        }
        return null;
//        System.out.println("max char " + str.substring(beginIndex, endIndex + 1) + ", maxTime " + maxTime);
//        return str.substring(beginIndex, endIndex + 1);
    }

    private static String method2() {
        int maxTime = 0;
        int beginIndex = 0;
        int endIndex = 0;

        String str = "aabbccddeee";
        char[] arrays = str.toCharArray();
        for (int i = 0; i < arrays.length; i++) {
            if (i != 0 && arrays[i] == arrays[i - 1]) {
                continue;
            }
            int time = 1;
            for (int j = i + 1; j < arrays.length; j++) {
                if (arrays[i] == arrays[j]) {
                    time++;
                } else {
                    break;
                }
                if (time > maxTime) {
                    maxTime = time;
                    beginIndex = i;
                    endIndex = j;
                }
            }
        }
        System.out.println("max char " + str.substring(beginIndex, endIndex + 1) + ", maxTime " + maxTime);
        return str.substring(beginIndex, endIndex + 1);
    }

    private static String method1() {
        int maxTime = 0;
        int beginIndex = 0;
        int endIndex = 0;

        String str = "aabbccddeee";
        for (int i = 0; i < str.length(); i++) {
            if (i != 0 && str.charAt(i) == str.charAt(i - 1)) {
                continue;
            }
            int time = 1;
            for (int j = i + 1; j < str.length(); j++) {
                if (str.charAt(i) == str.charAt(j)) {
                    time++;
                } else {
                    System.out.println("break me");
                    break;
                }
                if (time > maxTime) {
                    maxTime = time;
                    beginIndex = i;
                    endIndex = j;
                }
            }
        }
        System.out.println("max char " + str.substring(beginIndex, endIndex + 1) + ", maxTime " + maxTime);
        return str.substring(beginIndex, endIndex + 1);
    }
}