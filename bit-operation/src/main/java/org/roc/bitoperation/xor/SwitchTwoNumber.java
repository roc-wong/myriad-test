package org.roc.bitoperation.xor;

/**
 * @author roc
 * @since 2020/5/18 13:49
 */
public class SwitchTwoNumber {

    public static void main(String[] args) {

        int a = 3, b = 5;
        a = a ^ b;
        b = b ^ a;
        a = a ^ b;
        System.out.println("result a = " + a + ", b = " + b);
    }
}
