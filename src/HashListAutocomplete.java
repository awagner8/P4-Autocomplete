/*@author Avi Wagner
 */
import java.util.*;

public class HashListAutocomplete implements Autocompletor {
    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap;
    private int mySize;


    public HashListAutocomplete(String[] terms, double[] weights) {
        if (terms == null || weights == null) {
            throw new NullPointerException("One or more arguments null");
        }

        if (terms.length != weights.length) {
            throw new IllegalArgumentException("terms and weights are not the same length");
        }
        initialize(terms, weights);
    }

    @Override
    public List<Term> topMatches(String prefix, int k) {
        if(prefix.length() > MAX_PREFIX) {
            prefix = prefix.substring(0, MAX_PREFIX);
        }
        if (!myMap.containsKey(prefix)) {
            return new ArrayList<>();
        }
        List<Term> all = myMap.get(prefix);
        List<Term> list = all.subList(0, Math.min(k, all.size()));
        return list;
        



    }

    @Override
    public void initialize(String[] terms, double[] weights) {
        for (int i = 0; i < terms.length; i++) {
            for (int j = 0; j < MAX_PREFIX; j++) {
                String prefix = terms[i].substring(0, j);
                if (!myMap.containsKey(prefix)) {
                    myMap.put(prefix, new ArrayList<>());
                }
                myMap.get(prefix).add(new Term(terms[i], weights[i]));
            }
        }

        for (String key : myMap.keySet()) {
            List<Term> list = myMap.get(key);
            list.sort(Comparator.comparing(Term::getWeight).reversed());
            if (list.size() > mySize) {
                list = list.subList(0, mySize);
            }
            myMap.put(key, list);
        }
        
    }

    @Override
    public int sizeInBytes() {
        if (myMap == null) {
            return 0;
        }
        return myMap.size() * mySize * 8;
    }  
}

