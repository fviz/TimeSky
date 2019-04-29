public class Keyframe {
    int position;
    String[] type = {"spawn", "update"};

    Keyframe(int positionInput, String typeInput) {
        this.checkType(typeInput);

    }

    void checkType(String typeInput) {
        try {
            boolean found = false;
            for (String typeToCheck : this.type) {
                if (typeToCheck.equals(typeInput)) {
                    found = true;
                }
            }

            if (!found) {
                throw new Exception("Invalid keyframe type");
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
