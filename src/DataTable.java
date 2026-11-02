import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DataTable {
    private ArrayList<String> attributes;
    private HashMap<String, ArrayList<String>> rows;

    public DataTable(ArrayList<String> attributes) {
        this.attributes = new ArrayList<>(attributes);
        rows = new HashMap<>();
    }

    public ArrayList<String> getAttributes() {
        return new ArrayList<>(attributes);
    }

    public int getAttributesSize() {
        return attributes.size();
    }

    public ArrayList<String> getKeys() {
        ArrayList<String> keys = new ArrayList<>();
        for (String key : rows.keySet()) {
            keys.add(key);
        }
        Collections.sort(keys);
        return keys;
    }

    public ArrayList<String> getKeys(String reverse) {
        ArrayList<String> keys = getKeys();
        Collections.reverse(keys);
        return keys;
    }

    public boolean hasKey(String key) {
        return rows.containsKey(key);
    }

    public ArrayList<String> getRow(String key) {
        if (!rows.containsKey(key)) {
            return null;
        }
        return new ArrayList<>(rows.get(key));
    }

    public ArrayList<String> getColumn(String attribute) {
        ArrayList<String> column = new ArrayList<>();
        int index = attributes.indexOf(attribute);
        if (index < 0) {
            return column;
        }
        for (String key : getKeys()) {
            String value = getValue(key, index);
            if (value != null) {
                column.add(value);
            }
        }
        return column;
    }

    public String getValue(String key, int index) {
        if (!rows.containsKey(key)) {
            return null;
        }
        if (index < 0 || index >= rows.get(key).size()) {
            return null;
        }
        return rows.get(key).get(index);
    }

    public void updateRow(String key, ArrayList<String> newRow) {
        if (key != null && !key.trim().equals("") && newRow != null) {
            rows.put(key, new ArrayList<>(newRow));
        }
    }

    public void updateValue(String key, int index, String newValue) {
        if (key == null || key.trim().equals("") || index < 0
                || index >= attributes.size() || newValue == null) {
            return;
        }
        ArrayList<String> values = rows.get(key);
        if (values == null) {
            values = new ArrayList<>();
        }
        while (values.size() <= index) {
            values.add("");
        }
        values.set(index, newValue);
        rows.put(key, values);
    }

    public boolean removeRow(String key) {
        if (!rows.containsKey(key)) {
            return false;
        }
        rows.remove(key);
        return true;
    }

    @Override
    public String toString() {
        String result = attributes.toString() + "\n";
        for (String key : getKeys()) {
            result = result + key + ": " + rows.get(key) + "\n";
        }
        return result;
    }
}
