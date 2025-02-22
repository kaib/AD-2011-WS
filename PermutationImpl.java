import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.lang.StringBuilder;

/**
 * @author Ben Rexin <benjamin.rexin@haw-hamburg.de>
 * @author Patrick Detlefsen <patrick.detlefsen@haw-hamburg.de>
 * @author Till Theis
 * @author Raimund Wege
 * @author Andreas Wimmer
 * @author Sebastian Krome
 * @author Daniel Liesener
 * @author Fenja Harbke
 * @author Felix Schmidt
 * @author Berthold Wiblishauser
 * @author Oliver Behncke <oliver.behncke@haw-hamburg.de>
 * @author Panagiotis Filippidis <panagiotis.filippidis@haw-hamburg.de>
 * @version 0.3
 * @since 2011-10-12
 */

public class PermutationImpl implements Permutation {
	private List<Integer> elements;

	private PermutationImpl(List<Integer> imageList) {
		this.elements = imageList;
	}

	/**
     * Create a new permutation
     * 
     * @param imageList a n-size list of integer [a1, ..., an]
     * @return a new permutation object from symmetric group S(n) where \u03c3(i)=ai for all 1\u2264i\u2264n
     * @throws IllegalArgumentException if \u03c3(i)=\u03c3(j) for i\u2260j or if not 1\u2264\u03c3(i)\u2264n for all 1<\u2264i\u2264n
     * @throws NullPointerException if the argument is null
     * 
     */
    public static Permutation valueOf(List<Integer> imageList) throws NullPointerException, IllegalArgumentException {
        if (imageList == null) {
            throw new NullPointerException();
        } else if (!checkPreconditionList(imageList, imageList.size())) {
            throw new IllegalArgumentException();
        }
        return new PermutationImpl(imageList);
    }
    
    public static Permutation s(int...ints){
    	List<Integer> result = new ArrayList<Integer>();
    	for(int elem : ints){
    		result.add(elem);
    	}
    	return PermutationImpl.valueOf(result);
    }
    
    private static boolean checkForDuplicatesInList(List<Integer> list) {
        boolean result = true;
        if (list.size() != (new HashSet<Integer>(list)).size()) {
            result = false;
        }
        return result;
    }

    private static boolean checkForElementsOutOfRange(List<Integer> list, int size) {
        boolean result = true;
        for (int i : list) {
            if (i < 1 || i > size) {
                	result = false;
            	}
        }
        	return result;
    	}

  	  private static boolean checkPreconditionList(List<Integer> list, int size) {
        	return checkForDuplicatesInList(list) && checkForElementsOutOfRange(list, size);
    	}

	/**
	 * accessor method for permutation elements
	 * 
	 * @return ArrayList<Integer>
	 */
	protected List<Integer> getElements() {
		return elements;
	}

	/**
	 * @author Andreas Wimmer
	 * @author Sebastian Krome
	 */
	public Permutation inverse() {
		// inverse: List<Integer> --> List<Integer> --- gibt die Inverse
		// Darstellung von Sigma aus (als Liste)
		// Bsp.: [1,2,3]->[1,2,3]; [3,4,2,1] -> [4,3,1,2] [1] ->[1]; [] -> []
		Map<Integer, Integer> inverse = new HashMap<Integer, Integer>();
		List<Integer> result = new ArrayList<Integer>();

		// map funktion invertieren, d.h. keys werden values und values werden
		// keys
		inverse = invert(getElementsAsMap());

		// result erzeugen mit der noetigen groesse, gefuellt mit Nullen
		result = createArray(this.getElements().size());

		// inverse in Array gie§en

		for (Map.Entry<Integer, Integer> entry : inverse.entrySet()) {
			result.set(entry.getKey() - 1, entry.getValue());
		}

		return new PermutationImpl(result);

	}

