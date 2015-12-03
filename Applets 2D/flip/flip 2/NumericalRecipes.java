/* Charge Flip - NumericalRecipes.java
 * 
 */

public abstract class NumericalRecipes {
	private static void swap(double arr[], int index1, int index2) {
		double temp=arr[index1];
		arr[index1]=arr[index2];
		arr[index2]=temp;
	}

	/*
	 * Returns the kth smallest value in the array arr[1..n]. 
	 * The input array will be rearranged to have this value in location 
	 * arr[k], with all smaller elements moved to arr[1..k-1] (in arbitrary order) 
	 * and all larger elements in arr[k+1..n] (also in arbitrary order).
	 */
	public static double select(int k, double arr[]) {
		int i,ir,j,l,mid;
		double a,temp;
		l=0;
		ir=arr.length-1;
		for (;;) {
			if (ir <= l+1) { //Active partition contains 1 or 2e lements.
				if (ir == l+1 && arr[ir] < arr[l]) { //Case of 2 elements.
					swap(arr, l, ir);
				}
				return arr[k];
			} else {
				mid=(l+ir) >> 1; //Choose median of left, center, and right elements as partitioning element a. Also rearrange so that arr[l] = arr[l+1], arr[ir] = arr[l+1].
				swap(arr, mid,l+1);
				if (arr[l] > arr[ir]) {
					swap(arr, l,ir);
				}
				if (arr[l+1] > arr[ir]) {
					swap(arr, l+1,ir);
				}
				if (arr[l] > arr[l+1]) {
					swap(arr, l,l+1);
				}
				i=l+1; //Initialize pointers for partitioning.
				j=ir;
				a=arr[l+1]; //Partitioning element.
				for (;;) { //Beginning of innermost loop.
					do i++; while (arr[i] < a); //Scan up to find element > a.
					do j--; while (arr[j] > a); //Scan down to find element < a.
					if (j < i) break; //Pointers crossed. Partitioning complete.
					swap(arr, i,j);
				} //End of innermost loop.
				arr[l+1]=arr[j]; //Insert partitioning element.
				arr[j]=a;
				if (j >= k) ir=j-1; //Keep active the partition that contains the kth element.
				if (j <= k) l=i; 
			}
		}
	}
}
