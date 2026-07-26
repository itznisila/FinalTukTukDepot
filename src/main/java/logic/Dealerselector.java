package logic;

import model.Dealer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dealerselector {
    private Random random;

    public Dealerselector() {
        this.random = new Random();
    }

    public List<Dealer> selectRandomDealers(List<Dealer> allDealers, int count) {
        if (allDealers == null || allDealers.size() < count) {
            System.out.println("Not enough dealers to select from.");
            return new ArrayList<>();
        }

        List<Integer> pickedIndexes = new ArrayList<>();
        List<Dealer> selected = new ArrayList<>();

        while (selected.size() < count) {
            int randomIndex = random.nextInt(allDealers.size());

            if (!pickedIndexes.contains(randomIndex)) {
                pickedIndexes.add(randomIndex);
                selected.add(allDealers.get(randomIndex));
            }
        }
        return selected;
    }

    public void sortByLocation(List<Dealer> dealers) {
        for (int i = 1; i < dealers.size(); i++) {
            Dealer key = dealers.get(i);
            int j = i - 1;

            while (j >= 0 && compareDealersByLocation(dealers.get(j), key) > 0) {
                dealers.set(j + 1, dealers.get(j));
                j--;
            }
            dealers.set(j + 1, key);
        }
    }

    private int compareDealersByLocation(Dealer a, Dealer b) {
        return a.getLocation().compareToIgnoreCase(b.getLocation());
    }
}