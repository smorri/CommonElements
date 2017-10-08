import java.util.Arrays;

/** ========================================================================================================
 * class CommonElements
 * 
 * [HOMEWORK HELP] Completed a homework assignment for an individual; provided a walkthrough of solution.
 * This is simply a quick-and-dirty solution. It functions as it should and meets the requirements
 * described in the document.
 * 
 * INSTRUCTIONS:
 * 
 * Create an  efficient algorithm for finding the common elements of a set of collections. 
 * 
 * Ideally, the goal is to achieve an algorithm that will only need to perform at most on the order of 
 * N * (k - 1) comparisons, where N is the number of elements in the query set and k is the total number of 
 * sets in the collection. This can only be achieved if each element in the non-query collections only
 * participates in at most 1 comparison (with a few exceptions)
 * 
 * - It should be able to accept as input 0 to k collections, stored as simple arrays. We’re restricting
 * 	 the data structure to arrays since we haven’t covered higher order data structures yet.
 * - The elements of the collections should all be of type Comparable, and they should all be derived from
 *   the same base class (not counting the Object class). Implementation of the Comparable interface is
 *   necessary since the elements must be compared to each other in order to determine commonality. They
 *   must all be derived from the same base class since comparisons between different data types is
 *   undefined.
 * - Duplicate elements should be allowed; e.g., if there are M instances of the value, “XYZ”, in all the
 *   input collections, there should be M instances of the value, “XYZ”, in the collection of common 
 *   elements.
 * - The collections should be allowed to be of varying lengths; i.e., some collections may have more
 *   items than others.
 * - One of the collections must be designated as the “query” collection, which is the collection
 *   containing the elements to which the elements in the other collections are compared.
 * - The total number of element comparisons performed should be less than the value for the quadratic
 *   solution described above. That is, the total number of comparisons in the worst case should be less
 *   than N^2 * (k - 1). Do not be concerned about average performance or best case performance. Also,
 *   the total number of comparisons is defined, for this assignment, to be only those comparisons that
 *   are performed once the traversal of the query collection begins, and the other collections are checked
 *   for the presence of the elements in the query collection. Any comparisons performed to manipulate
 *   the data prior to searching for the common elements should be ignored.
 *   
 *   
 * The framework for your algorithm should satisfy the following criteria, for ease in testing:
 * 
 * - Create a class called CommonElements, to contain your algorithm and associated methods and attributes.
 * - In your CommonElements class, encapsulate your algorithm within a method called findCommonElements,
 *   that has the following signature:
 * 			
 * 			public Comparable[] findCommonElements(Comparable[][] collections).
 *	    
 *   The argument to this method, collections, will be the set of k collections discussed earlier. Each
 *   collection will be represented as an array of objects of type Comparable. Note that in Java, a 2D
 *   array will support arrays of varying sizes provided it is initialized without first specifying the
 *   two dimensions. For example:
 *   
 *   		Comparable[][] collections = {{“A”}, {“A”, “B”}, {“A”, “B”, “C”}};
 *   
 *   results in an array of 3 Comparable arrays of varying sizes. The following syntax also works:
 *   
 *   		Comparable[] col_1 = {“A”};
 *   		Comparable[] col_2 = {“A”, “B”};
 *   		Comparable[] col_3 = {“A”, “B”, “C”};
 *   		Comparable[][] collections = {col_1, col_2, col_3};
 *   
 * - The value returned by your findCommonElements method should be a collection of Comparable elements
 *   that contains only the elements common to all the input collections.
 * - Since you are being asked to evaluate your algorithm based on the number of comparisons performed,
 *   you will need to have your findCommonElements method maintain a running total of comparisons
 *   performed for each set of collections tested. You should create an attribute called comparisons
 *   in your CommonElements class to store the number of comparisons, and provide a getter method called :
 *   		
 *   		getComparisons()
 *   
 *   to return this value. In order to keep a running total of comparisons, you will need to instrument
 *   your code by incrementing the comparisons attribute each time a comparison between two elements is
 *   made. Since element comparisons are typically performed in if statements, you may need to increment
 *   comparisons immediately before each comparison is actually performed. Although that may sound
 *   counter-intuitive, if you try to increment comparisons inside the if statement, after the element
 *   comparison has been made, you will miss all the comparisons that cause the condition inside the if
 *   statement to evaluate to false.
 * 
 * @author  Samone Morris
 * @version 1.1.0
 * @done	10/06/17
 * ========================================================================================================
 */
