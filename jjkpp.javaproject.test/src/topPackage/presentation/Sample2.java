package topPackage.presentation;

import java.util.Random;

public class Sample2 {

	Integer methodA() {
		if (new Random().nextBoolean())
			return null;
		return 3;
	}

	void methodB() {
		Integer i = methodA();
		System.out.println(i.toString());
	}

}
