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
        myMap = new HashMap<>();
        for(int j=0; j < terms.length; j++) {
            int i = 0;
            while(i <= terms[j].length() && i <= 10) {
                String substring = terms[j].substring(0, i);
                if(!myMap.containsKey(substring)) {
                    myMap.put(substring, new ArrayList<>());
                    mySize += BYTES_PER_CHAR*substring.length();
                }
                myMap.get(substring).add(new Term(terms[j], weights[j]));
                mySize += BYTES_PER_CHAR*terms[j].length();
                mySize += BYTES_PER_DOUBLE;
                i++;
            }
        }
        /*for (int i = 0; i < terms.length; i++) {
            for (int j = 0; j < terms[i].length(); j++) {
                String prefix = terms[i].substring(0, j);
                if (!myMap.containsKey(prefix)) {
                    myMap.put(prefix, new ArrayList<>());
                    mySize += BYTES_PER_CHAR * prefix.length();
                }
                myMap.get(prefix).add(new Term(terms[i], weights[i]));
                mySize += BYTES_PER_CHAR * terms[i].length();
                mySize += BYTES_PER_DOUBLE;
            }
        }*/

        for (String key : myMap.keySet()) {
            Collections.sort(myMap.get(key), Comparator.comparing(Term::getWeight).reversed());
        }
        
    }

    @Override
    public int sizeInBytes() {
        return mySize;
    }  
}
