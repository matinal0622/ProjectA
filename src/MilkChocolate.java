/** A fictional milk chocolate product used by this coursework program. */
public class MilkChocolate extends Chocolate {
    public MilkChocolate(String name, int weightGrams, int cocoaPercentage) {
        super(name, weightGrams, cocoaPercentage);
    }

    @Override
    public String getType() {
        return "Milk";
    }

    @Override
    public String getIngredients() {
        return "cocoa mass, sugar, milk powder, cocoa butter";
    }
}
