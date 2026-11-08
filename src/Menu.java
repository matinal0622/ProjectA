import java.util.ArrayList;
import java.util.Arrays;

public class Menu implements MenuUI {
    private ArrayList<String> menuOptions;

    public Menu(String[] menuOptions) {
        this.menuOptions = new ArrayList<>(Arrays.asList(menuOptions));
    }

    @Override
    public void printMenuOptions() {
        System.out.println();
        System.out.println("========== " + menuOptions.get(0) + " ==========");
        for (int i = 1; i < menuOptions.size(); i++) {
            System.out.println(i + ". " + menuOptions.get(i));
        }
        System.out.println(menuOptions.size() + ". Exit");
    }

    @Override
    public int userInput() {
        return userInput(1, menuOptions.size());
    }

    public int userInput(int maxInput) {
        return userInput(0, maxInput);
    }

    public int userInput(int minInput, int maxInput) {
        while (true) {
            System.out.print("--> ");
            int choice = In.nextInt();
            if (choice < minInput) {
                System.out.println("Enter a number from " + minInput + " to " + maxInput + ".");
            } else if (choice > maxInput) {
                System.out.println("Enter a number from " + minInput + " to " + maxInput + ".");
            } else {
                return choice;
            }
        }
    }

    @Override
    public String toString() {
        return menuOptions.get(0) + ": " + menuOptions.size() + " options";
    }
}
