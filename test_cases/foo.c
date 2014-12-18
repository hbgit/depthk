void foo(float x) {
	int n = 0;
	while (1)
		if (x)
			if (n<60)
				n++;
			else
				n = 0;
}
