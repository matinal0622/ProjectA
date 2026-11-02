public class Menu implements MenuUI {
    private String[] menuOptions;

    public Menu(String[] menuOptions) {
        this.menuOptions = new String[menuOptions.length];
        for (int i = 0; i < menuOptions.length; i++) {
            this.menuOptions[i] = menuOptions[i];
        }
    }

    @Override
    public void printMenuOptions() {
        System.out.println();
        System.out.println("========== " + menuOptions[0] + " ==========");
        for (int i = 1; i < menuOptions.length; i++) {
            System.out.println(i + ". " + menuOptions[i]);
        }
        System.out.println(menuOptions.length + ". Exit");
    }

    @Override
    public int userInput() {
        return userInput(1, menuOptions.length);
    }

    public int userInput(int maxInput) {
        return userInput(0, maxInput);
    }

    public int userInput(int minInput, int maxInput) {
        while (true) {
            System.out.print("--> ");
            String input = In.nextLine().trim();
            for (int choice = minInput; choice <= maxInput; choice++) {
                if (input.equals(String.valueOf(choice))) {
                    return choice;
                }
            }
            System.out.println("Enter a number from " + minInput
                    + " to " + maxInput + ".");
        }
    }

    @Override
    public String toString() {
        return menuOptions[0] + ": " + menuOptions.length + " options";
    }
}
