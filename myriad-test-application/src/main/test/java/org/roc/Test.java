package org.roc;

public class Test {
    public static void main(String[] args) {
        System.out.println("return value of test(): " + test());
    }

    public static int test() {
        int i = 1;

          if(i == 1)
              return 0;
        System.out.println("the previous statement of try block");
        i = i / 0;

        try {
            System.out.println("try block");
            return i;
        } finally {
            System.out.println("finally block");
        }
    }
}