public class CommonElements {
	/** =============================================================================================== **/
	/** ========================================== VARIABLES ========================================== **/
	/** =============================================================================================== **/	
	@SuppressWarnings("rawtypes")
	private Comparable[] query_set;

	@SuppressWarnings("rawtypes")
	private final Comparable[] EMPTY_SET = {};
	
	private int query_set_size,
				num_comparisons;
	private final String EMPTY = "Ø";
	
	/** =============================================================================================== **/
	/** ======================================== CONSTRUCTORS ========================================= **/
	/** =============================================================================================== **/	
	
	public CommonElements() { }

	/** =============================================================================================== **/
	/** ===================================== METHODS / FUNCTIONS ===================================== **/
	/** =============================================================================================== **/
	
	/** ---------------------------------------------------------------------------------------------------
	 * initialize()
	 * 
	 * Resets all of our values. Since it is likely the user will re-use only one instance of this 
	 * class to check any number of collections, we can simply reset it everytime findCommonElements()
	 * is called.
	 */
	private void initialize(){
		this.query_set = null;
		this.query_set_size = -1;
		this.num_comparisons = 0;
	}// end initialize()
	
	/** ---------------------------------------------------------------------------------------------------
	 * findCommonElements(collections)
	 * 
	 * Computes the intersection of 1 or more Sets and returns a subset, S, which contains all common 
	 * elements amongst those Sets. The subset returned should be the query set, q, the smallest set
	 * contained in the collection. 
	 * 
	 * @param collections		a collection of 1 or more sets. The collection is
	 * 						    represented as a 2-dimensional array, but it is 
	 * 						    important to keep in mind that the sets 'appear'
	 * 						    as :
	 * 							collections <T> [][] = { 
	 * 								Set 0 = {...},
	 * 								Set 1 = {...},
	 * 									. . . .
     *               				Set k = {...}
     *               			};
     *               			
     *               			Each Set is a 1-dimensional array and all must be
     *               			of the same type. The Object type is not accepted.
	 * @return					a collection of elements common to all the input 
	 * 							collections
	 */
	@SuppressWarnings("rawtypes")
	public Comparable[] findCommonElements(Comparable[][] collections){
		/* ------------------------------------------------------------------------------------------------
		 * [1] SORT
		 * 
		 *  It is more efficient to sort the Sets contained in the Collection before computing the
		 *  intersection.
		 */
		try {
			initialize();
			sortSets( collections );
		} catch (Exception e) {
			e.printStackTrace();
		}// end try / catch
		
		return intersection( collections );
	}// end findCommonElements()
	
	/** ---------------------------------------------------------------------------------------------------
	 * getComparisons()
	 * 
	 * The total number of comparisions performed in the binarySearch() call
	 * 
	 * @return		number of comparisions performed
	 */
	public int getComparisons(){
		return num_comparisons;
	}// end getComparisons()
	
