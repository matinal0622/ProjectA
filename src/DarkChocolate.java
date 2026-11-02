/** A fictional dark chocolate product used by this coursework program. */
public class DarkChocolate extends Chocolate {
    public DarkChocolate(String name, int weightGrams, int cocoaPercentage) {
        super(name, weightGrams, cocoaPercentage);
    }

    @Override
    public String getType() {
        return "Dark";
    }

    @Override
    public String getIngredients() {
        return "cocoa mass, sugar, cocoa butter";
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
