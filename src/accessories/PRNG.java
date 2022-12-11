package accessories;

public final class PRNG {
	// pseudo random number generator
	// between min and max
	private int _seed = 884895;
	private double _min = -10;
	private double _max = 100;
	private static PRNG _instance;

	public static PRNG getInstance() {
		return getInstance(Double.NaN, Double.NaN);
	}

	public static PRNG getInstance(double min_, double max_) {
		if (_instance == null)
			_instance = new PRNG();

		if (min_ != Double.NaN)
			_instance.set_min(min_);
		if (max_ != Double.NaN)
			_instance.set_max(max_);

		return _instance;
	}

	public double nextUnitRand() {
		double a = _seed++ * 15485863.0;
		return (a * a * a % 2038074743) / 2038074743; // in [0,1]
	}

	public double nextDouble() {
		return (nextUnitRand() * (_max - _min)) + _min;
	}

	public int nextInt() {
		return (int) nextDouble();
	}

	private static boolean alreadyContains(final int attempt_, final int[] vector_) {
		for (int jj = 0; jj < vector_.length; jj++) {
			if (attempt_ == vector_[jj])
				return true;
		}
		return false;
	}

	public int[] generateSet(final int n_) {
		// returns an array of n different pseudo random numbers between min and max
		int[] out = new int[n_];
		int lastIndex = 0, attempt;

		do {
			attempt = nextInt();
			if (!alreadyContains(attempt, out))
				out[lastIndex++] = attempt;
		} while (lastIndex < n_);

		return out;
	}

	public void check_distribution(int nSample_, int nColumns_) {
		// illustrates a sample distribution in a histogram of nColumns_
		int width = (int) ((_max - _min) / nColumns_);// univocal width of the bins
		int sol[] = new int[nColumns_];

		for (int ii = 0; ii < nSample_; ii++) {
			int r = nextInt();
			int index = (int) Math.min((r - _min) / width, nColumns_ - 1);
			sol[index]++;
		}

		System.out.println("SRNG distribution");
		for (int jj : sol)
			System.out.println(jj);
	}

	public void set_seed(int seed_) {
		_seed = seed_;
	}

	public void set_min(double min_) {
		_min = min_;
	}

	public void set_max(double max_) {
		_max = max_;
	}
}
