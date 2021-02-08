import java.util.*;
/**
 * This class contains several Search & Sort algorithms to be used
 * for the Efficiency Investigation assignment.
 * Source: Geeks for Geeks
 * Edited by: Lia Silver
 */
class Algorithm {
    /* Constructor
     * */
    public Algorithm() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please input size of search set (n value): ");
        int n = sc.nextInt();
        System.out.println("Please input size of target set (k value): ");
        int k = sc.nextInt();

        int[] list = setGenerator(n);
        int[] targets = targetsGenerator(list, k);

        System.out.println("Average MA Time: " + MA(list,targets));
        System.out.println("Average MB Time: " + MB(list,targets));
    } //end Constructor

    /**
     * setGenerator method creates a search set filled with random, even integers
     * @param n size of array
     * @return array size n of random, even integers
     */
    private static int [] setGenerator (int n){
        Random rand = new Random();
        int [] array = new int[n];
        int i = 0;
        while (i < n){
            int r = rand.nextInt();
            if (r % 2 == 0){
                array[i] = r;
                i++;
            }
        }
        return array;
    }//end setGenerator

    /**
     * targetsGenerator method creates an array of k target values, half of which are in S and half are not in S
     * @param set search set
     * @param k number of k values
     * @return array of search values
     */
    private static int [] targetsGenerator (int [] set, int k){
        Random rand = new Random();
        int [] array = new int [k];
        //fill first half of array with elements from set
        for (int i = 0; i < k/2; i++){
            array[i] = set[rand.nextInt(set.length)];
        }
        int j = k/2;
        //fill second half with random odd elements
        while (j < k){
            int r = rand.nextInt();
            if (r % 2 != 0){
                array[j] = r;
                j++;
            }
        }
        return array;
    }//end targetsGenerator

    /**
     * MA method implements linear search with multiple target values
     * @param list search set array
     * @param targets array of targets
     * @return average time of 500 trials
     */
    private static long MA (int [] list, int [] targets){
        long time = 0;
        //repeat 500 times for meaningful average
        for (int i = 0; i< 500; i++){
            for(int j: targets){
                long start = System.nanoTime();
                linearSearch(list,j);
                time += System.nanoTime() - start;
            }
        }
        return time/500;
    }//end MA

    /**
     * MB method implements merge sort with binary search
     * @param list search set array
     * @param targets array of targets
     * @return average time of 500 trials + time taken to merge sort
     */
    static long MB (int [] list, int [] targets){
        long time = 0;
        long mergeTime = 0;
        //repeat 500 times for meaningful average
        for (int i = 0; i<500; i++){
            long mergeStart = System.nanoTime();
            int [] merged = mergeSort(list,0, list.length-1);
            mergeTime=System.nanoTime() - mergeStart;

            long binaryStart = System.nanoTime();
            for(int j: targets){
                binarySearch(merged,j);
            }
            time += System.nanoTime() - binaryStart;
        }
        return (time/500) + mergeTime;
    }//end MB

    /* Implementation of Linear Search.
     * @param list[] Array to be searched
     * @param item The element to be searched for
     * @return The index where the item has been found in the array,
     *         or -1 if the item has not been found.
     */
    static int linearSearch(int[] list, int item) {
        for(int i=0; i<list.length; i++) {
            // If the item is found in the list, return it
            if (list[i] == item) {
                return i;
            }
        }
        // If a value hasn't been returned by this point, the item
        // is not in the list
        return -1;
    } // end linearSearch

    /* Implementation of Binary Search.
     * @param list[] Array to be searched
     * @param item The element to be searched for
     * @return The index where the item has been found in the array,
     *         or -1 if the item has not been found.
     */
    static int binarySearch (int[] list, int item) {
        int bottom = 0; //lower bound of searching
        int top = list.length - 1; //upper bound of searching
        int middle; //current search candidate
        boolean found = false;
        int location = -1; //location of item, -1 for failure
        while (bottom <= top && !found) {
            middle = (bottom + top)/2; //integer division, auto-truncate
            if (list[middle] == item) {
                location = middle; //success!
                found = true;
            } else if (list[middle] < item) {
                bottom = middle + 1; //look only in top half
            } else {
                top = middle - 1; //look only in bottom half
            }
        }
        return location;
    } // end binarySearch

    /* Implementation of Merge sort.
     * @param arr[] Array to be sorted
     * @param l  The starting index of the array
     * @param r The ending index of the array
     * @return sorted array
     */
    static int [] mergeSort(int arr[], int l, int r) {
        if (l < r) {
            //Find the middle point
            int m = (l+r)/2;

            // Sort first and second halves
            mergeSort(arr, l, m);
            mergeSort(arr , m+1, r);

            //Merge the sorted halves
            merge(arr, l, m, r);
        }
        return arr;
    } // end mergeSort

    /* Helper method used by mergeSort().
     * This method merges two subarrays of arr[] so that they are sorted.
     * @param arr[] The array to be sorted
     * @param l The start index of the first array
     * @param m The last index of the first array
     * @param r The last index of the second array
     * @return The index of the pivot's final position
     */
    static int [] merge(int arr[], int l, int m, int r) {
        //Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        //Create temporary arrays
        int L[] = new int [n1];
        int R[] = new int [n2];

        //Copy data to temp arrays
        for (int i=0; i<n1; ++i) {
            L[i] = arr[l + i];
        }
        for (int j=0; j<n2; ++j) {
            R[j] = arr[m + 1+ j];
        }

        //Merging the temporary arrays
        //Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        //Copy remaining elements of L[] if any
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        //Copy remaining elements of R[] if any
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
        return arr;
    } // end merge
}