	/** ---------------------------------------------------------------------------------------------------
	 * intersection()
	 * 
	 * Performs the intersection of the Query Set, q, and all remaining sets in the collection. Utilizes
	 * binary search to restrict our search size.
	 * 
	 * @param collections	a collection of Sets to perform an intersection on
	 * @return				either the empty set (since the subset { Ø } is common in all Sets) if no
	 * 					    other common elements are discovered -OR- the Query Set, q UNION { Ø }
	 */
	@SuppressWarnings("rawtypes")
	private Comparable[] intersection(final Comparable[][] collections){
		/* ------------------------------------------------------------------------------------------------
		 * [0] EMPTY SET
		 * If our Query Set is the Empty Set { Ø }, then this is the smallest set that exists in the
		 * collection. The Empty Set is always a subset of any Set.
		 * ------------------------------------------------------------------------------------------------
		 */
		if( query_set_size == 0 ){
			System.out.println("[COMMON ELEMENTS] = { " + EMPTY + " }");
			return query_set;
		}// end if
		
		/* ------------------------------------------------------------------------------------------------
		 * [1] QUERY
		 * Instead we must use our smallest discovered Query Set, q, and compare it against all ( k - 1 )
		 * sets. Therefore we must perform at most N * ( k - 1 ) comparisons, where N is equal to the
		 * number of elements in Query Set, q.
		 * ------------------------------------------------------------------------------------------------
		 */
		int i = 0,
			num_sets = collections.length;
				
		for(; i < num_sets; i++){
			// We have encountered a Set that has NO commonalities with the Query Set. Therefore, we should
			// stop our search, and return the empty set { Ø }
			if( !binarySearch(collections[i]) ){
				System.out.println("[COMMON ELEMENTS] = { " + EMPTY + " }");
				return EMPTY_SET;
			}// end if
		}// end for
				
		String output = Arrays.toString( query_set );
		output = output.substring(1, output.length() - 1);
		System.out.println("[COMMON ELEMENTS] = { " + output + ", " + EMPTY + " }");
		
		return query_set;
	}// end intersection(...)
	
	
	/** ---------------------------------------------------------------------------------------------------
	 * sortSets()
	 * 
	 * Sorts all Sets contained in a Collection in lexicographic order. Does so using Bubble Sort which
	 * has Avg. and Worst Case runtime = O(n^2)
	 * 
	 * @param  collections		a collection of Sets to sort
	 * @throws Exception		if all Sets are not of the same type (empty Sets {} are an exception)
	 * 							if a Set contains 1 or more elements that are of different types
	 */
	@SuppressWarnings("rawtypes")
	private void sortSets(final Comparable[][] collections) throws Exception {
		int i = 0,
			num_sets = collections.length;
		Comparable [] set;
		Class [] types = new Class[ num_sets ];		// We need to keep track of all Set types just
													// to ensure all Sets are of the same type
													// Empty sets , {} , we can 'assume' are 
													// of the same type as the majority in this
													// array
		for(; i < num_sets; i++){
			set = collections[i];
			
			/* ------------------------------------------------------------------------------------------
			 * [1] SORT
			 * For Sets that are not empty, we need to sort the elements. 
			 * We also need to check, after a successful sort, that the current Set type is equal to the
			 * previous Set type. If not, we cannot complete the intersection of all Sets.
			 * 
			 * If the Set is empty, we know this is the smallest set we can encounter, so make this our
			 * query set. It's type can remain 'null' but we'll just assume it is of the same type as
			 * the majority of the other Sets
			 * ------------------------------------------------------------------------------------------
			 */
			if( !isEmpty( set ) ){ 
				types[i] = bubbleSort( set );
				
				if( i > 0 && types[i - 1] != null && types[i - 1] != types[i]){
					throw new Exception("At least 2 Sets are not of the same type");
				}// end if / throw 
			} else {
				query_set = null;
				query_set_size = 0;
			}// end if / else
		}// end for(...)
	}// end sortSets(...)

	
	/** ---------------------------------------------------------------------------------------------------
	 * isEmpty()
	 * 
	 * Determines if a Set is empty (has no elements)
	 * 
	 * @param set		the Set to evaluate
	 * @return			true if the size of this Set is equal to 0; false otherwise
	 */
	@SuppressWarnings("rawtypes")
	private boolean isEmpty(final Comparable[] set){
		return set.length == 0;
	}// end isEmpty(...)
	
	/** ---------------------------------------------------------------------------------------------------
	 * bubbleSort()
	 * 
	 * Performs Bubble Sort on a set. Average and Worst Case runtime is O(n^2)
	 * 
	 * @param  set			the Set to sort
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Class bubbleSort(final Comparable[] set) throws Exception {
		int i = set.length - 1,
			j = 1;
		
		while( i >= 0 ){
			j = 1;
			
			while( j <= i ){
				/* ------------------------------------------------------------------------------------------
				 * [0] SANITY CHECK
				 * Make sure every element in the Set are of the same type.
				 * Make sure every element is not of type Object.
				 * ------------------------------------------------------------------------------------------
				 */
				if( set[ j - 1 ].getClass() != set[ j ].getClass() ){
					throw new Exception("1 or more elements in the Set " + set + " are of unequal types");
				}// end if
				
				if( set[ j - 1 ].getClass() == Object.class ){
					throw new Exception("Cannot compare elements of class `Object`");
				}// end if

				/* ------------------------------------------------------------------------------------------
				 * [1] COMPARISON
				 * If the previous element is larger than the current element, we need to swap them.
				 * ------------------------------------------------------------------------------------------
				 */
				if( set[ j - 1 ].compareTo( set[ j ]) > 0 ){
					
					Comparable temp = set[ j - 1 ];
					set[ j - 1 ] = set[ j ];
					set[ j ] = temp;
				}// end if
				
