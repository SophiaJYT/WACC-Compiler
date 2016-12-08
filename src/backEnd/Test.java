package backEnd;

public class Test {

    public static void main(String[] args) {
        int i = 1;
        while (i < 5) {
            for (int j = 0; j < 10; j++) {
                if (j >= 5) {
                    j++;
                    continue;
                }
                System.out.print(j);
            }
            System.out.println(i);
            if (i >= 2) {
                i++;
                continue;
            }
            i++;
        }
    }

}
