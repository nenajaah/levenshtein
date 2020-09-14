/* Levenshtein algorithm implementation by Eerika Haajanen
 * Non recursive method used for better time complexity


Resources used:
https://rosettacode.org/wiki/Levenshtein_distance
https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
*/

package levenshtein;

public class Levenshtein {

	/*
	 * Levenshtein method returning the distance between token 1 and token 2
	 */
	static int levenshtein(String token1, String token2) {
		if (token1 == token2)
			return 0;
		int len1 = token1.length() + 1;
		int len2 = token2.length() + 1;

		// The array of distances
		int[] cost = new int[len1];
		int[] newcost = new int[len1];

		// Initial cost of skipping prefix in token1
		for (int i = 0; i < len1; i++)
			cost[i] = i;

		// Dynamically compute the array of distances

		// Transformation cost for each letter in token2
		for (int j = 1; j < len2; j++) {
			// Initial cost of skipping prefix in token2
			newcost[0] = j;

			// Transformation cost for each letter in token1
			for (int i = 1; i < len1; i++) {
				// Current letters match in both strings
				int match = (token1.charAt(i - 1) == token2.charAt(j - 1)) ? 0 : 1;

				// Compute cost of all possible transformations
				int cost_replace = cost[i - 1] + match;
				int cost_insert = cost[i] + 1;
				int cost_delete = newcost[i - 1] + 1;

				// Store minimum cost value
				newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
			}

			// Swap cost/newcost arrays
			int[] swap = cost;
			cost = newcost;
			newcost = swap;
		}

		// The distance is the cost for transforming all letters in both strings
		return cost[len1 - 1];
	}

	private static int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	/*
	 * Modified version of levenshtein method which makes an early exit if the
	 * distance exceeds a maximum distance
	 */
	static int levenshtein(String token1, String token2, int maxDist) {
		if (token1 == token2)
			return 0;
		int len1 = token1.length();
		int len2 = token2.length();

		// If either string is empty, only option is to insert all letters of other
		// string
		if (len1 == 0)
			return len2;
		if (len2 == 0)
			return len1;
		if (len1 < len2) {
			// Swap so that shorter string is token1
			int tl = len1;
			len1 = len2;
			len2 = tl;
			String ts = token1;
			token1 = token2;
			token2 = ts;
		}

		// The array of distances
		int[] cost = new int[len2 + 1];

		for (int i = 1; i <= len1; i += 1) {
			cost[0] = i;
			int prv = i - 1;
			int min = prv;
			for (int j = 1; j <= len2; j += 1) {
				// Current letters match in both strings
				int match = prv + (token1.charAt(i - 1) == token2.charAt(j - 1) ? 0 : 1);

				// Pick minimum of the possible transformations
				cost[j] = minimum(1 + (prv = cost[j]), 1 + cost[j - 1], match);
				if (prv < min)
					min = prv;
			}
			if (maxDist >= 0 && min > maxDist)
				// Distance has exceeded maximum
				return maxDist + 1;
		}
		if (maxDist >= 0 && cost[len2] > maxDist)
			return maxDist + 1;

		return cost[len2];

	}

	public static void main(String[] args) {
		/*
		 * A performance measurement on all test-cases and both variants of
		 * implementation
		 */

		String[][] test_lev = { { "Cats", "Hats" }, { "Band", "Hands" }, { "Cats", "Kansas" },
				{ "International", "Internship" } };

		// Compare real VS expected results of levenshtein method and levenshtein method
		// with max
		int[] expected_lev = { 1, 2, 4, 6 };
		int[] expected_lev_max = { 1, 2, 3, 3 };

		int correct_lev = 0;
		int correct_lev_max = 0;

		for (int i = 0; i < test_lev.length; ++i) {
			int dist = levenshtein(test_lev[i][0], test_lev[i][1]);
			System.out.println("levenshtein(" + test_lev[i][0] + ", " + test_lev[i][1] + ") = " + dist);
			if (dist == expected_lev[i])
				correct_lev++;
		}

		System.out.println();

		for (int i = 0; i < test_lev.length; ++i) {
			int dist = levenshtein(test_lev[i][0], test_lev[i][1], 2);
			System.out.println("levenshtein(" + test_lev[i][0] + ", " + test_lev[i][1] + ", 2) = " + dist);
			if (dist == expected_lev_max[i])
				correct_lev_max++;
		}

		// Calculate accuracies as performance measure (ratio of correct observations to
		// total observations)
		double acc_lev = correct_lev / 4.0;
		double acc_lev_max = correct_lev_max / 4.0;

		System.out.println();
		System.out.println("Accuracy of levenshtein method: " + acc_lev);
		System.out.println("Accuracy of levenshtein method with max: " + acc_lev_max);

	}
}