				j++;
			}// end while
			
			i--;
		}// end while
		
		/* ------------------------------------------------------------------------------------------
		 * [2] QUERY SET 
		 * Determine if we need to update our Query Set. We will do this only when the size of this
		 * set is smaller than our previously stored Query Set -OR- if we have never stored a 
		 * Query Set yet 
		 * ------------------------------------------------------------------------------------------
		 */		
		if( query_set_size == -1 || set.length < query_set_size ){
			query_set = set;
			query_set_size = set.length;
		}// end if
		
		/* ------------------------------------------------------------------------------------------
		 * [3] RETURN TYPE
		 * We need to return the type of this Set. So that when we do comparisons among other Sets
		 * we can verify if they are all of the same type. 
		 * ------------------------------------------------------------------------------------------
		 */	
		return set[0].getClass();
	}// end bubbleSort(...)

	/** ----------------------------------------------------------------------------------------------------
	 * binarySearch()
	 * 
	 * Performs Binary Search on a Set against the Query Set. Accounts for duplicate elements that appear
	 * in the Query Set
	 * 
	 * @param set
	 * @return			true if we have found all of the elements that appear in the Query Set, q; false
	 * 					otherwise
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean binarySearch(final Comparable[] set){
		int i = 0,
			low,
			high,
			mid,
			diff;
		Comparable key;
		boolean found = false,		// A flag used to determine if we have discovered a match for
									// all elements in the Query Set
				duplicate;			// A flag used to account for a duplicate discovered in
									// the Query Ser
		
		/* ------------------------------------------------------------------------------------------
		 * [0] QUERY SET MATCH
		 * 
		 * We do not need to check the Query Set against itself
		 */
		if( set == query_set ){ return true; }
		
		for(; i < query_set_size; i++){
			key = query_set[i];				// Grab an element from the Query Set
			duplicate = false;				// For every iteration, we need to reset all of our
			found = false;					// indicies and flags
			low = 0;
			high = set.length - 1;
			
			/* ------------------------------------------------------------------------------------------
			 * [1] DUPLICATES
			 * 
			 * We need to check for duplicated since it is allowed in this program. If the next 
			 * element is a duplicate, then update our flag.
			 */
			if( i + 1 < query_set_size && query_set[ i + 1 ] == key){
				duplicate = true;
			}// end if
						
			/* ------------------------------------------------------------------------------------------
			 * [1] SEARCH
			 * 
			 * Now loop through our array; splitting it in half each time until we find the element in
			 * the Set.
			 * 
			 * If we have a duplicate, we need to check the next position in the Set to see if it is
			 * also a duplicate. If it is not, we need to break out of our loop.
			 */
			while( high >= low ){
				num_comparisons++;			// This check counts as a comparison. Have to perform this
											// as mentioned in the instructions.
				
				mid = ( low + high ) / 2;	// Find the middle element			
				
				diff = set[mid].compareTo( key );
				
				if( diff == 0 ){ 			// We have found the matching elements
					int next = mid + 1,
						prev = mid - 1;					
					
					if( duplicate ){
						// If there was a duplicate but we run out of bounds on the LHS of the 
						// array, then this is invalid
						if( prev == -1 && set[mid + 1] != key ){
							return false;
						}// end if
						
						// If there was a duplicate, but we run out of bounds on the RHS of the
						// array, then this is invalid
						if( next > high && set[mid - 1] != key ){
							return false;
						}// end if
						
						// If there was a duplicate and we are contained in the array, but the
						// left and right most neighbors do not match, then this is a fail case
						if( (set[ mid + 1 ] != key && set[ mid - 1 ] != key) ){
							return false;
						}// end if
					}// end if
					
					found = true;			// Otherwise, we found a match!
					break;
					// return true;
				} else if( diff < 0 ){		// The element may be in the top-half most of the Set
					low = mid + 1;
				} else if( diff > 0 ){		// The element may be in the lower-half of the Set
					high = mid - 1;
				} else if( high == low ){}
			}// end while
			
			if( !found ){ break; }			// If we reach the end of the Set without finding a 
											// match using our Query Set, then it is impossible
											// to find common elements amongst all Sets.
		}// end for
		
		return found;
	}// end binarySearch(...)
}// end Class CommonElements
