public class Test {


    public static void main(String[] args) {
        String number = " -81.375833";
        System.out.println(number);
        String newNumber = number.replaceAll("\\s","");
        System.out.println(newNumber);
        System.out.println(Float.parseFloat(newNumber));
    }
}