	/**
	 * @author Andreas Wimmer
	 * @author Sebastian Krome
	 */
	public static List<Integer> createArray(int n) {
		// erzeugeArray: int --> List<Integer> -- erzeugt einen Array mit der
		// Laenge n, gefuellt mit Nullen
		// Bsp.: erzeugeArray(3) -->[0,0,0]
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			result.add(0);
		}
		return result;
	}

	/**
	 * @author Andreas Wimmer
	 * @author Sebastian Krome
	 */
	public static Map<Integer, Integer> invert(Map<Integer, Integer> m) {
		// invert Map<Integer,Integer> --> Map<Integer,Integer> -- vertauscht
		// die keys und values
		// Bsp.: {1->2; 2->3; 3->1} --> {2->1; 3->2; 1->3}
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		for (Map.Entry<Integer, Integer> entry : m.entrySet()) {
			result.put(entry.getValue(), entry.getKey());
		}
		return result;
	}

	/**
	 * @author Daniel Liesener
	 * @author Fenja Harbke
	 */
	public Set<List<Integer>> allCycles() {
		// Wandelt Permutation in Cycle Notation um
		// Bsp.: [2,1,3] -> [[2,1][3]]
		Map<Integer, Integer> elementsMap = getElementsAsMap();

        return new HashSet<List<Integer>>(getAllCycles_(new ArrayList<List<Integer>>(),
                                          elementsMap, 1));
	}

	/**
	 * @author Ben Rexin <benjamin.rexin@haw-hamburg.de>
	 * @author Patrick Detlefsen <patrick.detlefsen@haw-hamburg.de>
	 * @author Andreas Wimmer
	 * @author Sebastian Krome
	 * @return
	 */
	private List<List<Integer>> getAllCyclesAsList() {
		Map<Integer, Integer> elementsMap = getElementsAsMap();

		return getAllCycles_(new ArrayList<List<Integer>>(), elementsMap, 1);
	}

	/**
	 * @author Ben Rexin <benjamin.rexin@haw-hamburg.de>
	 * @author Patrick Detlefsen <patrick.detlefsen@haw-hamburg.de>
	 * @author Andreas Wimmer
	 * @author Sebastian Krome
	 * @return
	 */
	private Map<Integer, Integer> getElementsAsMap() {
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		for (int i = 0; i < this.getElements().size(); i++) {
			result.put(i + 1, this.getElements().get(i));
		}
		return result;
	}

	/**
	 * @author Daniel Liesener
	 * @author Fenja Harbke
	 */
	private List<List<Integer>> getAllCycles_(List<List<Integer>> totalCycle,
			Map<Integer, Integer> map, int currentKey) {
		// Hilfsfunktion fuer getAllCycles() , getAllCyclesAsList()
		int newCurrentKey;
		List<Integer> singleCycle = new ArrayList<Integer>();
		// Einzelnen Cycle bestimmen
		while (map.containsKey(currentKey)) {
			newCurrentKey = map.get(currentKey); 	// Wert bestimmen durch Key
			singleCycle.add(newCurrentKey); 	// Wert zum Cycle hinzufŸgen
			map.remove(currentKey); 		// Wert aus Map entfernen
			currentKey = newCurrentKey; 		// Wert fuer naechsten Key festlegen
		}
		// Wenn singleCycle leer ist nicht zum Endergebnis hinzufuegen
		if (!singleCycle.isEmpty()) {
			totalCycle.add(singleCycle);
		}
		// Wenn Map nicht leer ist weiter suchen
		if (!map.isEmpty()) {
			getAllCycles_(totalCycle, map, currentKey + 1);
		}
		return totalCycle;
	}

	/**
	 * @author Ben Rexin <benjamin.rexin@haw-hamburg.de>
	 * @author Patrick Detlefsen <patrick.detlefsen@haw-hamburg.de>
	 * @author Andreas Wimmer
	 * @author Sebastian Krome
	 * @author Daniel Liesener
	 * @author Fenja Harbke
	 */
	public List<Integer> cycle(int index) throws IllegalArgumentException {
		try {
			return getAllCyclesAsList().get(index - 1);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @author Ben Rexin <benjamin.rexin@haw-hamburg.de>
	 * @author Patrick Detlefsen <patrick.detlefsen@haw-hamburg.de>
	 */
	@Override
	public Permutation compose(Permutation other) throws NullPointerException, IllegalArgumentException {
		// Checks:
		// Same cardinality
		// Same range (1...n)
		// -> Both are Permutation objects, should be valid

		// Example:
		// [2,4,5,1,3] this
		// [3,5,1,4,2] other
		// [5,4,2,3,1] composite
		if (other == null) {
			throw new NullPointerException();
		}
		
		if (permutationClass() != other.permutationClass()) {
			throw new IllegalArgumentException();
		}

		ArrayList<Integer> resultList = new ArrayList<Integer>();
		for (Integer element : this) {
			resultList.add(other.sigma(element));
		}
		Permutation result = new PermutationImpl(resultList);
		return result;
	}

	/**
	 * @author Ben Rexin <benjamin.rexin@haw-hamburg.de>
	 * @author Patrick Detlefsen <patrick.detlefsen@haw-hamburg.de>
	 */
	public int hashCode() {
		// Delegate HashCode to element list
		return getElements().hashCode();
	}

	/**
	 * @author Ben Rexin <benjamin.rexin@haw-hamburg.de>
	 * @author Patrick Detlefsen <patrick.detlefsen@haw-hamburg.de>
	 */
	public boolean equals(Object other) {
		boolean result = false;

		// Reference Test
		if (this == other) {
			result = true;
		}
		// Type test (instanceof)
		else if (other instanceof Permutation) {
			// Attribute test
			if (this.permutationClass() == ((Permutation) other)
					.permutationClass()
					&& this.getElements().equals(
							((PermutationImpl) other).getElements())) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * @author Ben Rexin <benjamin.rexin@haw-hamburg.de>
	 * @author Patrick Detlefsen <patrick.detlefsen@haw-hamburg.de>
	 */
	public Iterator<Integer> iterator() {
		return getElements().iterator();
	}

	/**
	 * @author Ben Rexin <benjamin.rexin@haw-hamburg.de>
	 * @author Patrick Detlefsen <patrick.detlefsen@haw-hamburg.de>
	 * @author Andreas Wimmer
	 * @author Sebastian Krome
	 */
	@Override
	public int sigma(int index) throws IllegalArgumentException {
		try {
			return getElements().get(index - 1); // -1 cause typicaly sigma
													// starts at 1, arrays at 0
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @author Ben Rexin <benjamin.rexin@haw-hamburg.de>
	 * @author Patrick Detlefsen <patrick.detlefsen@haw-hamburg.de>
	 * @author Andreas Wimmer
	 * @author Sebastian Krome
	 */
	@Override
	public Set<Integer> fixedPoints() {
		Set<Integer> result = new HashSet<Integer>();

		for (Map.Entry<Integer, Integer> e : getElementsAsMap().entrySet()) {
			if (e.getKey().equals(e.getValue())) {
				result.add(e.getValue());
			}
		}

		return result;
	}

	@Override
	public int permutationClass() {
		return getElements().size();
	}

    /**
     * @author Raimund Wege
     * @author Till Theis
     */
    @Override
	public String toString() {
        return listToString(getElements());
	}

	/**
	 * @author Raimund Wege
	 * @author Till Theis
	 */
	@Override
	public String toCycleNotationString() {
        // use the list variant to have an order wich is 'nicer' to read
        // (cycles which include lower values are put first).
        //
        // where allCycles() would return (5 3)(2 4 1), getAllCyclesAsList()
        // returns (2 4 1)(5 3).
        List<List<Integer>> cycles = getAllCyclesAsList();

        // use StringBuilder for faster String construction.
        StringBuilder builder = new StringBuilder();

        for (List<Integer> cycle : cycles) {
            builder.append(listToString(cycle));
        }

        return builder.toString();
	}

    /**
     * Return a the String representation of a List that is compatible with
     * the cycle notation.
     *
     * Arrays.asList(1,2,3).toString() == "[1, 2, 3]"
     * listToString(Arrays.asList(1,2,3)) == "(1 2 3)"
     */
    private String listToString(List<Integer> elems) {
        // use StringBuilder for faster String construction.
        StringBuilder builder = new StringBuilder();

        builder.append("("); // open surrounding parentheses
        for (int elem : elems) {
            builder.append(elem);
            builder.append(" ");
        }
        builder.deleteCharAt(builder.length()-1); // remove space at the end
        builder.append(")"); // close surrounding parentheses

		return builder.toString();
    }
    
    
	/**
	 * @author Kai Bielenberg
	 * @author Tobias Mainusch
	 */
    // Gibt die Order der Permutation aus.
    public int order(){
    	if (this.getElements().isEmpty()) 
    		return 0;
    	else{
    		Set<List<Integer>> pCycle = this.allCycles();
    		List<Integer> cycleLength = new ArrayList<Integer>();
    	for(List<Integer> cycle : pCycle){
    		cycleLength.add(cycle.size());
    	}
    	return kgv(cycleLength);}
    }
	/**
	 * @author Kai Bielenberg
	 * @author Tobias Mainusch
	 */
    //Berechnet ggt von 2 Zahlen, ben�tigt zur KGV berechnung.
    private int ggt(int m, int n) {
    	if(n == 0) return m;
    	else return ggt(n,m%n);
    }
    
	/**
	 * @author Kai Bielenberg
	 * @author Tobias Mainusch
	 */
    //KGV Berechnung 2er Zahlen
    private int kgv(int m, int n){
    	int o = 0;
    	o = ggt(m,n);
    	return (m*n) / o;	
    }
    
	/**
	 * @author Kai Bielenberg
	 * @author Tobias Mainusch
	 */
    //KGV Berechnung mehrerer Integer in einer Liste
    private int kgv(List<Integer> l)throws IllegalArgumentException{
    	if (l.isEmpty()){throw new IllegalArgumentException("KGV von einer Liste ohne Elemente nicht berechenbar"); }
    	
    	Iterator<Integer> it = l.iterator();	
    	int result = it.next();   	
    	
    	while(it.hasNext()) {
    		result = kgv(result, it.next());
    	}	
    	return result;   	
    }
  
}
