public abstract class Chocolate {
    private final String name;
    private final int weightGrams;
    private final int cocoaPercentage;

    public Chocolate(String name, int weightGrams, int cocoaPercentage) {
        if (name == null) {
            this.name = "";
        } else {
            this.name = name.trim();
        }
        this.weightGrams = weightGrams;
        this.cocoaPercentage = cocoaPercentage;
    }

    public String getName() {
        return name;
    }

    public int getWeightGrams() {
        return weightGrams;
    }

    public int getCocoaPercentage() {
        return cocoaPercentage;
    }

    public abstract String getType();

    public abstract String getIngredients();

    @Override
    public String toString() {
        return name + " | " + getType() + " | " + weightGrams + " g | "
                + cocoaPercentage + "% cocoa | Ingredients: " + getIngredients();
    }

}
