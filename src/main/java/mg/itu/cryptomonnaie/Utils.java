package mg.itu.cryptomonnaie;

public final class Utils {

    public static String toCamelCase(String snakeCase) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean nextUpperCase = false;

        for (char c : snakeCase.toCharArray()) {
            if (c == '_') nextUpperCase = true;
            else {
                stringBuilder.append(nextUpperCase ? Character.toUpperCase(c) : c);
                nextUpperCase = false;
            }
        }

        return stringBuilder.toString();
    }
}
