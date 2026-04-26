public class MAIN {

    public static void main(String[] args) {

        String source = """
                    adult := age >= 18;
                    print adult;
                    approved := income > 5000 and not blocked;
                    print approved;
                """;

        Scanner scanner = new Scanner(source);

        for (Token token : scanner.scanTokens()) {
            System.out.println(token);
        }
    }
}