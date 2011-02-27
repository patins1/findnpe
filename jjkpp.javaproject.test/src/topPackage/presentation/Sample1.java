package topPackage.presentation;

import java.util.Random;

public class Sample1 {

	void methodA() {
		Integer i = 3;
		if (new Random().nextBoolean())
			i = null;
		System.out.println(i.toString());
	}

}
