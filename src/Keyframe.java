public class Keyframe {
    int position;
    String[] availableTypes = {"spawn", "update", "death"};
    String type;
    int value;
    String name;

    Keyframe(int positionInput, String typeInput, int intValue) {
        if(this.checkType(typeInput)) type = typeInput;
        this.position = positionInput;
        this.value = intValue;
    }

    boolean checkType(String typeInput) {
        try {
            boolean found = false;
            for (String typeToCheck : this.availableTypes) {
                if (typeToCheck.equals(typeInput)) {
                    found = true;
                }
            }

            if (!found) {
                throw new Exception("Invalid keyframe availableTypes");
            }

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    void setName(String newName) {
        this.name = newName;
    }
}
