package tk.agarsia.tictac2.model.board;

public class BoardParser {
	public static boolean testForEmpty(byte[] arr) {
		// 'for(int i=0;i<arr.len;i++)' takes 220~240ns
		// iterating is slightly faster with 190~200ns
		for (byte b : arr)
			if (b == 0)
				return true;

		return false;
	}

	public static byte testBoardForWinner(byte[] arr, int len, int wLen) {
		// this test method executes in 140~160ns
		// could be slightly faster by iterating, but we currently need x & y
		// values

		// actually i have no clue, why this win test is so much faster than the
		// simple testForEmpty() method

		byte tfh, tfb, tfv, tfs;

		for (int x = 0; x < len; x++) {
			for (int y = 0; y < len; y++) {
				// initialize parent values...
				tfv = arr[y + x * len]; 
				tfs = arr[y + x * len]; 
				tfh = arr[y + x * len]; 
				tfb = arr[y + x * len]; 

				if (tfh != 0) { // abort if field is free (no testing required)
					for (int z = 1; z < wLen; z++) {
						// test horizontal
						if (y < len - (wLen - 1)&&tfh != 0 && arr[y + x * len + z] != tfh)
								tfh = 0;

						
						// test diagonal backslash
						if (y < len - (wLen - 1)&&x < len - (wLen - 1) && tfb != 0 && arr[y + (x + z) * len + z] != tfb)
								tfb = 0;
					
						
						// test vertical
						if (x < len - (wLen - 1) && tfv != 0 && arr[y + (x + z) * len] != tfv)
								tfv = 0;
						

						// test diagonal slash
						if (x < len - (wLen - 1) && y >= len - wLen && y < len && tfs != 0 && arr[y + (x + z) * len - z] != tfs)
								tfs = 0;
					}
					
					//output the winner if we got one
					if(tfv!=0)
						return tfv;
					if(tfs!=0)
						return tfs;
					if(tfh!=0)
						return tfh;
					if(tfb!=0)
						return tfb;
				}
			}
		}

		//we have no winner :(
		return 0;
	}

	
	public static void main(String...args) {
		//need to test all cases...
		byte[] arr6 = new byte[] {
				2,2,1,1,2,2,
				1,1,2,2,1,1,
				2,2,1,1,2,2, 
				1,1,2,2,1,1,
				2,2,1,1,2,2,
				1,1,2,2,1,1
		};
		byte[] arr6b = new byte[] {
				1,1,2,2,1,1,
				2,2,1,1,2,2, 
				1,1,2,2,1,1,
				2,2,1,1,2,2,
				1,1,2,2,1,1,
				2,2,1,1,2,2
		};

		System.out.println("Test Board: (Board requires resolving all test cases)");
		System.out.println("2 2 1 1 2 2       1 1 2 2 1 1");
		System.out.println("1 1 2 2 1 1       2 2 1 1 2 2");
		System.out.println("2 2 1 1 2 2  and  1 1 2 2 1 1");
		System.out.println("1 1 2 2 1 1       2 2 1 1 2 2");
		System.out.println("2 2 1 1 2 2       1 1 2 2 1 1");
		System.out.println("1 1 2 2 1 1       2 2 1 1 2 2");
		
		
		System.out.println("Starting performance tests:\ntest for winner");
		int times = 10000000;
		int t2 = times/2;
		long start = System.nanoTime();

		for(int j = 0; j<t2; j++) {
				testBoardForWinner(arr6,6,3);
				//testing two different arrays to prevent caching...
				testBoardForWinner(arr6b,6,3);
		}
		
		long total = (System.nanoTime()-start);
		System.out.println();
		System.out.println("tested "+times+ "times in "+total+"ns, resulting an avarage of "+total/times+"ns per test!");
		
		System.out.println("\ntest for empty fields");
		start = System.nanoTime();

		for(int j = 0; j<t2; j++) {
				testForEmpty(arr6);
				//testing two different arrays to prevent caching...
				testForEmpty(arr6b);
		}
		
		total = (System.nanoTime()-start);
		System.out.println();
		System.out.println("tested "+times+ "times in "+total+"ns, resulting an avarage of "+total/times+"ns per test!");
	}
}